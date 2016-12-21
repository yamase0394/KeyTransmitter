package netpro.keyTransmitter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

public class KeyViewHolder extends RecyclerView.ViewHolder implements View.OnTouchListener, View.OnLongClickListener {
    private TextView name;
    private TextView description;
    private Key key;

    public KeyViewHolder(View itemView) {
        super(itemView);
        itemView.setOnTouchListener(this);
        itemView.setOnLongClickListener(this);
        description = (TextView) itemView.findViewById(R.id.description);
        name = (TextView) itemView.findViewById(R.id.name);
        //  titleTextView = (TextView) itemView.findViewById(R.id.textView_main);
        //summaryTextView = (TextView) itemView.findViewById(R.id.textView_sub);
    }

    public void setKey(Key key) {
        this.key = key;
        description.setText(key.getDescription());
        name.setText(key.getName());
    }

    public void setText(String name, String description) {
        // titleTextView.setText(title.toUpperCase());
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        Log.d("KeyViewHolder", motionEvent.toString());
        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_DOWN:
                key.onActionDown();
                return true;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                key.onActionUp();
                break;
        }
        return true;
    }

    @Override
    public boolean onLongClick(View view) {
        key.onLongClick();
        return false;
    }
}