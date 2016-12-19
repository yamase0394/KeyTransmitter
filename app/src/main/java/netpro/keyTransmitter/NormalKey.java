package netpro.keyTransmitter;

import android.util.Log;

public class NormalKey extends Key {
    public NormalKey(int columnSpan, int rowSpan, String name, String description, Type type) {
        super(columnSpan, rowSpan, name, description, type);
    }

    @Override
    public void onActionDown() {
        Log.d("KeyViewHolder", "送信:話したとき");
    }
}
