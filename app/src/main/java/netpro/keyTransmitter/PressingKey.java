package netpro.keyTransmitter;

import android.util.Log;

/**押している間*/
public class PressingKey extends Key implements Runnable{
    private boolean running;

    public PressingKey(int columnSpan, int rowSpan, String name, String description, Type type) {
        super(columnSpan, rowSpan, name, description, type);
    }

    @Override
    public void onActionDown() {
        running = true;
        new Thread(this).start();
    }

    @Override
    public void onActionUp() {
        Log.d("pressing","ヤメロォ");
        running = false;
    }

    @Override
    public void run() {
        while (running) {
            Log.d("Pressing", "押してる");
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
