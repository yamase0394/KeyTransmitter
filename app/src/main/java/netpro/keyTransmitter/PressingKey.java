package netpro.keytransmitter;

/**
 * 押している間キーコードを送信し続けるキーです
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
    public void onActionDown() {
        running = false;
        running = true;
        new Thread(new Executer()).start();
    }

    @Override
    public void onActionUp() {
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
                send();
                try {
                    Thread.sleep(inputInterval);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
