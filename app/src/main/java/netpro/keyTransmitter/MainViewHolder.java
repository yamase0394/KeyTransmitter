package netpro.keyTransmitter;

import android.support.v7.widget.RecyclerView;
import android.view.View;

public class MainViewHolder extends RecyclerView.ViewHolder {
    //private TextView titleTextView;
    //private TextView summaryTextView;
    int type = 1;

    public MainViewHolder(View itemView) {
        super(itemView);
      //  titleTextView = (TextView) itemView.findViewById(R.id.textView_main);
        //summaryTextView = (TextView) itemView.findViewById(R.id.textView_sub);
    }

    public void setText(String title, String summary) {
       // titleTextView.setText(title.toUpperCase());
    }
}