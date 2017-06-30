package jp.gr.java_conf.snake0394.keytransmitter;

/**
 * EditActivityで子Viewのタッチを検知するためのリスナ
 */
public interface OnRecyclerClickListener {
    void onClickListener(int position, BaseKey key);
}