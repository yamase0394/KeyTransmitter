package jp.gr.java_conf.snake0394.keytransmitter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import java.util.*

/**
 * MainActivityのRecyclerViewの
 */
class KeyRecyclerViewAdapter : RecyclerView.Adapter<KeyViewHolder>() {
    var keyList: MutableList<BaseKey> = LinkedList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): KeyViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.layout_cardview, parent, false)
        return KeyViewHolder(v)
    }

    override fun onBindViewHolder(view: KeyViewHolder, position: Int) {
        val key = keyList[position]
        view.setKey(key)
    }

    override fun getItemCount(): Int {
        return keyList.size
    }

    fun addView(abstractKey: BaseKey) {
        keyList.add(abstractKey)
        notifyItemInserted(itemCount - 1)
    }

    fun addAllView(list: List<BaseKey>) {
        val preSize = itemCount
        keyList.addAll(list)
        notifyItemRangeInserted(preSize, list.size - 1)
    }

    operator fun get(index: Int): BaseKey {
        return keyList[index]
    }
}