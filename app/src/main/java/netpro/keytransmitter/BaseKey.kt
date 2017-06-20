package netpro.keytransmitter

import android.view.MotionEvent
import android.view.View
import java.io.Serializable

abstract class BaseKey(val columnSpan: Int, val rowSpan: Int, val description: String, val type: KeyType) : Serializable {

    var keyCodesMap = mutableMapOf<String, List<String>>()

    open fun onActionDown(view: View, motionEvent: MotionEvent) {}

    open fun onActionUp(view: View, motionEvent: MotionEvent) {}

    open fun onLongClick() {}

    open fun onCancel() {}

    open fun onMove(view: View, motionEvent: MotionEvent) {}

    protected fun send(keyCodeList: List<String>?) {
        if (keyCodeList == null) {
            return
        }
        KeyTransmitter.send(keyCodeList)
    }

    companion object {
        private const val serialVersionUID = 5559614634094335358L
    }
}