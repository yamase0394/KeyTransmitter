package netpro.keyTransmitter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

public class KeyViewHolder extends RecyclerView.ViewHolder {
    private TextView name;
    private TextView description;

    public KeyViewHolder(View itemView) {
        super(itemView);
      //  titleTextView = (TextView) itemView.findViewById(R.id.textView_main);
        //summaryTextView = (TextView) itemView.findViewById(R.id.textView_sub);
    }

    public void setText(String name, String description) {
       // titleTextView.setText(title.toUpperCase());
    }
}