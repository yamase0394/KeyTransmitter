package jp.gr.java_conf.snake0394.keytransmitter

import android.content.Context
import android.os.Build
import android.os.Handler
import android.preference.PreferenceManager
import android.util.Base64
import android.widget.Toast
import org.spongycastle.crypto.digests.SHA256Digest
import org.spongycastle.crypto.generators.PKCS5S2ParametersGenerator
import org.spongycastle.crypto.params.KeyParameter
import java.math.BigInteger
import java.net.*
import java.nio.charset.Charset
import java.security.KeyFactory
import java.security.spec.RSAPublicKeySpec
import java.util.*
import java.util.concurrent.TimeUnit
import javax.crypto.Cipher
import kotlin.concurrent.thread


object KeyTransmitter {
    private const val TAG = "KeyTransmitter"

    private lateinit var ip: String
    private var port: Int = 8888
    private var isSending = false
    private val aesManager = AESManager()
    private lateinit var keyExchangeServer: KeyExchangeServer
    private lateinit var context: Context

    fun run(ip: String, port: Int, context: Context) {
        KeyTransmitter.context = context
        KeyTransmitter.ip = ip
        KeyTransmitter.port = port
        keyExchangeServer = KeyExchangeServer()
        keyExchangeServer.run()
    }

    fun stop() {
        keyExchangeServer.stop()
    }

    fun restart(ip: String, port: Int) {
        stop()
        Thread.sleep(100)
        run(ip, port, context)
    }

    @Synchronized
    fun send(keyStringList: List<String>) {
        if (isSending || keyStringList.isEmpty()) {
            Logger.d(TAG, "cancel sending")
            return
        }

        isSending = true

        thread {
            var sendMsg = ""
            keyStringList.forEach { keyStr -> sendMsg += keyStr + "," }
            sendMsg = aesManager.encrypt(sendMsg)
            val sendByte = sendMsg.toByteArray(Charset.forName("UTF-8"))

            try {
                DatagramSocket().use { sendSocket ->
                    val sendPacket = DatagramPacket(sendByte, sendByte.size, InetSocketAddress(ip, port))
                    sendSocket.send(sendPacket)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                isSending = false
            }
        }
    }

    private class KeyExchangeServer {
        private var isRunning = false
        private lateinit var server: ServerSocket
        private lateinit var base64EncryptedIvAndSessionKey: ByteArray

        init {
            initSessionKey()
        }

        fun run() {
            server = ServerSocket(port)
            isRunning = true
            thread {
                Logger.d(TAG, "run KeyExchangeServer")
                try {
                    while (true) {
                        Logger.d(TAG, "KeyExchangeServer starts waiting connection")
                        server.accept().use sock@ {sock ->
                            Logger.d(TAG, "client connected")

                            val input = sock.getInputStream().bufferedReader(Charsets.UTF_8)
                            val output = sock.getOutputStream().bufferedWriter(Charsets.UTF_8)

                            if (input.readLine() != "connect") {
                                return@sock
                            }

                            val modulus = BigInteger(1, Base64.decode(input.readLine(), Base64.NO_WRAP))
                            val exponent = BigInteger(1, Base64.decode(input.readLine(), Base64.NO_WRAP))
                            val pubKeySpec = RSAPublicKeySpec(modulus, exponent)
                            val keyFactory = KeyFactory.getInstance("RSA")
                            val pubKey = keyFactory.generatePublic(pubKeySpec)
                            val cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding")
                            cipher.init(Cipher.ENCRYPT_MODE, pubKey)

                            output.write(Base64.encodeToString(cipher.doFinal("ok".toByteArray(Charsets.UTF_8)), Base64.NO_WRAP))
                            output.newLine()
                            output.flush()

                            sock.soTimeout = TimeUnit.SECONDS.toMillis(10).toInt()

                            output.write(Base64.encodeToString(cipher.doFinal(base64EncryptedIvAndSessionKey), Base64.NO_WRAP))
                            output.newLine()
                            output.flush()

                            val encrypted: String
                            try {
                                encrypted = input.readLine()
                            } catch (e: SocketTimeoutException) {
                                initSessionKey()
                                return@sock
                            }

                            val values = encrypted.split("?")
                            //IVと暗号文の組でない
                            if (values.size != 2) {
                                when (values[0]) {
                                    "e1" -> {
                                        Handler(context.mainLooper).post {
                                            Toast.makeText(context, "パスワードが一致していません", Toast.LENGTH_LONG).show()
                                        }
                                    }
                                    else -> {
                                        Logger.d(TAG, "abnormal message:${values[0]}")
                                    }
                                }

                                initSessionKey()
                                return@sock
                            }

                            val serverMsg = aesManager.decrypt(Base64.decode(values[0], Base64.NO_WRAP), Base64.decode(values[1], Base64.NO_WRAP)).toString(Charsets.UTF_8)
                            if (serverMsg != "ok") {
                                Logger.d(TAG, "abnormal message:$serverMsg")
                                initSessionKey()
                                return@sock
                            }
                            Logger.d(TAG, "receive ok")

                            output.write(Build.MODEL)
                            output.newLine()
                            output.flush()
                        }
                    }
                } catch (e: SocketException) {
                    if (!isRunning) {
                        //stop()が呼ばれた
                        return@thread
                    }
                    throw e
                } finally {
                    Logger.d(TAG, "close KeyExchangeServer")
                }
            }
        }

        private fun initSessionKey() {
            aesManager.initKey()

            val sp = PreferenceManager.getDefaultSharedPreferences(context)
            val keyStoreManager = KeyStoreManager.getInstance(context)
            val encryptedPw = sp.getString("pass", "")
            val pwBytes: ByteArray
            if (encryptedPw.isNullOrEmpty()) {
                pwBytes = "".toByteArray(Charsets.UTF_8)
            } else {
                pwBytes = keyStoreManager.decrypt(Base64.decode(encryptedPw, Base64.NO_WRAP))
            }
            val gen = PKCS5S2ParametersGenerator(SHA256Digest())
            gen.init(pwBytes, "終末なにしてますか?忙しいですか?救ってもらっていいですか?".toByteArray(Charsets.UTF_8), 4096)
            val secKey = (gen.generateDerivedParameters(256) as KeyParameter).key
            Arrays.fill(pwBytes, 0)
            base64EncryptedIvAndSessionKey = aesManager.encrypt(aesManager.rawKey, secKey).toByteArray(Charsets.UTF_8)
            Arrays.fill(secKey, 0)
        }

        fun stop() {
            isRunning = false
            server.close()
        }
    }
}
