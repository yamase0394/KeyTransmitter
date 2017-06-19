package netpro.keytransmitter

import android.util.Log
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetSocketAddress
import java.nio.charset.Charset
import kotlin.concurrent.thread

/**
 * キーの名前をレシーバーに送信するためのクラス
 */
object KeyTransmitter {
    //private const val TAG  = "KeyTransmitter"

    var ip: String? = null
        set
    var port: Int = 0
        set

    private var isSending = false

    @Synchronized
    fun send(keyStringList: List<String>) {
        if (isSending || keyStringList.isEmpty()) {
            return
        }

        isSending = true

        thread {
            var sendMsg = ""
            keyStringList.forEach { keyStr -> sendMsg += keyStr + "," }

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
}
