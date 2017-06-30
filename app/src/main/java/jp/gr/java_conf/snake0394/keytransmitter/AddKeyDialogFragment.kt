package jp.gr.java_conf.snake0394.keytransmitter

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.support.design.widget.TextInputLayout
import android.support.v7.app.AlertDialog
import android.text.Editable
import android.text.TextWatcher
import android.util.DisplayMetrics
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import android.widget.*
import java.util.*

class AddKeyDialogFragment : android.support.v4.app.DialogFragment() {

    private lateinit var listener: OnKeyGeneratedListener
    private val keyCodeViewList = ArrayList<View>()
    private val rightKeyCodeViewList = ArrayList<View>()
    private val leftKeyCodeViewList = ArrayList<View>()
    private val flickUpKeyCodeViewList = ArrayList<View>()
    private val flickDownKeyCodeViewList = ArrayList<View>()
    private val flickRightKeyCodeViewList = ArrayList<View>()
    private val flickLeftKeyCodeViewList = ArrayList<View>()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val view = View.inflate(activity, R.layout.dialog_fragment_add_key, null)

        val intervalLayout = view.findViewById(R.id.intervalLayout) as TextInputLayout
        intervalLayout.visibility = GONE
        intervalLayout.isErrorEnabled = true
        intervalLayout.error = "必須"
        intervalLayout.editText!!.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
            }

            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
            }

            override fun afterTextChanged(editable: Editable) {
                try {
                    Integer.parseInt(editable.toString())
                } catch (e: NumberFormatException) {
                    intervalLayout.isErrorEnabled = true
                    intervalLayout.error = "必須"
                    return
                }

                intervalLayout.isErrorEnabled = false
            }
        })

        val adjustTextInputLayout = view.findViewById(R.id.text_input_layout_adjust) as TextInputLayout
        adjustTextInputLayout.visibility = GONE
        adjustTextInputLayout.isErrorEnabled = true
        adjustTextInputLayout.error = "必須"
        adjustTextInputLayout.editText!!.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
            }

            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
            }

            override fun afterTextChanged(editable: Editable) {
                try {
                    Integer.parseInt(editable.toString())
                } catch (e: NumberFormatException) {
                    adjustTextInputLayout.isErrorEnabled = true
                    adjustTextInputLayout.error = "必須"
                    return
                }

                adjustTextInputLayout.isErrorEnabled = false
            }
        })

        val descriptionTil = view.findViewById(R.id.descriptionTextInputLayout) as TextInputLayout
        descriptionTil.isErrorEnabled = true
        descriptionTil.error = "必須"
        descriptionTil.editText!!.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(editable: Editable) {
                val keyTypeSpinner = view.findViewById(R.id.keyTypeSpinner) as Spinner
                val str = keyTypeSpinner.selectedItem as String
                val type = KeyType.toType(str)
                if (type != KeyType.EMPTY && editable.isEmpty()) {
                    descriptionTil.isErrorEnabled = true
                    descriptionTil.error = "必須"
                } else {
                    descriptionTil.isErrorEnabled = false
                }
            }
        })

        val addKeyLayout = view.findViewById(R.id.addKeyLayout) as LinearLayout
        val addRightKeyLayout = view.findViewById(R.id.addRightKeyLayout) as LinearLayout
        val addLeftKeyLayout = view.findViewById(R.id.addLeftKeyLayout) as LinearLayout
        val addFlickUpKeyLayout = view.findViewById(R.id.addFlickUpKeyLayout) as LinearLayout
        val addFlickDownKeyLayout = view.findViewById(R.id.addFlickDownKeyLayout) as LinearLayout
        val addFlickRightKeyLayout = view.findViewById(R.id.addFlickRightKeyLayout) as LinearLayout
        val addFlickLeftKeyLayout = view.findViewById(R.id.addFlickLeftKeyLayout) as LinearLayout

        val columnCountSpinner = view.findViewById(R.id.columnCountSpinner) as Spinner
        val columnCounts = arrayOf(1, 2, 3, 4, 5)
        var adapter = ArrayAdapter(context, android.R.layout.simple_spinner_item, columnCounts)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        columnCountSpinner.adapter = adapter
        columnCountSpinner.setSelection(0)

        val rowCountSpinner = view.findViewById(R.id.rowCountSpinner) as Spinner
        val rowCounts = arrayOf(1, 2, 3, 4)
        adapter = ArrayAdapter(context, android.R.layout.simple_spinner_item, rowCounts)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        rowCountSpinner.adapter = adapter
        rowCountSpinner.setSelection(0)

        val keyTypeSpinner = view.findViewById(R.id.keyTypeSpinner) as Spinner
        val keyTypes = KeyType.values()
        val keyDescriptions = arrayOfNulls<String>(keyTypes.size)
        for (i in keyDescriptions.indices) {
            keyDescriptions[i] = keyTypes[i].description
        }

        val keyAdapter = ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, keyDescriptions)
        keyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        keyTypeSpinner.adapter = keyAdapter
        keyTypeSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(adapterView: AdapterView<*>, view: View, i: Int, l: Long) {
                val spinner = adapterView as Spinner
                val description = spinner.selectedItem as String
                val type = KeyType.toType(description)

                if (type == KeyType.EMPTY) {
                    //名前と説明を必須にしない
                    descriptionTil.isErrorEnabled = false;
                    //キーを追加できなくする
                    addKeyLayout.visibility = GONE
                } else {
                    if (descriptionTil.editText!!.text.isEmpty()) {
                        descriptionTil.isErrorEnabled = true
                        descriptionTil.error = "必須";
                    }

                    addKeyLayout.visibility = View.VISIBLE
                }

                if (type == KeyType.PRESSING) {
                    //キー送信間隔入力ボックスを表示
                    intervalLayout.visibility = View.VISIBLE
                } else {
                    intervalLayout.visibility = GONE
                }

                if (type == KeyType.KNOB) {
                    addKeyLayout.visibility = GONE
                    addRightKeyLayout.visibility = View.VISIBLE
                    addLeftKeyLayout.visibility = View.VISIBLE
                } else {
                    addRightKeyLayout.visibility = View.GONE
                    addLeftKeyLayout.visibility = View.GONE
                }

                if (type == KeyType.FLICK) {
                    addKeyLayout.visibility = GONE
                    addFlickUpKeyLayout.visibility = View.VISIBLE
                    addFlickDownKeyLayout.visibility = View.VISIBLE
                    addFlickRightKeyLayout.visibility = View.VISIBLE
                    addFlickLeftKeyLayout.visibility = View.VISIBLE
                    adjustTextInputLayout.visibility = View.VISIBLE
                } else {
                    addFlickUpKeyLayout.visibility = View.GONE
                    addFlickDownKeyLayout.visibility = View.GONE
                    addFlickRightKeyLayout.visibility = View.GONE
                    addFlickLeftKeyLayout.visibility = View.GONE
                    adjustTextInputLayout.visibility = GONE
                }
            }

            override fun onNothingSelected(adapterView: AdapterView<*>) {

            }
        }
        keyTypeSpinner.setSelection(0)

        val linearLayout = view.findViewById(R.id.keyCodesLayout) as LinearLayout
        val addButton = view.findViewById(R.id.addButton) as Button
        initKeyCodesLayout(linearLayout, addButton, keyCodeViewList)

        val rightKeyCodesLayout = view.findViewById(R.id.rotate_right_keyCodesLayout) as LinearLayout
        val addRightButton = view.findViewById(R.id.button_add_rotate_right_key) as Button
        initKeyCodesLayout(rightKeyCodesLayout, addRightButton, rightKeyCodeViewList)

        val leftKeyCodesLayout = view.findViewById(R.id.rotate_left_keyCodesLayout) as LinearLayout
        val addLeftButton = view.findViewById(R.id.button_add_rotate_left_key) as Button
        initKeyCodesLayout(leftKeyCodesLayout, addLeftButton, leftKeyCodeViewList)

        val flickUpKeyCodesLayout = view.findViewById(R.id.flick_up_keyCodesLayout) as LinearLayout
        val addFlickUpButton = view.findViewById(R.id.button_add_flick_up_key) as Button
        initKeyCodesLayout(flickUpKeyCodesLayout, addFlickUpButton, flickUpKeyCodeViewList)

        val flickDownKeyCodesLayout = view.findViewById(R.id.flick_down_keyCodesLayout) as LinearLayout
        val addFlickDownButton = view.findViewById(R.id.button_add_flick_down_key) as Button
        initKeyCodesLayout(flickDownKeyCodesLayout, addFlickDownButton, flickDownKeyCodeViewList)

        val flickRightKeyCodesLayout = view.findViewById(R.id.flick_right_keyCodesLayout) as LinearLayout
        val addFlickRightButton = view.findViewById(R.id.button_add_flick_raight_key) as Button
        initKeyCodesLayout(flickRightKeyCodesLayout, addFlickRightButton, flickRightKeyCodeViewList)

        val flickLeftKeyCodesLayout = view.findViewById(R.id.flick_left_keyCodesLayout) as LinearLayout
        val addFlickLeftButton = view.findViewById(R.id.button_add_flick_left_key) as Button
        initKeyCodesLayout(flickLeftKeyCodesLayout, addFlickLeftButton, flickLeftKeyCodeViewList)

        val createButton = view.findViewById(R.id.createButton) as Button
        createButton.setOnClickListener(View.OnClickListener {
            val editDescription = view.findViewById(R.id.description) as EditText
            val description = editDescription.text.toString()

            val columnCount = columnCountSpinner.selectedItem as Int
            val rowCount = rowCountSpinner.selectedItem as Int
            if (columnCount * rowCount > arguments.getInt("emptySpace")) {
                val shortage = columnCount * rowCount - arguments.getInt("emptySpace")
                showErrorDialog("スペースが " + shortage + "足りません")
                return@OnClickListener
            }

            val type = KeyType.toType(keyTypeSpinner.selectedItem as String)
            var key: BaseKey = EmptyKey()
            when (type) {
                KeyType.RELEASED -> {
                    key = NormalKey(columnCount, rowCount, description, type)
                    key.keyCodesMap.put(key.NORMAL, toKeyStrList(keyCodeViewList))
                }
                KeyType.LONG_PRESS -> {
                    key = LongPressKey(columnCount, rowCount, description, type)
                    key.keyCodesMap.put(key.NORMAL, toKeyStrList(keyCodeViewList))
                }
                KeyType.PRESSING -> {
                    val editInterval = view.findViewById(R.id.interval) as EditText
                    val interval: Long
                    try {
                        interval = editInterval.text.toString().toLong()
                    } catch (e: NumberFormatException) {
                        showErrorDialog("キー送信間隔を入力してください")
                        return@OnClickListener
                    }

                    key = PressingKey(columnCount, rowCount, description, type, interval)
                    key.keyCodesMap.put(key.NORMAL, toKeyStrList(keyCodeViewList))
                }
                KeyType.KNOB -> {
                    key = ControlKnob(columnCount, rowCount, description, type)
                    key.keyCodesMap.put(key.RIGHT, toKeyStrList(rightKeyCodeViewList))
                    key.keyCodesMap.put(key.LEFT, toKeyStrList(leftKeyCodeViewList))
                    /*
                        if (rightKeyStrList.isEmpty() || leftKeyStrList.isEmpty()) {
                            showErrorDialog("少なくとも1つの入力キーが必要です");
                            return;
                        }
                        */
                }
                KeyType.FLICK -> {
                    val adjustEditText = view.findViewById(R.id.edit_text_adjust) as EditText
                    val adjust: Int
                    try {
                        adjust = Integer.parseInt(adjustEditText.text.toString())
                    } catch (e: NumberFormatException) {
                        showErrorDialog("フリック距離を入力してください")
                        return@OnClickListener
                    }

                    key = FlickKey(columnCount, rowCount, description, type, adjust)
                    key.keyCodesMap.put(key.UP, toKeyStrList(flickUpKeyCodeViewList))
                    key.keyCodesMap.put(key.DOWN, toKeyStrList(flickDownKeyCodeViewList))
                    key.keyCodesMap.put(key.RIGHT, toKeyStrList(flickRightKeyCodeViewList))
                    key.keyCodesMap.put(key.LEFT, toKeyStrList(flickLeftKeyCodeViewList))
                    /*
                        if (rightKeyStrList.isEmpty() || leftKeyStrList.isEmpty()) {
                            showErrorDialog("少なくとも1つの入力キーが必要です");
                            return;
                        }
                        */
                }
                KeyType.EMPTY -> key = EmptyKey(columnCount, rowCount, description, type)
            }

            if (key !is EmptyKey && description.isNullOrEmpty()) {
                showErrorDialog("入力が不完全です");
                return@OnClickListener
            }

            listener.onKeyGenerated(key)
            dismiss()
        })

        val cancelButton = view.findViewById(R.id.cancelButton) as Button
        cancelButton.setOnClickListener { dismiss() }

        val builder = AlertDialog.Builder(activity)
        builder.setTitle("キー生成").setView(view)
        return builder.create()
    }

    private fun showErrorDialog(message: String) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("エラー").setMessage(message).setPositiveButton("確認", null).show()
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

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is OnKeyGeneratedListener) {
            listener = context
        } else {
            throw RuntimeException(context!!.toString() + " must implement OnKeyGeneratedListener")
        }
    }

    interface OnKeyGeneratedListener {
        fun onKeyGenerated(abstractKey: BaseKey)
    }

    private fun initKeyCodesLayout(keyCodesLayout: LinearLayout, addKeyCodeButton: Button, keyCodeViewList: MutableList<View>) {
        addKeyCodeButton.setOnClickListener {
            //Spinnerに入れるキーコードの種類を選択させるダイアログを生成
            val builder = AlertDialog.Builder(context)
            val items = arrayOf("アルファベット", "数字", "制御キー", "ファンクションキー", "特殊")
            builder.setItems(items) { _, which ->
                val addKeyCodeView = View.inflate(activity, R.layout.layout_add_key_code, null)

                val spinner = addKeyCodeView.findViewById(R.id.keyCodeSpinner) as Spinner
                val adapter = ArrayAdapter<String>(context, android.R.layout.simple_spinner_item)
                //選択結果に応じてSpinnerの選択肢を変える
                when (which) {
                //アルファベット
                    0 -> adapter.addAll(resources.getStringArray(R.array.alphabet).asList())
                //数字
                    1 -> adapter.addAll(resources.getStringArray(R.array.number).asList())
                //制御キー
                    2 -> adapter.addAll(resources.getStringArray(R.array.control_key).asList())
                //ファンクションキー
                    3 -> adapter.addAll(resources.getStringArray(R.array.function_key).asList())
                //特殊
                    4 -> adapter.addAll(resources.getStringArray(R.array.special_key).asList())
                //記号
                //5 -> adapter.addAll(resources.getStringArray(R.array.symbol).asList())
                }
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spinner.adapter = adapter
                spinner.setSelection(0)

                val removeButton = addKeyCodeView.findViewById(R.id.removeButton) as Button
                removeButton.setOnClickListener { view ->
                    val parent = view.parent as ViewGroup
                    keyCodeViewList.remove(parent)
                    parent.removeAllViews()
                }
                keyCodesLayout.addView(addKeyCodeView)
                keyCodeViewList.add(addKeyCodeView)
            }
            builder.show()
        }
    }

    private fun toKeyStrList(keyViewList: List<View>): List<String> {
        return keyViewList
                .map { it.findViewById(R.id.keyCodeSpinner) as Spinner }
                .map { it.selectedItem as String }
    }

    companion object {
        fun newInstance(emptySpace: Int): AddKeyDialogFragment {
            val fragment = AddKeyDialogFragment()
            val args = Bundle()
            args.putInt("emptySpace", emptySpace)
            fragment.arguments = args
            return fragment
        }
    }
}
