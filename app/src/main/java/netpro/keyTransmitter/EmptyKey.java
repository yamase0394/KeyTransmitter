package netpro.keyTransmitter;

import java.io.Serializable;

public class EmptyKey extends Key implements Serializable{

    private static final long serialVersionUID = -8169291257134576691L;

    public EmptyKey() {
        super(1, 1, "", "", Type.EMPTY);
    }

    public EmptyKey(int columnSpan, int rowSpan, String name, String description, Type type) {
        super(columnSpan, rowSpan, name, description, type);
    }

}
