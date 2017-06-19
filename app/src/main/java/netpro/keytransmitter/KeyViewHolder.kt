package netpro.keytransmitter

import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.ImageView
import android.widget.TextView

/**
 * KeyRecyclerViewAdapterのViewHolder
 */
class KeyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnTouchListener, View.OnLongClickListener {
    private val description: TextView
    private val type: TextView
    private val keyName: TextView
    private var view: View? = null
    private var key: Key? = null
    private val borderType: View
    private val knobImageView: ImageView


    init {
        val cardView = itemView.findViewById(R.id.cardView) as CardView
        cardView.setOnTouchListener(this)
        cardView.setOnLongClickListener(this)
        description = itemView.findViewById(R.id.description) as TextView
        view = itemView.findViewById(R.id.border)
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
            view!!.visibility = View.INVISIBLE
            borderType.visibility = View.INVISIBLE
            keyName.visibility = View.INVISIBLE
            type.visibility = View.INVISIBLE
            return
        }

        description.text = key.description
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

    }

    override fun onTouch(view: View, motionEvent: MotionEvent): Boolean {
        when (motionEvent.action) {
            MotionEvent.ACTION_DOWN -> key!!.onActionDown(view, motionEvent)
            MotionEvent.ACTION_CANCEL -> {
                Log.d("keyViewHolder", "cancel")
                key!!.onCancel()
            }
            MotionEvent.ACTION_UP -> {
                Log.d("keyViewHolder", "up")
                key!!.onActionUp(view, motionEvent)
            }
            MotionEvent.ACTION_MOVE -> {
                Log.d("keyViewHolder", "move")
                key!!.onMove(view, motionEvent)
            }
        }
        return false
    }

    override fun onLongClick(view: View): Boolean {
        key!!.onLongClick()
        return false
    }
}