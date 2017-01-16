package netpro.keytransmitter;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class KeyViewHolder extends RecyclerView.ViewHolder implements View.OnTouchListener, View.OnLongClickListener {
    private TextView description;
    private TextView type;
    private TextView keyName;
    private View view;
    private Key key;
    private View borderType;
    private ImageView knobImageView;


    public KeyViewHolder(View itemView) {
        super(itemView);
        CardView cardView = (CardView) itemView.findViewById(R.id.cardView);
        cardView.setOnTouchListener(this);
        cardView.setOnLongClickListener(this);
        description = (TextView) itemView.findViewById(R.id.description);
        view = itemView.findViewById(R.id.border);
        view = itemView.findViewById(R.id.border);
        borderType = itemView.findViewById(R.id.border_type);
        keyName = (TextView) itemView.findViewById(R.id.key_name);
        type = (TextView) itemView.findViewById(R.id.type);
        knobImageView = (ImageView) itemView.findViewById(R.id.image_view_knob);
    }

    public void setKey(Key key) {
        this.key = key;

        if (key instanceof ControlKnob) {
            knobImageView.setVisibility(View.VISIBLE);
        } else {
            knobImageView.setVisibility(View.GONE);
        }

        if (key instanceof EmptyKey) {
            description.setVisibility(View.INVISIBLE);
            view.setVisibility(View.INVISIBLE);
            borderType.setVisibility(View.INVISIBLE);
            keyName.setVisibility(View.INVISIBLE);
            type.setVisibility(View.INVISIBLE);
            return;
        }

        description.setText(key.getDescription());
        type.setText(key.getType().getDescription());
        String keyNames = "";
        for (String str : key.getKeyCodeList()) {
            if (keyNames.length() == 0) {
                keyNames += str;
                continue;
            }
            keyNames += " + " + str;
        }
        keyName.setText(keyNames);

    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_DOWN:
                key.onActionDown(view, motionEvent);
                break;
            case MotionEvent.ACTION_CANCEL:
                Log.d("keyViewHolder", "cancel");
                key.onCancel();
                break;
            case MotionEvent.ACTION_UP:
                Log.d("keyViewHolder", "up");
                key.onActionUp(view, motionEvent);
                break;
            case MotionEvent.ACTION_MOVE:
                Log.d("keyViewHolder", "move");
                key.onMove(view, motionEvent);
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