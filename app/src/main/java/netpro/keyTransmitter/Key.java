package netpro.keyTransmitter;

import java.io.Serializable;

public class Key implements Serializable{

    private static final long serialVersionUID = 5559614634094335356L;

    public enum Type {
        RELEASED,
        LONGPRESS,
        PRESSING,
        SEEKBAR,
        EMPTY;
    }

    private int columnSpan;
    private int rowSpan;
    private String name;
    private String description;
    private Type type;

    public Key(int columnSpan, int rowSpan, String name, String description, Type type) {
        this.columnSpan = columnSpan;
        this.rowSpan = rowSpan;
        this.name = name;
        this.description = description;
        this.type = type;
    }

    public int getColumnSpan() {
        return columnSpan;
    }

    public int getRowSpan() {
        return rowSpan;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Type getType() {
        return type;
    }

    public static Key getEmptyKey() {
        return new Key(1, 1, "", "", Type.EMPTY);
    }
}