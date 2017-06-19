package netpro.keytransmitter

import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.TextView

/**
 * KeyRecyclerViewAdapterのViewHolder
 */
class KeyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnTouchListener, View.OnLongClickListener {
    private val description: TextView
    private var key: Key? = null

    init {
        val cardView = itemView.findViewById(R.id.cardView) as CardView
        cardView.setOnTouchListener(this)
        cardView.setOnLongClickListener(this)
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