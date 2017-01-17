package netpro.keytransmitter;

import android.view.MotionEvent;
import android.view.View;

/**
 * 指を離したときにキーを送信するボタンです
 */
public class NormalKey extends Key {

    private static final long serialVersionUID = 8877586457351207301L;

    public NormalKey(int columnSpan, int rowSpan, String description, Type type) {
        super(columnSpan, rowSpan, description, type);
    }

    @Override
    public void onActionUp(View view, MotionEvent motionEvent) {
        send(keyCodeList);
    }
}
