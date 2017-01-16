package netpro.keytransmitter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.LinkedList;
import java.util.List;

public class KeyRecyclerViewAdapter extends RecyclerView.Adapter<KeyViewHolder> {
    private List<Key> datasource = new LinkedList<>();
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
        Key key = datasource.get(position);
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

    public Key get(int index) {
        return datasource.get(index);
    }

    public void setDatasource(List<Key> datasource) {
        this.datasource = datasource;
        notifyDataSetChanged();
    }
}