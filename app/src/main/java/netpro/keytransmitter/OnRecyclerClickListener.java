package netpro.keytransmitter;

/**
 * EditActivityで子Viewのタッチを検知するためのリスナ
 */
public interface OnRecyclerClickListener {
    void onClickListener(int position, Key key);
}