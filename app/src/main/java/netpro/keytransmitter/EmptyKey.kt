package netpro.keytransmitter

/**
 * 空を表すボタンです
 */
class EmptyKey : Key {

    constructor() : super(1, 1, "", Key.Type.EMPTY)

    constructor(columnSpan: Int, rowSpan: Int, description: String, type: Key.Type) : super(columnSpan, rowSpan, description, type)

    companion object {
        private val serialVersionUID = -1696906106555982373L
    }

}
