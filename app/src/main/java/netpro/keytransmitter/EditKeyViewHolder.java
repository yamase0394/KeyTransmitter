package netpro.keytransmitter;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class EditKeyViewHolder extends RecyclerView.ViewHolder {
    private TextView description;
    private TextView type;
    private TextView keyName;
    private View view;
    private Key key;
    private View borderType;
    private ImageView knobImageView;
    private OnRecyclerClickListener listener;

    public EditKeyViewHolder(View itemView) {
        super(itemView);
        CardView cardView = (CardView) itemView.findViewById(R.id.cardView);
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    listener.onClickListener(getAdapterPosition(), key);
                }
            }
        });
        description = (TextView) itemView.findViewById(R.id.description);
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

        description.setText(key.getDescription());

        description.setVisibility(View.VISIBLE);
        view.setVisibility(View.VISIBLE);
    }

    public void setListener(OnRecyclerClickListener listener) {
        this.listener = listener;
    }

}