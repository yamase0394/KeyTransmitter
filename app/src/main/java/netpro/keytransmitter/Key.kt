package netpro.keytransmitter

import android.view.MotionEvent
import android.view.View
import java.io.Serializable
import java.util.*

abstract class Key(columnSpan: Int, rowSpan: Int, description: String, type: Key.Type) : Serializable {

    enum class Type constructor(val description: String) {
        RELEASED("指を離したとき"),
        LONGPRESS("長押し"),
        PRESSING("押している間"),
        EMPTY("何もしない"),
        KNOB("つまみ"),
        FLICK("フリック");

        companion object {
            private val toTypeMap = HashMap<String, Type>()

            init {
                for (type in values()) {
                    toTypeMap.put(type.description, type)
                }
            }

            fun toType(description: String): Type? {
                return toTypeMap[description]
            }
        }
    }

    var columnSpan: Int = 0
        protected set
    var rowSpan: Int = 0
        protected set
    var description: String
        protected set
    var type: Type
        protected set
    var keyCodeList: MutableList<String>
        get

    init {
        this.columnSpan = columnSpan
        this.rowSpan = rowSpan
        this.description = description
        this.type = type
        keyCodeList = mutableListOf()
    }

    open fun addKeyCode(keyCode: String) {
        keyCodeList.add(keyCode)
    }

    open fun onActionDown(view: View, motionEvent: MotionEvent) {}

    open fun onActionUp(view: View, motionEvent: MotionEvent) {}

    open fun onLongClick() {}

    open fun onCancel() {}

    open fun onMove(view: View, motionEvent: MotionEvent) {}

    protected fun send(keyCodeList: List<String>) {
        KeyTransmitter.send(keyCodeList)
    }

    companion object {
        private const val serialVersionUID = 5559614634094335358L
    }
}