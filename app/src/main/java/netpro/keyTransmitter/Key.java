package netpro.keyTransmitter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class Key implements Serializable {

    private static final long serialVersionUID = 5559614634094335358L;

    public enum Type {
        RELEASED("指を離したとき"),
        LONGPRESS("長押し"),
        PRESSING("押している間"),
        SEEKBAR("バー"),
        EMPTY("何もしない");

        private String description;
        private static Map<String, Type> toTypeMap = new HashMap<>();

        static {
            for (Type type : values()) {
                toTypeMap.put(type.description, type);
            }
        }

        Type(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }

        public static Type toType(String description) {
            return toTypeMap.get(description);
        }
    }

    protected int columnSpan;
    protected int rowSpan;
    protected String name;
    protected String description;
    protected Type type;
    protected List<String> keyCodeList;

    /**
     * @param columnSpan
     * @param rowSpan
     * @param name
     * @param description
     */
    public Key(int columnSpan, int rowSpan, String name, String description, Type type) {
        this.columnSpan = columnSpan;
        this.rowSpan = rowSpan;
        this.name = name;
        this.description = description;
        this.type = type;
        keyCodeList = new ArrayList<>();
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

    public List<String> getKeyCodeList() {
        return keyCodeList;
    }

    public void setKeyCodeList(List<String> keyCodeList) {
        this.keyCodeList = keyCodeList;
    }

    public void addKeyCode(String keyCode) {
        keyCodeList.add(keyCode);
    }

    public void onActionDown() {
    }

    public void onActionUp() {
    }

    public void onLongClick() {
    }

    public void onCancel() {
    }
}