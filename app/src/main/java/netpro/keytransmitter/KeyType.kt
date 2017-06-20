package netpro.keytransmitter

/**
 * Created by snake0394 on 2017/06/20.
 */
enum class KeyType constructor(val description: String) {
    RELEASED("指を離したとき"),
    LONG_PRESS("長押し"),
    PRESSING("押している間"),
    EMPTY("何もしない"),
    KNOB("つまみ"),
    FLICK("フリック");

    companion object {
        private val toTypeMap = HashMap<String, KeyType>()

        init {
            for (type in values()) {
                toTypeMap.put(type.description, type)
            }
        }

        fun toType(description: String): KeyType? {
            return toTypeMap[description]
        }
    }
}