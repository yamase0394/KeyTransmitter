package netpro.keytransmitter

/**
 * 長押ししたときにキーを送信するボタンです
 */
class LongPressKey(columnSpan: Int, rowSpan: Int, description: String, type: KeyType) : BaseKey(columnSpan, rowSpan, description, type) {

    val NORMAL = "normal"

    override fun onLongClick() {
        send(keyCodesMap[NORMAL])
    }

    companion object {
        private val serialVersionUID = -7412635502613056996L
    }
}
