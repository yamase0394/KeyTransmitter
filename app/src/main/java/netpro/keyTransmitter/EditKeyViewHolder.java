package netpro.keyTransmitter;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

public class EditKeyViewHolder extends RecyclerView.ViewHolder {
    private TextView name;
    private TextView description;
    private View view;
    private Key key;
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
        name = (TextView) itemView.findViewById(R.id.name);
        view = itemView.findViewById(R.id.border);
    }

    public void setKey(Key key) {
        this.key = key;

        if(key instanceof EmptyKey){
            name.setVisibility(View.INVISIBLE);
            description.setVisibility(View.INVISIBLE);
            view.setVisibility(View.INVISIBLE);
            return;
        }

        description.setText(key.getDescription());
        name.setText(key.getName());

        name.setVisibility(View.VISIBLE);
        description.setVisibility(View.VISIBLE);
        view.setVisibility(View.VISIBLE);
    }

    public void setListener(OnRecyclerClickListener listener) {
        this.listener = listener;
    }

}