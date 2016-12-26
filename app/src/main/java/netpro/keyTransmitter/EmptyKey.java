package netpro.keyTransmitter;

public class EmptyKey extends Key {

    public EmptyKey() {
        super(1, 1, "", "", Type.EMPTY);
    }

    public EmptyKey(int columnSpan, int rowSpan, String name, String description, Type type) {
        super(columnSpan, rowSpan, name, description, type);
    }

}
