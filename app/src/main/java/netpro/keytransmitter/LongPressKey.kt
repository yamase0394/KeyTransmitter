package netpro.keytransmitter

/**
 * 長押ししたときにキーを送信するボタンです
 */
class LongPressKey(columnSpan: Int, rowSpan: Int, description: String, type: Key.Type) : Key(columnSpan, rowSpan, description, type) {

    override fun onLongClick() {
        send(keyCodeList)
    }

    companion object {

        private val serialVersionUID = -7412635502613056996L
    }
}
