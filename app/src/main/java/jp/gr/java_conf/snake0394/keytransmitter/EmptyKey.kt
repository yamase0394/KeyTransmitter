package jp.gr.java_conf.snake0394.keytransmitter

/**
 * 空を表すボタンです
 */
class EmptyKey : BaseKey {

    constructor() : super(1, 1, "", KeyType.EMPTY)

    constructor(columnSpan: Int, rowSpan: Int, description: String, type: KeyType) : super(columnSpan, rowSpan, description, type)

    companion object {
        private val serialVersionUID = -1696906106555982373L
    }

}
