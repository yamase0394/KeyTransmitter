package netpro.keytransmitter;

public class NormalKey extends Key {

    private static final long serialVersionUID = 8877586457351207301L;

    public NormalKey(int columnSpan, int rowSpan, String description, Type type) {
        super(columnSpan, rowSpan, description, type);
    }

    @Override
    public void onActionUp() {
        send(keyCodeList);
    }
}
