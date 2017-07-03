package jp.gr.java_conf.snake0394.keytransmitter

import android.content.Context
import android.os.Build
import android.os.Handler
import android.preference.PreferenceManager
import android.util.Base64
import android.widget.Toast
import org.spongycastle.crypto.digests.SHA256Digest
import org.spongycastle.crypto.generators.PKCS5S2ParametersGenerator
import org.spongycastle.crypto.params.ECPublicKeyParameters
import org.spongycastle.crypto.params.KeyParameter
import org.spongycastle.crypto.util.PublicKeyFactory
import org.spongycastle.jce.ECNamedCurveTable
import org.spongycastle.jce.provider.BouncyCastleProvider
import org.spongycastle.jce.spec.ECPublicKeySpec
import java.net.*
import java.nio.charset.Charset
import java.security.KeyFactory
import java.security.KeyPairGenerator
import java.security.SecureRandom
import java.security.spec.ECGenParameterSpec
import java.util.*
import java.util.concurrent.TimeUnit
import javax.crypto.KeyAgreement
import kotlin.concurrent.thread

object KeyTransmitter {
    private const val TAG = "KeyTransmitter"

    private lateinit var ip: String
    private var port: Int = 8888
    private var isSending = false
    private val aesManager = AESKeyManager()
    private var keyExchangeServer: KeyExchangeServer? = null
    private lateinit var context: Context

    fun run(ip: String, port: Int, context: Context) {
        if (keyExchangeServer != null && keyExchangeServer!!.isRunning) {
            return
        }

        KeyTransmitter.context = context
        if (ip.isEmpty()) {
            Handler(KeyTransmitter.context.mainLooper).post {
                Toast.makeText(KeyTransmitter.context, "IPアドレスを設定してください", Toast.LENGTH_LONG).show()
            }
            return
        }
        KeyTransmitter.ip = ip
        KeyTransmitter.port = port
        keyExchangeServer = KeyExchangeServer()
        keyExchangeServer!!.run()
    }

    fun stop() {
        keyExchangeServer?.stop()
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
            var sendMsg = "${System.currentTimeMillis()}\r\n"
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
        var isRunning = false
            private set
            get
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

                        val provider = BouncyCastleProvider()
                        val ecKeyGen = KeyPairGenerator.getInstance("ECDH", provider)
                        ecKeyGen.initialize(ECGenParameterSpec("prime256v1"), SecureRandom())
                        val keyPair = ecKeyGen.generateKeyPair()

                        server.accept().use sock@ { sock ->
                            Logger.d(TAG, "client connected")

                            val input = sock.getInputStream().bufferedReader(Charsets.UTF_8)
                            val output = sock.getOutputStream().bufferedWriter(Charsets.UTF_8)

                            //ECDH鍵共有
                            //公開情報を受信
                            val csPubKeyByte = Base64.decode(input.readLine(), Base64.NO_WRAP)
                            val params = ECNamedCurveTable.getParameterSpec("prime256v1")
                            val csPubKeyParam = PublicKeyFactory.createKey(csPubKeyByte) as ECPublicKeyParameters
                            val keyFactory = KeyFactory.getInstance("ECDH", provider)
                            val csPubKey = keyFactory.generatePublic(ECPublicKeySpec(csPubKeyParam.q, params))

                            //公開情報を送信
                            output.write(Base64.encodeToString(keyPair.public.encoded, Base64.NO_WRAP))
                            output.newLine()
                            output.flush()

                            //共通鍵を生成
                            val keyAgree = KeyAgreement.getInstance("ECDH", provider)
                            keyAgree.init(keyPair.private)
                            keyAgree.doPhase(csPubKey, true)
                            val dhSecKey = keyAgree.generateSecret()

                            sock.soTimeout = TimeUnit.SECONDS.toMillis(10).toInt()

                            output.write(aesManager.encrypt(base64EncryptedIvAndSessionKey, dhSecKey))
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

                            output.write(aesManager.encrypt(Build.MODEL))
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
            base64EncryptedIvAndSessionKey = aesManager.encrypt(aesManager.key, secKey).toByteArray(Charsets.UTF_8)
            Arrays.fill(secKey, 0)
        }

        fun stop() {
            isRunning = false
            server.close()
        }
    }
}
