package jp.gr.java_conf.snake0394.keytransmitter

import android.graphics.Rect
import android.support.v7.widget.RecyclerView
import android.view.View

/**
 * RecyclerViewの子View間のスペースを設定するクラス
 */
class SpaceItemDecoration(private val topSpace: Int, private val bottomSpace: Int, private val rightSpace: Int, private val leftSpace: Int) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State?) {
        outRect.set(leftSpace, topSpace, rightSpace, bottomSpace)
    }
}