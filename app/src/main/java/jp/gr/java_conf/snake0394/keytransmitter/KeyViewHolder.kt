package jp.gr.java_conf.snake0394.keytransmitter

import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.MotionEvent
import android.view.View
import android.widget.TextView

/**
 * KeyRecyclerViewAdapterのViewHolder
 */
class KeyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnTouchListener, View.OnLongClickListener {
    private val description: TextView
    private lateinit var key: BaseKey

    init {
        val cardView = itemView.findViewById(R.id.cardView) as CardView
        cardView.setOnTouchListener(this)
        cardView.setOnLongClickListener(this)
        description = itemView.findViewById(R.id.description) as TextView
    }

    fun setKey(key: BaseKey) {
        this.key = key

        if (key is EmptyKey) {
            description.visibility = View.INVISIBLE
        } else {
            description.text = key.description
            description.visibility = View.VISIBLE
        }
    }

    override fun onTouch(view: View, motionEvent: MotionEvent): Boolean {
        when (motionEvent.action) {
            MotionEvent.ACTION_DOWN -> key.onActionDown(view, motionEvent)
            MotionEvent.ACTION_CANCEL -> key.onCancel()
            MotionEvent.ACTION_UP -> key.onActionUp(view, motionEvent)
            MotionEvent.ACTION_MOVE -> key.onMove(view, motionEvent)
        }
        return false
    }

    override fun onLongClick(view: View): Boolean {
        key.onLongClick()
        return false
    }
}