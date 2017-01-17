package netpro.keytransmitter;

import android.view.MotionEvent;
import android.view.View;

/**
 * 押している間、設定した間隔でキーコードを送信し続けるボタンです
 */
public class PressingKey extends Key  {

    private boolean running;
    private long inputInterval;

    /**
     * @param inputInterval キーを送信する間隔(ミリ秒)
     */
    public PressingKey(int columnSpan, int rowSpan,String description, Type type, long inputInterval) {
        super(columnSpan, rowSpan, description, type);
        this.inputInterval = inputInterval;
    }

    @Override
    public void onActionDown(View view, MotionEvent motionEvent) {
        running = false;
        running = true;
        new Thread(new Executer()).start();
    }

    @Override
    public void onActionUp(View view, MotionEvent event) {
        running = false;
    }

    @Override
    public void onCancel() {
        running = false;
    }

    public long getInputInterval() {
        return inputInterval;
    }

    private class Executer implements Runnable {
        @Override
        public void run() {
            while (running) {
                send(keyCodeList);
                try {
                    Thread.sleep(inputInterval);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
