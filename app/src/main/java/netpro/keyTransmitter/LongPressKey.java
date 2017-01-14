package netpro.keytransmitter;

public class LongPressKey extends Key {

    private static final long serialVersionUID = -7412635502613056996L;

    public LongPressKey(int columnSpan, int rowSpan, String description, Type type) {
        super(columnSpan, rowSpan, description, type);
    }

    @Override
    public void onLongClick() {
        send(keyCodeList);
    }
}
