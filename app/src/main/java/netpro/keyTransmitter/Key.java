package netpro.keyTransmitter;

import java.io.Serializable;

public abstract class Key implements Serializable {

    private static final long serialVersionUID = 5559614634094335358L;

    public enum Type {
        RELEASED,
        LONGPRESS,
        PRESSING,
        SEEKBAR,
        EMPTY;
    }

    protected int columnSpan;
    protected int rowSpan;
    protected String name;
    protected String description;
    protected Type type;

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

    public void onActionDown() {

    }

    public void onActionUp() {

    }

    public void onLongClick() {

    }
}