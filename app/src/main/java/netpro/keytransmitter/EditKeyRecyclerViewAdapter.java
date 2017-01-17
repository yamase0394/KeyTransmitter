package netpro.keytransmitter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.LinkedList;
import java.util.List;

/**
 * EditActivityのRecyclerViewのアダプタ
 */
public class EditKeyRecyclerViewAdapter extends RecyclerView.Adapter<EditKeyViewHolder> {
    private List<Key> datasource = new LinkedList<>();
    private OnRecyclerClickListener listener;

    public EditKeyRecyclerViewAdapter(){
    }

    @Override
    public EditKeyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_cardview, parent, false);
        return new EditKeyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(EditKeyViewHolder view, int position) {
        Key key = datasource.get(position);
        view.setKey(key);
        view.setListener(listener);
    }

    @Override
    public int getItemCount() {
        return datasource.size();
    }

    public void addView(Key key) {
        datasource.add(key);
        notifyItemInserted(getItemCount() - 1);
    }

    public void addView(int index, Key key) {
        datasource.add(index, key);
        notifyItemInserted(index);
    }

    public void addAllView(List<Key> list) {
        int preSize = getItemCount();
        datasource.addAll(list);
        notifyItemRangeInserted(preSize, list.size() - 1);
    }

    public void removeView(int index, boolean fillsEmpty) {
        Key target = datasource.get(index);
        datasource.remove(index);
        notifyItemRemoved(index);

        if (!fillsEmpty) {
            return;
        }

        int keySize = target.getColumnSpan() * target.getRowSpan();
        for (int i = 0; i < keySize; i++) {
            addView(new EmptyKey());
            notifyItemInserted(datasource.size() - 1);
        }
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

    public void setDatasource(List<Key> datasource) {
        this.datasource = datasource;
        notifyDataSetChanged();
    }

    public void setOnRecyclerClickListener(OnRecyclerClickListener listener) {
        this.listener = listener;
    }

    public int getEmptySpace() {
        int emptySpace = 0;
        for (Key key : datasource) {
            if (key instanceof EmptyKey) {
                emptySpace += key.getColumnSpan() * key.getRowSpan();
            }
        }
        return emptySpace;
    }
}