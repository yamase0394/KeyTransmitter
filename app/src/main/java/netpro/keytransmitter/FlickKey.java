package netpro.keytransmitter;

import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class FlickKey extends Key {

    private static final long serialVersionUID = -5499597086336673528L;

    // 最後にタッチされた座標
    private float startTouchX;
    private float startTouchY;

    // 現在タッチ中の座標
    private float nowTouchedX;
    private float nowTouchedY;

    private List<String> flickUpKeyStrList;
    private List<String> flickDownKeyStrList;
    private List<String> flickRightKeyStrList;
    private List<String> flickLeftKeyStrList;


    // フリックの遊び部分（最低限移動しないといけない距離）
    private int adjust = 120;

    /**
     * @param columnSpan
     * @param rowSpan
     * @param description
     * @param type
     */
    public FlickKey(int columnSpan, int rowSpan, String description, Type type, int adjust) {
        super(columnSpan, rowSpan, description, type);
        this.adjust = adjust;
    }

    @Override
    public void onActionDown(View view, MotionEvent event) {
        startTouchX = event.getX();
        startTouchY = event.getY();
    }

    @Override
    public List<String> getKeyCodeList() {
        return new ArrayList<>();
    }

    @Override
    public void setKeyCodeList(List<String> keyCodeList) {

    }

    @Override
    public void onActionUp(View view, MotionEvent event) {
        nowTouchedX = event.getX();
        nowTouchedY = event.getY();

        //上
        if (startTouchY > nowTouchedY) {
            //左
            if (startTouchX > nowTouchedX) {
                if ((startTouchY - nowTouchedY) > (startTouchX - nowTouchedX)) {
                    if (startTouchY > nowTouchedY + adjust) {
                        //Log.v("Flick", "左上上");
                        // 上フリック時の処理を記述する
                        send(flickUpKeyStrList);
                    }
                } else if ((startTouchY - nowTouchedY) < (startTouchX - nowTouchedX)) {
                    if (startTouchX > nowTouchedX + adjust) {
                        //Log.v("Flick", "左上左");
                        // 左フリック時の処理を記述する
                        send(flickLeftKeyStrList);
                    }
                }
                //右
            } else if (startTouchX < nowTouchedX) {
                if ((startTouchY - nowTouchedY) > (nowTouchedX - startTouchX)) {
                    if (startTouchY > nowTouchedY + adjust) {
                        //Log.v("Flick", "右上上");
                        // 上フリック時の処理を記述する
                        send(flickUpKeyStrList);
                    }
                } else if ((startTouchY - nowTouchedY) < (nowTouchedX - startTouchX)) {
                    if (startTouchX + adjust < nowTouchedX) {
                        //Log.v("Flick", "右上右");
                        // 右フリック時の処理を記述する
                        send(flickRightKeyStrList);
                    }
                }
            }
            //下
        } else if (startTouchY < nowTouchedY) {
            //左
            if (startTouchX > nowTouchedX) {
                if ((nowTouchedY - startTouchY) > (startTouchX - nowTouchedX)) {
                    if (startTouchY + adjust < nowTouchedY) {
                        //Log.v("Flick", "左下下");
                        // 下フリック時の処理を記述する
                        send(flickDownKeyStrList);
                    }
                } else if ((nowTouchedY - startTouchY) < (startTouchX - nowTouchedX)) {
                    if (startTouchX > nowTouchedX + adjust) {
                        //Log.v("Flick", "左下左");
                        // 左フリック時の処理を記述する
                        send(flickLeftKeyStrList);
                    }
                }
                //右
            } else if (startTouchX < nowTouchedX) {
                if ((nowTouchedY - startTouchY) > (nowTouchedX - startTouchX)) {
                    if (startTouchY + adjust < nowTouchedY) {
                        //Log.v("Flick", "右下下");
                        // 下フリック時の処理を記述する
                        send(flickDownKeyStrList);
                    }
                } else if ((nowTouchedY - startTouchY) < (nowTouchedX - startTouchX)) {
                    if (startTouchX + adjust < nowTouchedX) {
                        //Log.v("Flick", "右下右");
                        // 右フリック時の処理を記述する
                        send(flickRightKeyStrList);
                    }
                }
            }
        }
    }

    public List<String> getFlickUpKeyStrList() {
        return flickUpKeyStrList;
    }

    public void setFlickUpKeyStrList(List<String> flickUpKeyStrList) {
        this.flickUpKeyStrList = flickUpKeyStrList;
    }

    public List<String> getFlickDownKeyStrList() {
        return flickDownKeyStrList;
    }

    public void setFlickDownKeyStrList(List<String> flickDownKeyStrList) {
        this.flickDownKeyStrList = flickDownKeyStrList;
    }

    public List<String> getFlickRightKeyStrList() {
        return flickRightKeyStrList;
    }

    public void setFlickRightKeyStrList(List<String> flickRightKeyStrList) {
        this.flickRightKeyStrList = flickRightKeyStrList;
    }

    public List<String> getFlickLeftKeyStrList() {
        return flickLeftKeyStrList;
    }

    public void setFlickLeftKeyStrList(List<String> flickLeftKeyStrList) {
        this.flickLeftKeyStrList = flickLeftKeyStrList;
    }

    public int getAdjust() {
        return adjust;
    }

    @Override
    public void addKeyCode(String keyCode) {
    }
}
