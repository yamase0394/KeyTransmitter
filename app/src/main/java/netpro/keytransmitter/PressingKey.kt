package netpro.keytransmitter

import android.view.MotionEvent
import android.view.View

/**
 * 押している間、設定した間隔でキーコードを送信し続けるボタンです
 */
class PressingKey
/**
 * @param inputInterval キーを送信する間隔(ミリ秒)
 */
(columnSpan: Int, rowSpan: Int, description: String, type: Key.Type, val inputInterval: Long) : Key(columnSpan, rowSpan, description, type) {

    private var running: Boolean = false

    override fun onActionDown(view: View, motionEvent: MotionEvent) {
        running = false
        running = true
        Thread(Executer()).start()
    }

    override fun onActionUp(view: View, motionEvent: MotionEvent) {
        running = false
    }

    override fun onCancel() {
        running = false
    }

    private inner class Executer : Runnable {
        override fun run() {
            while (running) {
                send(keyCodeList)
                try {
                    Thread.sleep(inputInterval)
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }

            }
        }
    }
}
