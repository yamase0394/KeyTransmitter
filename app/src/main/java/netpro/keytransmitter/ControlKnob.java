package netpro.keytransmitter;

import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class ControlKnob extends Key {
    private static final long serialVersionUID = -3872636246983991948L;

    private List<String> rotateLeftKeyCodeList;
    private List<String> rotateRightKeyCodeList;
    //前のタッチ座標
    private float preX;
    private float preY;
    //MotionEventから得られるx,y座標が入れ替わることによりつまみが揺れることを抑える
    private boolean preGapIsPositive;
    private boolean preGapIsNegative;

    /**
     * @param columnSpan
     * @param rowSpan
     * @param description
     * @param type
     */
    public ControlKnob(int columnSpan, int rowSpan, String description, Type type) {
        super(columnSpan, rowSpan, description, type);
        rotateLeftKeyCodeList = new ArrayList<>();
        rotateRightKeyCodeList = new ArrayList<>();
    }

    @Override
    public List<String> getKeyCodeList() {
        return new ArrayList<>();
    }

    @Override
    public void addKeyCode(String keyCode) {
    }

    @Override
    public void setKeyCodeList(List<String> keyCodeList) {
    }

    public List<String> getRotateLeftKeyCodeList() {
        return rotateLeftKeyCodeList;
    }

    public void setRotateLeftKeyCodeList(List<String> rotateLeftKeyCodeList) {
        this.rotateLeftKeyCodeList = rotateLeftKeyCodeList;
    }

    public List<String> getRotateRightKeyCodeList() {
        return rotateRightKeyCodeList;
    }

    public void setRotateRightKeyCodeList(List<String> rotateRightKeyCodeList) {
        this.rotateRightKeyCodeList = rotateRightKeyCodeList;
    }

    @Override
    public void onActionDown(View view, MotionEvent motionEvent) {
        preX = motionEvent.getX();
        preY = motionEvent.getY();
    }

    @Override
    public void onMove(View view, MotionEvent motionEvent) {
        Float a = preX - view.getWidth() / 2;
        Float b = preY - view.getHeight() / 2;
        Float c = motionEvent.getX() - view.getWidth() / 2;
        Float d = motionEvent.getY() - view.getHeight() / 2;
        preX = motionEvent.getX();
        preY = motionEvent.getY();
        //前のタッチ位置とviewの中心とのラジアン
        double atanA = Math.atan2(b, a);
        //現在ののタッチ位置とviewの中心とのラジアン
        double atanB = Math.atan2(d, c);
        double degreeA = Math.toDegrees(atanA);
        if (degreeA == 0 || degreeA == 360) {
            degreeA %= 360;
        } else if (degreeA < 0) {
            degreeA += 360;
        }
        double degreeB = Math.toDegrees(atanB);
        if (degreeB == 0 || degreeB == 360) {
            degreeB %= 360;
        } else if (degreeB < 0) {
            degreeB += 360;
        }

        int gap = (int) (degreeB - degreeA);
        //差が大きすぎる場合は異常値とみなす
        if (Math.abs(gap) > 30) {
            return;
        }
        if (gap > 0) {
            if (!preGapIsPositive) {
                preGapIsPositive = true;
                preGapIsNegative = false;
                return;
            }
            send(rotateRightKeyCodeList);
        } else if (gap == 0) {
            return;
        } else {
            if (!preGapIsNegative) {
                preGapIsPositive = false;
                preGapIsNegative = true;
                return;
            }
            send(rotateLeftKeyCodeList);
        }
        view.setRotation(view.getRotation() + (float) (gap));
    }

}
