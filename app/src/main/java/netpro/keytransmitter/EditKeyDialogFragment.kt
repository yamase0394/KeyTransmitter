package netpro.keytransmitter

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

class EditKeyDialogFragment : android.support.v4.app.DialogFragment() {

    val alphabetList = ArrayList<String>()

    init {
        val ALPHABET_SIZE = 'Z' - 'A'
        var alphabet = 'A'
        for (i in 0..ALPHABET_SIZE) {
            alphabetList.add(alphabet++.toString())
        }
    }

    val numberList = ArrayList<String>()

    init {
        for (i in 0..9) {
            numberList.add(i.toString())
        }
    }

    val controlKeyList = Arrays.asList("Backspace", "Enter", "Shift", "Ctrl", "Alt", "Pause", "Space", "PageUp", "PageDown", "End", "Home", "←", "↑", "→", "↓", "PrintScreen", "Insert", "Delete", "Win", "NumLock", "ScrollLock", "Esc", "Tab")
    val functionKeyList = (1..12).mapTo(ArrayList<String>()) { "F" + it.toString() }
    //val symbolList = Arrays.asList(":", ";", "+", ",", "-", "=", ".", "/", "@", "[", "\\", "]", "^", "_")
    val specialList = Arrays.asList("VolUp", "VolDown", "VolMute")

    private var listener: OnKeyUpdatedListener? = null
    private val keyCodeViewList = ArrayList<View>()
    private val rightKeyCodeViewList = ArrayList<View>()
    private val leftKeyCodeViewList = ArrayList<View>()
    private val flickUpKeyCodeViewList = ArrayList<View>()
    private val flickDownKeyCodeViewList = ArrayList<View>()
    private val flickRightKeyCodeViewList = ArrayList<View>()
    private val flickLeftKeyCodeViewList = ArrayList<View>()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val view = View.inflate(activity, R.layout.dialog_fragment_edit_key, null)
        val key = arguments.getSerializable("key") as Key

        val intervalLayout = view.findViewById(R.id.intervalLayout) as TextInputLayout
        intervalLayout.isErrorEnabled = true
        intervalLayout.error = "必須"
        if (key is PressingKey) {
            intervalLayout.editText!!.setText(key.inputInterval.toString())
            intervalLayout.isErrorEnabled = false
        } else {
            intervalLayout.visibility = GONE
        }
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
        adjustTextInputLayout.isErrorEnabled = true
        adjustTextInputLayout.error = "必須"
        if (key is FlickKey) {
            adjustTextInputLayout.editText!!.setText(key.adjust.toString())
            adjustTextInputLayout.isErrorEnabled = false
        } else {
            adjustTextInputLayout.visibility = GONE
        }
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
        descriptionTil.editText!!.setText(key.description)
        descriptionTil.editText!!.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(editable: Editable) {
                val keyTypeSpinner = view.findViewById(R.id.keyTypeSpinner) as Spinner
                val str = keyTypeSpinner.selectedItem as String
                val type = Key.Type.toType(str)
                if (type != Key.Type.EMPTY && editable.length == 0) {
                    descriptionTil.isErrorEnabled = true
                    descriptionTil.error = "必須"
                } else {
                    descriptionTil.isErrorEnabled = false
                }
            }
        })

        val columnCountSpinner = view.findViewById(R.id.columnCountSpinner) as Spinner
        val columnCounts = arrayOf(1, 2, 3, 4, 5)
        var adapter = ArrayAdapter(context, android.R.layout.simple_spinner_item, columnCounts)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        columnCountSpinner.adapter = adapter
        columnCountSpinner.setSelection(key.columnSpan - 1)

        val rowCountSpinner = view.findViewById(R.id.rowCountSpinner) as Spinner
        val rowCounts = arrayOf(1, 2, 3, 4)
        adapter = ArrayAdapter(context, android.R.layout.simple_spinner_item, rowCounts)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        rowCountSpinner.adapter = adapter
        rowCountSpinner.setSelection(key.rowSpan - 1)

        val addKeyLayout = view.findViewById(R.id.addKeyLayout) as LinearLayout
        val addRightKeyLayout = view.findViewById(R.id.addRightKeyLayout) as LinearLayout
        val addLeftKeyLayout = view.findViewById(R.id.addLeftKeyLayout) as LinearLayout
        val addFlickUpKeyLayout = view.findViewById(R.id.addFlickUpKeyLayout) as LinearLayout
        val addFlickDownKeyLayout = view.findViewById(R.id.addFlickDownKeyLayout) as LinearLayout
        val addFlickRightKeyLayout = view.findViewById(R.id.addFlickRightKeyLayout) as LinearLayout
        val addFlickLeftKeyLayout = view.findViewById(R.id.addFlickLeftKeyLayout) as LinearLayout

        val keyTypeSpinner = view.findViewById(R.id.keyTypeSpinner) as Spinner
        val keyTypes = Key.Type.values()
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
                val type = Key.Type.toType(description)

                if (type == Key.Type.EMPTY) {
                    //名前と説明を必須にしない
                    descriptionTil.isErrorEnabled = false;
                    //キーを追加できなくする
                    addKeyLayout.visibility = GONE
                } else {
                    if (descriptionTil.editText!!.text.length == 0) {
                        descriptionTil.isErrorEnabled = true
                        descriptionTil.error = "必須"
                    }
                    addKeyLayout.visibility = View.VISIBLE
                }

                if (type == Key.Type.PRESSING) {
                    //キー送信間隔入力ボックスを表示
                    intervalLayout.visibility = View.VISIBLE
                } else {
                    intervalLayout.visibility = GONE
                }

                if (type == Key.Type.KNOB) {
                    addRightKeyLayout.visibility = View.VISIBLE
                    addLeftKeyLayout.visibility = View.VISIBLE
                    addKeyLayout.visibility = GONE
                } else {
                    addRightKeyLayout.visibility = View.GONE
                    addLeftKeyLayout.visibility = View.GONE
                }

                if (type == Key.Type.FLICK) {
                    addFlickUpKeyLayout.visibility = View.VISIBLE
                    addFlickDownKeyLayout.visibility = View.VISIBLE
                    addFlickRightKeyLayout.visibility = View.VISIBLE
                    addFlickLeftKeyLayout.visibility = View.VISIBLE
                    adjustTextInputLayout.visibility = View.VISIBLE
                    addKeyLayout.visibility = GONE
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
        keyTypeSpinner.setSelection(key.type.ordinal)

        val keyCodesLayout = view.findViewById(R.id.keyCodesLayout) as LinearLayout
        val rightKeyCodesLayout = view.findViewById(R.id.rotate_right_keyCodesLayout) as LinearLayout
        val leftKeyCodesLayout = view.findViewById(R.id.rotate_left_keyCodesLayout) as LinearLayout
        val flickUpKeyCodesLayout = view.findViewById(R.id.flick_up_keyCodesLayout) as LinearLayout
        val flickDownKeyCodesLayout = view.findViewById(R.id.flick_down_keyCodesLayout) as LinearLayout
        val flickRightKeyCodesLayout = view.findViewById(R.id.flick_right_keyCodesLayout) as LinearLayout
        val flickLeftKeyCodesLayout = view.findViewById(R.id.flick_left_keyCodesLayout) as LinearLayout
        when (key.type) {
            Key.Type.RELEASED, Key.Type.LONGPRESS, Key.Type.PRESSING -> initKeyCodesLayout(key.keyCodeList, keyCodeViewList, keyCodesLayout)
            Key.Type.KNOB -> {
                initKeyCodesLayout((key as ControlKnob).rotateRightKeyCodeList, rightKeyCodeViewList, rightKeyCodesLayout)
                initKeyCodesLayout(key.rotateLeftKeyCodeList, leftKeyCodeViewList, leftKeyCodesLayout)
            }
            Key.Type.FLICK -> {
                initKeyCodesLayout((key as FlickKey).flickUpKeyStrList, flickUpKeyCodeViewList, flickUpKeyCodesLayout)
                initKeyCodesLayout(key.flickDownKeyStrList, flickDownKeyCodeViewList, flickDownKeyCodesLayout)
                initKeyCodesLayout(key.flickRightKeyStrList, flickRightKeyCodeViewList, flickRightKeyCodesLayout)
                initKeyCodesLayout(key.flickLeftKeyStrList, flickLeftKeyCodeViewList, flickLeftKeyCodesLayout)
            }

        }

        val addButton = view.findViewById(R.id.addButton) as Button
        initAddButton(keyCodesLayout, addButton, keyCodeViewList)

        val addRightButton = view.findViewById(R.id.button_add_rotate_right_key) as Button
        initAddButton(rightKeyCodesLayout, addRightButton, rightKeyCodeViewList)

        val addLeftButton = view.findViewById(R.id.button_add_rotate_left_key) as Button
        initAddButton(leftKeyCodesLayout, addLeftButton, leftKeyCodeViewList)

        val addFlickUpButton = view.findViewById(R.id.button_add_flick_up_key) as Button
        initAddButton(flickUpKeyCodesLayout, addFlickUpButton, flickUpKeyCodeViewList)

        val addFlickDownButton = view.findViewById(R.id.button_add_flick_down_key) as Button
        initAddButton(flickDownKeyCodesLayout, addFlickDownButton, flickDownKeyCodeViewList)

        val addFlickRightButton = view.findViewById(R.id.button_add_flick_right_key) as Button
        initAddButton(flickRightKeyCodesLayout, addFlickRightButton, flickRightKeyCodeViewList)

        val addFlickLeftButton = view.findViewById(R.id.button_add_flick_left_key) as Button
        initAddButton(flickLeftKeyCodesLayout, addFlickLeftButton, flickLeftKeyCodeViewList)

        val createButton = view.findViewById(R.id.createButton) as Button
        createButton.setOnClickListener(View.OnClickListener {
            val editDescription = view.findViewById(R.id.description) as EditText
            val description = editDescription.text.toString()

            val columnCount = columnCountSpinner.selectedItem as Int
            val rowCount = rowCountSpinner.selectedItem as Int

            val type = Key.Type.toType(keyTypeSpinner.selectedItem as String)
            var key: Key = EmptyKey()
            when (type) {
                Key.Type.RELEASED -> {
                    key = NormalKey(columnCount, rowCount, description, type)
                    for (v in keyCodeViewList) {
                        val spinner = v.findViewById(R.id.keyCodeSpinner) as Spinner
                        key.addKeyCode(spinner.selectedItem as String)
                    }
                }
                Key.Type.LONGPRESS -> {
                    key = LongPressKey(columnCount, rowCount, description, type)
                    for (v in keyCodeViewList) {
                        val spinner = v.findViewById(R.id.keyCodeSpinner) as Spinner
                        key.addKeyCode(spinner.selectedItem as String)
                    }
                }
                Key.Type.PRESSING -> {
                    val editInterval = view.findViewById(R.id.interval) as EditText
                    val interval: Int
                    try {
                        interval = Integer.parseInt(editInterval.text.toString())
                    } catch (e: NumberFormatException) {
                        showErrorDialog("入力が不完全です")
                        return@OnClickListener
                    }

                    key = PressingKey(columnCount, rowCount, description, type, interval.toLong())
                    for (v in keyCodeViewList) {
                        val spinner = v.findViewById(R.id.keyCodeSpinner) as Spinner
                        key.addKeyCode(spinner.selectedItem as String)
                    }
                }
                Key.Type.KNOB -> {
                    key = ControlKnob(columnCount, rowCount, description, type)

                    val rightKeyStrList = ArrayList<String>()
                    for (v in rightKeyCodeViewList) {
                        val spinner = v.findViewById(R.id.keyCodeSpinner) as Spinner
                        rightKeyStrList.add(spinner.selectedItem as String)
                    }
                    val leftKeyStrList = ArrayList<String>()
                    for (v in leftKeyCodeViewList) {
                        val spinner = v.findViewById(R.id.keyCodeSpinner) as Spinner
                        leftKeyStrList.add(spinner.selectedItem as String)
                    }
                    /*
                        if (rightKeyStrList.isEmpty() || leftKeyStrList.isEmpty()) {
                            showErrorDialog("少なくとも1つの入力キーが必要です");
                            return;
                        }
                        */
                    key.rotateRightKeyCodeList = rightKeyStrList
                    key.rotateLeftKeyCodeList = leftKeyStrList
                }
                Key.Type.FLICK -> {
                    val adjustEditText = view.findViewById(R.id.edit_text_adjust) as EditText
                    val adjust: Int
                    try {
                        adjust = Integer.parseInt(adjustEditText.text.toString())
                    } catch (e: NumberFormatException) {
                        showErrorDialog("フリック距離を入力してください")
                        return@OnClickListener
                    }

                    key = FlickKey(columnCount, rowCount, description, type, adjust)

                    val flickUpKeyStrList = ArrayList<String>()
                    for (v in flickUpKeyCodeViewList) {
                        val spinner = v.findViewById(R.id.keyCodeSpinner) as Spinner
                        flickUpKeyStrList.add(spinner.selectedItem as String)
                    }
                    val flickDownKeyStrList = ArrayList<String>()
                    for (v in flickDownKeyCodeViewList) {
                        val spinner = v.findViewById(R.id.keyCodeSpinner) as Spinner
                        flickDownKeyStrList.add(spinner.selectedItem as String)
                    }
                    val flickRightKeyStrList = ArrayList<String>()
                    for (v in flickRightKeyCodeViewList) {
                        val spinner = v.findViewById(R.id.keyCodeSpinner) as Spinner
                        flickRightKeyStrList.add(spinner.selectedItem as String)
                    }
                    val flickLeftKeyStrList = ArrayList<String>()
                    for (v in flickLeftKeyCodeViewList) {
                        val spinner = v.findViewById(R.id.keyCodeSpinner) as Spinner
                        flickLeftKeyStrList.add(spinner.selectedItem as String)
                    }
                    /*
                        if (rightKeyStrList.isEmpty() || leftKeyStrList.isEmpty()) {
                            showErrorDialog("少なくとも1つの入力キーが必要です");
                            return;
                        }
                        */
                    key.flickUpKeyStrList = flickUpKeyStrList
                    key.flickDownKeyStrList = flickDownKeyStrList
                    key.flickRightKeyStrList = flickRightKeyStrList
                    key.flickLeftKeyStrList = flickLeftKeyStrList
                }
                Key.Type.EMPTY -> key = EmptyKey(columnCount, rowCount, description, type)
            }

            if (key !is EmptyKey && description.isNullOrEmpty()) {
                showErrorDialog("入力が不完全です");
                return@OnClickListener
            }

            if (columnCount * rowCount > arguments.getInt("emptySpace")) {
                val shortage = columnCount * rowCount - arguments.getInt("emptySpace")
                showErrorDialog("スペースが " + shortage + "足りません")
                return@OnClickListener
            }

            listener!!.onKeyUpdated(arguments.getInt("position"), key)
            dismiss()
        })

        val cancelButton = view.findViewById(R.id.cancelButton) as Button
        cancelButton.setOnClickListener { dismiss() }

        val builder = AlertDialog.Builder(activity)
        builder.setTitle("キー編集").setView(view)
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
        if (context is OnKeyUpdatedListener) {
            listener = context as OnKeyUpdatedListener?
        } else {
            throw RuntimeException(context!!.toString() + " must implement OnKeyUpdatedListener")
        }
    }

    private fun initAddButton(keyCodesLayout: LinearLayout, addKeyCodeButton: Button, keyViewList: MutableList<View>) {
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
                    0 -> adapter.addAll(alphabetList)
                //数字
                    1 -> adapter.addAll(numberList)
                //制御キー
                    2 -> adapter.addAll(controlKeyList)
                //ファンクションキー
                    3 -> adapter.addAll(functionKeyList)
                //特殊
                    4 -> adapter.addAll(specialList)
                //記号
                //5 -> adapter.addAll(symbolList)
                }
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spinner.adapter = adapter
                spinner.setSelection(0)

                val removeButton = addKeyCodeView.findViewById(R.id.removeButton) as Button
                removeButton.setOnClickListener { view ->
                    val parent = view.parent as ViewGroup
                    keyViewList.remove(parent)
                    parent.removeAllViews()
                }
                keyCodesLayout.addView(addKeyCodeView)
                keyViewList.add(addKeyCodeView)
            }
            builder.show()
        }
    }

    private fun initKeyCodesLayout(keyStrList: List<String>, keyViewList: MutableList<View>, keyCodesLayout: LinearLayout) {
        for (keyCode in keyStrList) {
            var spinnerContents: List<String> = ArrayList()
            when {
                alphabetList.contains(keyCode) -> spinnerContents = alphabetList
                numberList.contains(keyCode) -> spinnerContents = numberList
                controlKeyList.contains(keyCode) -> spinnerContents = controlKeyList
                functionKeyList.contains(keyCode) -> spinnerContents = functionKeyList
                specialList.contains(keyCode) -> spinnerContents = specialList
            //symbolList.contains(keyCode) -> spinnerContents = symbolList
            }

            val addKeyCodeView = View.inflate(activity, R.layout.layout_add_key_code, null)

            val spinner = addKeyCodeView.findViewById(R.id.keyCodeSpinner) as Spinner

            val arrayAdapter = ArrayAdapter(context, android.R.layout.simple_spinner_item, spinnerContents)
            arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = arrayAdapter
            spinner.setSelection(arrayAdapter.getPosition(keyCode))

            val removeButton = addKeyCodeView.findViewById(R.id.removeButton) as Button
            removeButton.setOnClickListener { view ->
                val parent = view.parent as ViewGroup
                keyViewList.remove(parent)
                parent.removeAllViews()
            }
            keyCodesLayout.addView(addKeyCodeView)
            keyViewList.add(addKeyCodeView)
        }
    }

    interface OnKeyUpdatedListener {
        fun onKeyUpdated(position: Int, key: Key)
    }

    companion object {

        fun newInstance(position: Int, emptySpace: Int, key: Key): EditKeyDialogFragment {
            val fragment = EditKeyDialogFragment()
            val args = Bundle()
            args.putInt("position", position)
            args.putInt("emptySpace", emptySpace + key.columnSpan * key.rowSpan)
            args.putSerializable("key", key)
            fragment.arguments = args
            return fragment
        }
    }

}
