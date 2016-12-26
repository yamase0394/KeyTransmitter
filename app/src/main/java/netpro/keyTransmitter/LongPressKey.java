package netpro.keyTransmitter;

import android.util.Log;

public class LongPressKey extends Key {

    private static final long serialVersionUID = -7412635502613056996L;

    public LongPressKey(int columnSpan, int rowSpan, String name, String description, Type type) {
        super(columnSpan, rowSpan, name, description, type);
    }

    @Override
    public void onLongClick() {
        Log.d("KeyViewHolder", "長押し");
    }
}
