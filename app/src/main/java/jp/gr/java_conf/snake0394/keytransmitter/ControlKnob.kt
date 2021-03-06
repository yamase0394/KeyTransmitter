package jp.gr.java_conf.snake0394.keytransmitter

import android.view.MotionEvent
import android.view.View
import android.widget.ImageView

/**
 * 左右それぞれの方向への回転時にキーを送信します
 */
class ControlKnob
/**
 * @param columnSpan
 * *
 * @param rowSpan
 * *
 * @param description
 * *
 * @param type
 */
(columnSpan: Int, rowSpan: Int, description: String, type: KeyType) : BaseKey(columnSpan, rowSpan, description, type) {

    val LEFT = "left"
    val RIGHT = "right"

    //前のタッチ座標
    private var preX: Float = 0.toFloat()
    private var preY: Float = 0.toFloat()
    //MotionEventから得られるx,y座標が入れ替わることによりつまみが揺れることを抑える
    private var preGapIsPositive: Boolean = false
    private var preGapIsNegative: Boolean = false

    override fun onActionDown(view: View, motionEvent: MotionEvent) {
        preX = motionEvent.x
        preY = motionEvent.y
    }

    override fun onMove(view: View, motionEvent: MotionEvent) {
        val a = preX - view.width / 2
        val b = preY - view.height / 2
        val c = motionEvent.x - view.width / 2
        val d = motionEvent.y - view.height / 2
        preX = motionEvent.x
        preY = motionEvent.y
        //前のタッチ位置とviewの中心とのラジアン
        val atanA = Math.atan2(b.toDouble(), a.toDouble())
        //現在のタッチ位置とviewの中心とのラジアン
        val atanB = Math.atan2(d.toDouble(), c.toDouble())
        var degreeA = Math.toDegrees(atanA)
        if (degreeA == 0.0 || degreeA == 360.0) {
            degreeA %= 360.0
        } else if (degreeA < 0) {
            degreeA += 360.0
        }
        var degreeB = Math.toDegrees(atanB)
        if (degreeB == 0.0 || degreeB == 360.0) {
            degreeB %= 360.0
        } else if (degreeB < 0) {
            degreeB += 360.0
        }

        val gap = (degreeB - degreeA).toInt()
        //差が大きすぎる場合は異常値とみなす
        if (Math.abs(gap) > 30) {
            return
        }
        if (gap > 0) {
            if (!preGapIsPositive) {
                preGapIsPositive = true
                preGapIsNegative = false
                return
            }
            send(keyCodesMap[RIGHT])
        } else if (gap == 0) {
            return
        } else {
            if (!preGapIsNegative) {
                preGapIsPositive = false
                preGapIsNegative = true
                return
            }
            send(keyCodesMap[LEFT])
        }

        val knobImageView = view.findViewById(R.id.image_knob) as ImageView
        knobImageView.rotation = knobImageView.rotation + gap
    }

    companion object {
        private val serialVersionUID = -3872636246983991948L
    }

}
