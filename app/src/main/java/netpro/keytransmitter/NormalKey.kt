package netpro.keytransmitter

import android.view.MotionEvent
import android.view.View

/**
 * 指を離したときにキーを送信するボタンです
 */
class NormalKey(columnSpan: Int, rowSpan: Int, description: String, type: Key.Type) : Key(columnSpan, rowSpan, description, type) {

    override fun onActionUp(view: View, motionEvent: MotionEvent) {
        send(keyCodeList)
    }

    companion object {
        private val serialVersionUID = 8877586457351207301L
    }
}
