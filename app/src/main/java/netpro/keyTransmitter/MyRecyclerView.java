package netpro.keyTransmitter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class MyRecyclerView extends RecyclerView{

    private boolean isEditable;

    public MyRecyclerView(Context context) {
        super(context);
    }

    public MyRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MyRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent e) {
        if(isEditable){
            return super.onInterceptTouchEvent(e);
        }
        //falseにすると子ViewのMotionEventがキャンセルされなくなる
        //しかし、RecyclerViewのイベントがキャンセルされる
        //編集モードを作る
        return false;
    }

    public void setEditable(boolean isEditable) {
        this.isEditable = isEditable;
    }
}
