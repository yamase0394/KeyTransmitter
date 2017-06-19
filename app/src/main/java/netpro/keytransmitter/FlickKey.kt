package netpro.keytransmitter

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
(columnSpan: Int, rowSpan: Int, description: String, type: Key.Type, adjust: Int) : Key(columnSpan, rowSpan, description, type) {

    // 最後にタッチされた座標
    private var startTouchX: Float = 0.toFloat()
    private var startTouchY: Float = 0.toFloat()

    // 現在タッチ中の座標
    private var nowTouchedX: Float = 0.toFloat()
    private var nowTouchedY: Float = 0.toFloat()

    //それぞれの方向へフリックしたときに送信するキー情報
    var flickUpKeyStrList: List<String> = mutableListOf()
    var flickDownKeyStrList: List<String> = mutableListOf()
    var flickRightKeyStrList: List<String> = mutableListOf()
    var flickLeftKeyStrList: List<String> = mutableListOf()

    //フリック距離
    val adjust:Int

    init {
        this.adjust = adjust
    }

    override fun onActionDown(view: View, event: MotionEvent) {
        startTouchX = event.x
        startTouchY = event.y
    }

    override fun onActionUp(view: View, event: MotionEvent) {
        nowTouchedX = event.x
        nowTouchedY = event.y

        //上
        if (startTouchY > nowTouchedY) {
            //左
            if (startTouchX > nowTouchedX) {
                if (startTouchY - nowTouchedY > startTouchX - nowTouchedX) {
                    if (startTouchY > nowTouchedY + adjust) {
                        //Log.v("Flick", "左上上");
                        // 上フリック時の処理を記述する
                        send(flickUpKeyStrList!!)
                    }
                } else if (startTouchY - nowTouchedY < startTouchX - nowTouchedX) {
                    if (startTouchX > nowTouchedX + adjust) {
                        //Log.v("Flick", "左上左");
                        // 左フリック時の処理を記述する
                        send(flickLeftKeyStrList!!)
                    }
                }
                //右
            } else if (startTouchX < nowTouchedX) {
                if (startTouchY - nowTouchedY > nowTouchedX - startTouchX) {
                    if (startTouchY > nowTouchedY + adjust) {
                        //Log.v("Flick", "右上上");
                        // 上フリック時の処理を記述する
                        send(flickUpKeyStrList!!)
                    }
                } else if (startTouchY - nowTouchedY < nowTouchedX - startTouchX) {
                    if (startTouchX + adjust < nowTouchedX) {
                        //Log.v("Flick", "右上右");
                        // 右フリック時の処理を記述する
                        send(flickRightKeyStrList!!)
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
                        send(flickDownKeyStrList!!)
                    }
                } else if (nowTouchedY - startTouchY < startTouchX - nowTouchedX) {
                    if (startTouchX > nowTouchedX + adjust) {
                        //Log.v("Flick", "左下左");
                        // 左フリック時の処理を記述する
                        send(flickLeftKeyStrList!!)
                    }
                }
                //右
            } else if (startTouchX < nowTouchedX) {
                if (nowTouchedY - startTouchY > nowTouchedX - startTouchX) {
                    if (startTouchY + adjust < nowTouchedY) {
                        //Log.v("Flick", "右下下");
                        // 下フリック時の処理を記述する
                        send(flickDownKeyStrList!!)
                    }
                } else if (nowTouchedY - startTouchY < nowTouchedX - startTouchX) {
                    if (startTouchX + adjust < nowTouchedX) {
                        //Log.v("Flick", "右下右");
                        // 右フリック時の処理を記述する
                        send(flickRightKeyStrList!!)
                    }
                }
            }
        }
    }

    override fun addKeyCode(keyCode: String) {}

    companion object {
        private val serialVersionUID = -5499597086336673528L
    }
}
