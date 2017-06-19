package netpro.keytransmitter

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
    private val type: TextView
    private val keyName: TextView
    private val view: View
    private var key: Key? = null
    private val borderType: View
    private val knobImageView: ImageView
    private var listener: OnRecyclerClickListener? = null

    init {
        val cardView = itemView.findViewById(R.id.cardView) as CardView
        cardView.setOnClickListener {
            listener?.onClickListener(adapterPosition, key)
        }
        description = itemView.findViewById(R.id.description) as TextView
        view = itemView.findViewById(R.id.border)
        borderType = itemView.findViewById(R.id.border_type)
        keyName = itemView.findViewById(R.id.key_name) as TextView
        type = itemView.findViewById(R.id.type) as TextView
        knobImageView = itemView.findViewById(R.id.image_view_knob) as ImageView
    }

    fun setKey(key: Key) {
        this.key = key

        if (key is ControlKnob) {
            knobImageView.visibility = View.VISIBLE
        } else {
            knobImageView.visibility = View.GONE
        }

        if (key is EmptyKey) {
            description.visibility = View.INVISIBLE
            view.visibility = View.INVISIBLE
            borderType.visibility = View.INVISIBLE
            keyName.visibility = View.INVISIBLE
            type.visibility = View.INVISIBLE
            return
        }

        type.text = key.type.description
        var keyNames = ""
        for (str in key.keyCodeList) {
            if (keyNames.length == 0) {
                keyNames += str
                continue
            }
            keyNames += " + " + str
        }
        keyName.text = keyNames

        description.text = key.description

        description.visibility = View.VISIBLE
        view.visibility = View.VISIBLE
    }

    fun setListener(listener: OnRecyclerClickListener) {
        this.listener = listener
    }

}