package netpro.keyTransmitter;

import android.util.Log;

import java.io.Serializable;

public class NormalKey extends Key implements Serializable{

    private static final long serialVersionUID = 7924753387473217953L;

    public NormalKey(int columnSpan, int rowSpan, String name, String description, Type type) {
        super(columnSpan, rowSpan, name, description, type);
    }

    @Override
    public void onActionUp() {
        Log.d("KeyViewHolder", "送信:話したとき");
    }
}
