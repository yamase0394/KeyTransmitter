package netpro.keyTransmitter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

public class KeyRecyclerViewAdapter extends RecyclerView.Adapter<KeyViewHolder> implements Serializable {
    private static final long serialVersionUID = 9051227485244867118L;
    private List<Key> datasource = new LinkedList<>();
    private int displayWidth = 720;
    //private int width = 768;
    private Context context;


    public KeyRecyclerViewAdapter(Context context) {
        this.context = context;
    }

    @Override
    public KeyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_cardview, parent, false);
        return new KeyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(KeyViewHolder view, int position) {
        //Keyのサイズを画面のサイズに合わせて変更
        Key key = datasource.get(position);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.width = (displayWidth - displayWidth / 8) / 4 * key.getColumnSpan();
        params.height = (displayWidth - displayWidth / 8) / 4 * key.getRowSpan();

        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        if (key.getColumnSpan() > 1) {
            params.width += (key.getColumnSpan() - 1) * (10 * metrics.density);
        }
        if (key.getRowSpan() > 1) {
            params.height += (key.getRowSpan() - 1) * (10 * metrics.density);
        }

        view.itemView.setLayoutParams(params);
        view.setText((datasource.get(position)).getName(), datasource.get(position).getDescription());
        view.setKey(key);
    }

    @Override
    public int getItemCount() {
        return datasource.size();
    }

    public void addView(Key key) {
        datasource.add(key);
        notifyItemInserted(getItemCount() - 1);
    }

    public void addAllView(List<Key> list) {
        int preSize = getItemCount();
        datasource.addAll(list);
        notifyItemRangeInserted(preSize, list.size() - 1);
    }

    public void removeView(int index) {
        Key target = datasource.get(index);
        int keySize = target.getColumnSpan() * target.getRowSpan();
        datasource.remove(index);
        notifyItemRemoved(index);
        for (int i = 0; i < keySize; i++) {
            addView(new EmptyKey());
            notifyItemInserted(datasource.size() - 1);
        }
        Log.d("remove", String.valueOf(index));
    }

    public void move(int fromIndex, int toIndex) {
        notifyItemMoved(fromIndex, toIndex);
        Key temp = datasource.get(fromIndex);
        datasource.remove(fromIndex);
        datasource.add(toIndex, temp);
    }

    public Key get(int index) {
        return datasource.get(index);
    }

    public List<Key> getDatasource() {
        return datasource;
    }

    public String getName() {
        return "keyboard.txt";
    }

}