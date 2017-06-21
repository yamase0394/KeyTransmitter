package netpro.keytransmitter

import android.util.Base64
import android.util.Log
import java.net.*
import java.nio.charset.Charset
import javax.crypto.Cipher
import javax.security.cert.X509Certificate
import kotlin.concurrent.thread


/**
 * キーの名前をレシーバーに送信するためのクラス
 */
object KeyTransmitter {
    //private const val TAG  = "KeyTransmitter"

    private lateinit var ip: String
    private var port: Int = 8888
    private lateinit var pass: String
    private var isSending = false
    private val aesManager = AESManager()
    private val keyExchangeServer = KeyExchangeServer()

    fun init(ip: String, port: Int, pass: String) {
        this.ip = ip
        this.port = port
        this.pass = pass

        keyExchangeServer.run()
    }

    @Synchronized
    fun send(keyStringList: List<String>) {

        if (isSending || keyStringList.isEmpty()) {
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

        @Synchronized
        fun run(){
            if(isRunning){
                return
            }
            isRunning = true
            Log.d("keyExchange", "run")
            thread {
                ServerSocket(port).use { server ->
                    while (true) {
                        var serverMsg = ""
                        server.accept().use { sock ->
                            val input = sock.getInputStream()
                            val output = sock.getOutputStream()

                            serverMsg = input.bufferedReader(Charsets.UTF_8).readLine()
                            Log.d("serverMsg", serverMsg)
                            if (serverMsg == "connect") {
                                output.bufferedWriter(Charsets.UTF_8).write("ok\r\n")
                                output.flush()
                            } else {
                                output.bufferedWriter(Charsets.UTF_8).write("close\r\n")
                                output.flush()
                            }
                        }

                        if(serverMsg == "connect"){
                            exchangeKey()
                        }
                    }
                }
            }
        }

        private fun exchangeKey(){
            var certData = ByteArray(0)
            Socket(ip, port).use { sock ->
                val input = sock.getInputStream()
                val output = sock.getOutputStream()

                output.write("connect\r\n".toByteArray(Charsets.UTF_8))
                output.flush()

                var serverMsg = input.bufferedReader(Charsets.UTF_8).readLine()
                certData = Base64.decode(serverMsg, Base64.DEFAULT)
            }

            val cert = X509Certificate.getInstance(certData)
            val serverPubKey = cert.publicKey

            val cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding")
            cipher.init(Cipher.ENCRYPT_MODE, serverPubKey)
            val aesKeyEncrypted = Base64.encodeToString(cipher.doFinal(aesManager.rawKey), Base64.NO_WRAP)

            Socket(ip, port).use { sock ->
                val input = sock.getInputStream()
                val output = sock.getOutputStream()

                output.write((aesKeyEncrypted + "\r\n").toByteArray(Charsets.UTF_8))
                output.flush()

                val serverMsg = input.bufferedReader(Charsets.UTF_8).readLine()
                Log.d("serverMsg", serverMsg)
            }
        }
    }
}
