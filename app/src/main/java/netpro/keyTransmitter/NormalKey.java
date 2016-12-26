package netpro.keyTransmitter;

import android.util.Log;

public class NormalKey extends Key {

    private static final long serialVersionUID = 8877586457351207301L;

    public NormalKey(int columnSpan, int rowSpan, String name, String description, Type type) {
        super(columnSpan, rowSpan, name, description, type);
    }

    @Override
    public void onActionUp() {
        Log.d("KeyViewHolder", "送信:話したとき");
    }
}
