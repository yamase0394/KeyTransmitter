package netpro.keytransmitter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup

/**
 * EditActivityのRecyclerViewのアダプタ
 */
class EditKeyRecyclerViewAdapter : RecyclerView.Adapter<EditKeyViewHolder>() {
    var keyList: MutableList<Key> = mutableListOf()

    private lateinit var listener: OnRecyclerClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EditKeyViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.layout_cardview, parent, false)
        return EditKeyViewHolder(v)
    }

    override fun onBindViewHolder(view: EditKeyViewHolder, position: Int) {
        val key = keyList[position]
        view.setKey(key)
        view.setListener(listener)
    }

    override fun getItemCount(): Int {
        return keyList.size
    }

    fun addView(key: Key) {
        keyList.add(key)
        notifyItemInserted(itemCount - 1)
    }

    fun addView(index: Int, key: Key) {
        keyList.add(index, key)
        notifyItemInserted(index)
    }

    fun addAllView(list: List<Key>) {
        val preSize = itemCount
        keyList.addAll(list)
        notifyItemRangeInserted(preSize, list.size - 1)
    }

    fun removeView(index: Int, fillsEmpty: Boolean) {
        val target = keyList[index]
        keyList.removeAt(index)
        notifyItemRemoved(index)

        if (!fillsEmpty) {
            return
        }

        val keySize = target.columnSpan * target.rowSpan
        for (i in 0..keySize - 1) {
            addView(EmptyKey())
            notifyItemInserted(keyList.size - 1)
        }
    }

    fun move(fromIndex: Int, toIndex: Int) {
        notifyItemMoved(fromIndex, toIndex)
        val temp = keyList[fromIndex]
        keyList.removeAt(fromIndex)
        keyList.add(toIndex, temp)
    }

    operator fun get(index: Int): Key {
        return keyList[index]
    }

    fun setOnRecyclerClickListener(listener: OnRecyclerClickListener) {
        this.listener = listener
    }

    val emptySpace: Int
        get() {
            return keyList
                    .filterIsInstance<EmptyKey>()
                    .sumBy { it.columnSpan * it.rowSpan }
        }
}