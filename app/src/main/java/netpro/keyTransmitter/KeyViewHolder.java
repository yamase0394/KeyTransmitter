package netpro.keyTransmitter;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

public class KeyViewHolder extends RecyclerView.ViewHolder implements View.OnTouchListener, View.OnLongClickListener {
    private TextView name;
    private TextView description;
    private View view;
    private Key key;

    public KeyViewHolder(View itemView) {
        super(itemView);
        CardView cardView = (CardView) itemView.findViewById(R.id.cardView);
        cardView.setOnTouchListener(this);
        cardView.setOnLongClickListener(this);
        description = (TextView) itemView.findViewById(R.id.description);
        name = (TextView) itemView.findViewById(R.id.name);
        view = itemView.findViewById(R.id.border);
    }

    public void setKey(Key key) {
        this.key = key;

        if (key instanceof EmptyKey) {
            name.setVisibility(View.INVISIBLE);
            description.setVisibility(View.INVISIBLE);
            view.setVisibility(View.INVISIBLE);
            return;
        }

        description.setText(key.getDescription());
        name.setText(key.getName());
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_DOWN:
                key.onActionDown();
                break;
            case MotionEvent.ACTION_CANCEL:
                key.onCancel();
                break;
            case MotionEvent.ACTION_UP:
                key.onActionUp();
                break;
        }
        return false;
    }

    @Override
    public boolean onLongClick(View view) {
        key.onLongClick();
        return false;
    }
}