package netpro.keyTransmitter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.LinkedList;
import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<MainViewHolder> {
    private LinkedList<String> datasource = new LinkedList<>();
    private int displayWidth = 720;
    private int width = 768;

    @Override
    public MainViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_cardview, parent, false);
        return new MainViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MainViewHolder view, int position) {
        //MainViewHolderのサイズを変更
        if (datasource.get(position).equals("data2")) {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.height = (width - 80) / 2;
            params.width = (width - 80) / 4;
            view.itemView.setLayoutParams(params);
            view.type = 21;
        } else if (datasource.get(position).equals("data4")) {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.height = (width - 80) / 2;
            params.width = (width - 80) / 2;
            view.itemView.setLayoutParams(params);
            view.type = 22;
        } else if (datasource.get(position).equals("data1")) {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.width = (width - 80);
            params.height = (width - 80) / 4;
            view.itemView.setLayoutParams(params);
            view.type = 14;
        } else {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.height = (width - 80) / 4;
            params.width = (width - 80) / 4;
            view.itemView.setLayoutParams(params);
        }
        view.setText(datasource.get(position), datasource.get(position));
    }

    @Override
    public int getItemCount() {
        return datasource.size();
    }

    public void addView(String str) {
        datasource.add(str);
        notifyItemInserted(getItemCount() - 1);
    }

    public void addView(int index, String str) {
        datasource.add(index, str);
        notifyItemInserted(index);
    }

    public void addAllView(List<String> list) {
        int preSize = getItemCount();
        datasource.addAll(list);
        notifyItemRangeInserted(preSize, list.size() - 1);
    }

    public void removeView(int index, int type) {
        datasource.remove(index);
      switch (type) {
          case 21:
              addView("empty");
              addView("empty");
              break;
          case 14:
          case 22:
              addView("empty");
              addView("empty");
              addView("empty");
              addView("empty");
              break;
          default:
              addView("empty");
      }
        notifyItemRemoved(index);
    }

    public void move(int fromIndex,int toIndex){
        notifyItemMoved(fromIndex, toIndex);
        String temp = get(fromIndex);
        datasource.remove(fromIndex);
        datasource.add(toIndex, temp);
    }

    public String get(int index) {
        return datasource.get(index);
    }
}