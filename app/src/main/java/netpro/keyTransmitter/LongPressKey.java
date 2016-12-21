package netpro.keyTransmitter;

import android.util.Log;

import java.io.Serializable;

public class LongPressKey extends Key implements Serializable {

    private static final long serialVersionUID = 3646210596673098217L;

    public LongPressKey(int columnSpan, int rowSpan, String name, String description, Type type) {
        super(columnSpan, rowSpan, name, description, type);
    }

    @Override
    public void onLongClick() {
        Log.d("KeyViewHolder", "長押し");
    }
}
