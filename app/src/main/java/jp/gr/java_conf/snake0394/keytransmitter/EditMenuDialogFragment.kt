package jp.gr.java_conf.snake0394.keytransmitter

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.util.DisplayMetrics
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView

class EditMenuDialogFragment : android.support.v4.app.DialogFragment() {

    private lateinit var listener: OnListItemClickListener

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val view = View.inflate(activity, R.layout.dialog_fragment_edit_menu, null)
        val arrayAdapter = ArrayAdapter(activity, android.R.layout.simple_list_item_1, arrayOf("編集", "削除"))
        val listView = view.findViewById(R.id.listView) as ListView
        listView.adapter = arrayAdapter
        listView.onItemClickListener = AdapterView.OnItemClickListener { _, _, i, _ ->
            listener.onListItemClicked(arguments.getInt(ARG_POSITION), listView.getItemAtPosition(i) as String)
            dismiss()
        }

        val builder = AlertDialog.Builder(activity)
        builder.setMessage(arguments.getString(ARG_MESSAGE)).setView(view)
        return builder.create()
    }

    override fun onResume() {
        super.onResume()

        val dialog = dialog
        val layoutParams = dialog.window!!.attributes
        //display metricsでdpのもと(?)を作る
        val metrics = DisplayMetrics()
        activity.windowManager.defaultDisplay.getMetrics(metrics)
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

    interface OnListItemClickListener {
        fun onListItemClicked(position: Int, selectedStr: String)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnListItemClickListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnListItemClickListener")
        }
    }

    companion object {
        private val ARG_POSITION = "position"
        private val ARG_MESSAGE = "message"

        fun newInstance(position: Int, message: String): EditMenuDialogFragment {
            val fragment = EditMenuDialogFragment()
            val args = Bundle()
            args.putInt(ARG_POSITION, position)
            args.putString(ARG_MESSAGE, message)
            fragment.arguments = args
            return fragment
        }
    }
}
