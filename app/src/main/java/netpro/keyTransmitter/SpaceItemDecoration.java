package netpro.keyTransmitter;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * RecyclerViewの子View間のスペースを設定するクラスです
 */
public class SpaceItemDecoration extends RecyclerView.ItemDecoration {

    private final int topSpace;
    private final int bottomSpace;
    private final int rightSpace;
    private final int leftSpace;

    public SpaceItemDecoration(int topSpace, int spaceBottom, int spaceRight, int leftSpace) {
        this.topSpace = topSpace;
        this.bottomSpace = spaceBottom;
        this.rightSpace = spaceRight;
        this.leftSpace = leftSpace;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.set(leftSpace, topSpace, rightSpace, bottomSpace);
    }
}