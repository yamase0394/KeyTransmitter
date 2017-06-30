package jp.gr.java_conf.snake0394.keytransmitter

import android.view.MotionEvent
import android.view.View
/**
 * 指を離したときにキーを送信するボタンです
 */
class NormalKey(columnSpan: Int, rowSpan: Int, description: String, type: KeyType) : BaseKey(columnSpan, rowSpan, description, type) {

    val NORMAL = "normal"

    override fun onActionUp(view: View, motionEvent: MotionEvent) {
        send(keyCodesMap[NORMAL])
    }
    companion object {
        private val serialVersionUID = 8877586457351207301L
    }
}
