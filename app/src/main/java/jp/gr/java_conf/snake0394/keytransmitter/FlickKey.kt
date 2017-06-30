package jp.gr.java_conf.snake0394.keytransmitter

import android.view.MotionEvent
import android.view.View

/**
 * 上下左右にフリックしたときにキー情報を送信するキーです
 */
class FlickKey
/**
 * @param columnSpan
 * *
 * @param rowSpan
 * *
 * @param description
 * *
 * @param type
 * *
 * @param adjust フリック距離
 */
(columnSpan: Int, rowSpan: Int, description: String, type: KeyType, val adjust: Int) : BaseKey(columnSpan, rowSpan, description, type) {

    val UP = "up"
    val DOWN = "down"
    val RIGHT = "right"
    val LEFT = "left"

    // 最後にタッチされた座標
    private var startTouchX: Float = 0.toFloat()
    private var startTouchY: Float = 0.toFloat()

    // 現在タッチ中の座標
    private var nowTouchedX: Float = 0.toFloat()
    private var nowTouchedY: Float = 0.toFloat()

    override fun onActionDown(view: View, motionEvent: MotionEvent) {
        startTouchX = motionEvent.x
        startTouchY = motionEvent.y
    }

    override fun onActionUp(view: View, motionEvent: MotionEvent) {
        nowTouchedX = motionEvent.x
        nowTouchedY = motionEvent.y

        //上
        if (startTouchY > nowTouchedY) {
            //左
            if (startTouchX > nowTouchedX) {
                if (startTouchY - nowTouchedY > startTouchX - nowTouchedX) {
                    if (startTouchY > nowTouchedY + adjust) {
                        //Log.v("Flick", "左上上");
                        // 上フリック時の処理を記述する
                        send(keyCodesMap[UP])
                    }
                } else if (startTouchY - nowTouchedY < startTouchX - nowTouchedX) {
                    if (startTouchX > nowTouchedX + adjust) {
                        //Log.v("Flick", "左上左");
                        // 左フリック時の処理を記述する
                        send(keyCodesMap[LEFT])
                    }
                }
                //右
            } else if (startTouchX < nowTouchedX) {
                if (startTouchY - nowTouchedY > nowTouchedX - startTouchX) {
                    if (startTouchY > nowTouchedY + adjust) {
                        //Log.v("Flick", "右上上");
                        // 上フリック時の処理を記述する
                        send(keyCodesMap[UP])
                    }
                } else if (startTouchY - nowTouchedY < nowTouchedX - startTouchX) {
                    if (startTouchX + adjust < nowTouchedX) {
                        //Log.v("Flick", "右上右");
                        // 右フリック時の処理を記述する
                        send(keyCodesMap[RIGHT])
                    }
                }
            }
            //下
        } else if (startTouchY < nowTouchedY) {
            //左
            if (startTouchX > nowTouchedX) {
                if (nowTouchedY - startTouchY > startTouchX - nowTouchedX) {
                    if (startTouchY + adjust < nowTouchedY) {
                        //Log.v("Flick", "左下下");
                        // 下フリック時の処理を記述する
                        send(keyCodesMap[DOWN])
                    }
                } else if (nowTouchedY - startTouchY < startTouchX - nowTouchedX) {
                    if (startTouchX > nowTouchedX + adjust) {
                        //Log.v("Flick", "左下左");
                        // 左フリック時の処理を記述する
                        send(keyCodesMap[LEFT])
                    }
                }
                //右
            } else if (startTouchX < nowTouchedX) {
                if (nowTouchedY - startTouchY > nowTouchedX - startTouchX) {
                    if (startTouchY + adjust < nowTouchedY) {
                        //Log.v("Flick", "右下下");
                        // 下フリック時の処理を記述する
                        send(keyCodesMap[DOWN])
                    }
                } else if (nowTouchedY - startTouchY < nowTouchedX - startTouchX) {
                    if (startTouchX + adjust < nowTouchedX) {
                        //Log.v("Flick", "右下右");
                        // 右フリック時の処理を記述する
                        send(keyCodesMap[RIGHT])
                    }
                }
            }
        }
    }

    companion object {
        private val serialVersionUID = -5499597086336673528L
    }
}
