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
    private lateinit var abstractKey: BaseKey
    private lateinit var listener: OnRecyclerClickListener

    init {
        val cardView = itemView.findViewById(R.id.cardView) as CardView
        cardView.setOnClickListener {
            listener.onClickListener(adapterPosition, abstractKey)
        }
        description = itemView.findViewById(R.id.description) as TextView
    }

    fun setKey(abstractKey: BaseKey) {
        this.abstractKey = abstractKey

        if (abstractKey is EmptyKey) {
            description.visibility = View.INVISIBLE
        } else {
            description.text = abstractKey.description
            description.visibility = View.VISIBLE
        }
    }

    fun setListener(listener: OnRecyclerClickListener) {
        this.listener = listener
    }

}