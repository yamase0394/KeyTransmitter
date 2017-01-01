package netpro.keytransmitter;

public class EmptyKey extends Key {

    private static final long serialVersionUID = -1696906106555982373L;

    public EmptyKey() {
        super(1, 1, "", "", Type.EMPTY);
    }

    public EmptyKey(int columnSpan, int rowSpan, String name, String description, Type type) {
        super(columnSpan, rowSpan, name, description, type);
    }

}
