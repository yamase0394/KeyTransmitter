package jp.gr.java_conf.snake0394.keytransmitter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import netpro.keytransmitter.R

/**
 * EditActivityのRecyclerViewのアダプタ
 */
class EditKeyRecyclerViewAdapter : RecyclerView.Adapter<EditKeyViewHolder>() {
    var keyList: MutableList<BaseKey> = mutableListOf()

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

    fun addView(abstractKey: BaseKey) {
        keyList.add(abstractKey)
        notifyItemInserted(itemCount - 1)
    }

    fun addView(index: Int, key: BaseKey) {
        if(index >= keyList.size){
            keyList.add(key)
            notifyItemInserted(keyList.size - 1)
        } else {
            keyList.add(index, key)
            notifyItemInserted(index)
        }
    }

    fun addAllView(list: List<BaseKey>) {
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

    operator fun get(index: Int): BaseKey {
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