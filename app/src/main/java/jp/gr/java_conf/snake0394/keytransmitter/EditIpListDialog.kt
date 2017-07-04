package jp.gr.java_conf.snake0394.keytransmitter

import android.app.Activity
import android.app.Dialog
import android.app.DialogFragment
import android.content.Context
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.widget.CheckBox
import android.widget.LinearLayout

class EditIpListDialog : DialogFragment() {

    private lateinit var listener: OnCheckFinishedListener

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(activity)

        val rootView = LayoutInflater.from(activity).inflate(R.layout.dialog_edit_ip_list, null)
        val layout = rootView.findViewById(R.id.layout_root) as LinearLayout
        arguments.getStringArray(ARG_NAME_LIST).forEach {
            val checkBox = CheckBox(activity)
            checkBox.text = it
            layout.addView(checkBox)
        }
        builder.setView(rootView)
                .setPositiveButton("削除", { _, _ ->
                    val checkedList = mutableListOf<String>()
                    for (i in 0..layout.childCount) {
                        try {
                            val checkBox = layout.getChildAt(i) as CheckBox
                            if (checkBox.isChecked) {
                                checkedList.add(checkBox.text.toString())
                            }
                        }catch (e:TypeCastException){
                            continue
                        }
                    }
                    listener.onCheckFinished(checkedList)
                })
                .setNegativeButton("キャンセル", null)

        return builder.create()
    }

    override fun onResume() {
        super.onResume()

        val dialog = dialog

        //AttributeからLayoutParamsを求める
        val layoutParams = dialog.window!!
                .attributes

        //display metricsでdpのもと(?)を作る
        val metrics = DisplayMetrics()
        activity.windowManager
                .defaultDisplay
                .getMetrics(metrics)

        //LayoutParamsにdpを計算して適用(今回は横幅300dp)(※metrics.scaledDensityの返り値はfloat)
        val dialogWidth = 300 * metrics.scaledDensity
        layoutParams.width = dialogWidth.toInt()

        //LayoutParamsをセットする
        dialog.window!!.attributes = layoutParams
    }


    override fun onPause() {
        super.onPause()
        dismiss()
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)

        try {
            this.listener = context as OnCheckFinishedListener
        } catch (e: ClassCastException) {
            throw ClassCastException(context!!.toString() + " must implement OnCheckFinishedListener")
        }

    }

    override fun onAttach(activity: Activity?) {
        super.onAttach(activity)
        try {
            this.listener = activity as OnCheckFinishedListener
        } catch (e: ClassCastException) {
            throw ClassCastException(activity!!.toString() + " must implement OnCheckFinishedListener")
        }
    }

    interface OnCheckFinishedListener {
        fun onCheckFinished(checkedList: List<String>)
    }

    companion object {
        fun show(activity: Activity, names: Array<String>) {
            EditIpListDialog().apply {
                arguments = Bundle().apply {
                    putStringArray(ARG_NAME_LIST, names)
                }
            }.show(activity.fragmentManager, "EditIpListDialog")
        }

        private val ARG_NAME_LIST = "nameList"
    }
}