package netpro.keytransmitter

import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView

/**
 * EditRecyclerViewAdapterのViewHolder
 */
class EditKeyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val description: TextView
    private var key: Key? = null
    private var listener: OnRecyclerClickListener? = null

    init {
        val cardView = itemView.findViewById(R.id.cardView) as CardView
        cardView.setOnClickListener {
            listener?.onClickListener(adapterPosition, key)
        }
        description = itemView.findViewById(R.id.description) as TextView
    }

    fun setKey(key: Key) {
        this.key = key

        if (key is EmptyKey) {
            description.visibility = View.INVISIBLE
            return
        }

        description.text = key.description

        description.visibility = View.VISIBLE
    }

    fun setListener(listener: OnRecyclerClickListener) {
        this.listener = listener
    }

}