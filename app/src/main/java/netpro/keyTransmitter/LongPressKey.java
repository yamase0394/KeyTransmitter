package netpro.keyTransmitter;

import android.util.Log;

public class LongPressKey extends Key {
    public LongPressKey(int columnSpan, int rowSpan, String name, String description, Type type) {
        super(columnSpan, rowSpan, name, description, type);
    }

    @Override
    public void onLongClick() {
        Log.d("KeyViewHolder", "長押し");
    }
}
