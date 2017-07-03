package jp.gr.java_conf.snake0394.keytransmitter

import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView

/**
 * EditRecyclerViewAdapterのViewHolder
 */
class EditKeyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val description: TextView
    private lateinit var abstractKey: BaseKey
    private lateinit var listener: OnRecyclerClickListener
    private val knobView : ImageView

    init {
        val cardView = itemView.findViewById(R.id.cardView) as CardView
        cardView.setOnClickListener {
            listener.onClickListener(adapterPosition, abstractKey)
        }
        description = itemView.findViewById(R.id.description) as TextView
        knobView = itemView.findViewById(R.id.image_knob) as ImageView
    }

    fun setKey(key: BaseKey) {
        this.abstractKey = key

        if (key is ControlKnob) {
            knobView.visibility = View.VISIBLE
        } else {
            knobView.visibility = View.GONE
        }

        if (key is EmptyKey) {
            description.visibility = View.INVISIBLE
        } else {
            description.text = key.description
            description.visibility = View.VISIBLE
        }
    }

    fun setListener(listener: OnRecyclerClickListener) {
        this.listener = listener
    }

}