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
    private var ip: String? = null
    private var port: Int = 0

    fun send(keyStringList: List<String>) {
        if (keyStringList.isEmpty()) {
            Log.d("KeyTransmitter", "list is empty")
            return
        }

        var sendMsg = ""
        keyStringList.forEach { keyStr ->
            sendMsg += keyStr + ","
        }

        try {
            val sendByte = sendMsg.toByteArray(Charset.forName("UTF-8"))

            thread {
                Log.d("KeyTransmitter", "start sending")
                DatagramSocket().use { sendSocket ->
                    val sendPacket = DatagramPacket(sendByte, sendByte.size, InetSocketAddress(ip, port))
                    sendSocket.send(sendPacket)
                }
                Log.d("KeyTransmitter", "complete sending")
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    fun setIp(ip: String?) {
        this.ip = ip
    }

    fun setPort(port: Int) {
        this.port = port
    }
}
