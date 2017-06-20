package netpro.keytransmitter

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

    private var listener: OnListItemClickListener? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val view = View.inflate(activity, R.layout.dialog_fragment_edit_menu, null)
        val items = arrayOf("編集", "削除")
        val arrayAdapter = ArrayAdapter(activity, android.R.layout.simple_list_item_1, items)
        val listView = view.findViewById(R.id.listView) as ListView
        listView.adapter = arrayAdapter
        listView.onItemClickListener = AdapterView.OnItemClickListener { _, _, i, _ ->
            listener!!.onListItemClicked(arguments.getInt("position"), listView.getItemAtPosition(i) as String)
            dismiss()
        }

        val builder = AlertDialog.Builder(activity)
        builder.setMessage(arguments.getString("message")).setView(view)
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

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is OnListItemClickListener) {
            listener = context as OnListItemClickListener?
        } else {
            throw RuntimeException(context!!.toString() + " must implement OnListItemClickListener")
        }
    }

    companion object {
        fun newInstance(position: Int, message: String): EditMenuDialogFragment {
            val fragment = EditMenuDialogFragment()
            val args = Bundle()
            args.putInt("position", position)
            args.putString("message", message)
            fragment.arguments = args
            return fragment
        }
    }
}
