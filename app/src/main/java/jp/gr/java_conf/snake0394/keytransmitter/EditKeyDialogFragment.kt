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

class EditKeyDialogFragment : android.support.v4.app.DialogFragment() {

    private lateinit var alphabetList: List<String>
    private lateinit var numberList: List<String>
    private lateinit var controlKeyList: List<String>
    private lateinit var functionKeyList: List<String>
    private lateinit var specialList: List<String>
    //private lateinit var symbolList: List<String>

    private lateinit var listener: OnKeyUpdatedListener
    private val keyCodeViewList = ArrayList<View>()
    private val rightKeyCodeViewList = ArrayList<View>()
    private val leftKeyCodeViewList = ArrayList<View>()
    private val flickUpKeyCodeViewList = ArrayList<View>()
    private val flickDownKeyCodeViewList = ArrayList<View>()
    private val flickRightKeyCodeViewList = ArrayList<View>()
    private val flickLeftKeyCodeViewList = ArrayList<View>()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val view = View.inflate(activity, R.layout.dialog_fragment_edit_key, null)
        val key = arguments.getSerializable("key") as BaseKey

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
                    editable.toString().toInt()
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
                val type = KeyType.toType(str)
                if (type != KeyType.EMPTY && editable.isEmpty()) {
                    descriptionTil.isErrorEnabled = true
                    descriptionTil.error = "必須"
                } else {
                    descriptionTil.isErrorEnabled = false
                }
            }
        })

        val columnCountSpinner = view.findViewById(R.id.columnCountSpinner) as Spinner
        val columnCounts = arrayOf(1, 2, 3, 4)
        var adapter = ArrayAdapter(context, android.R.layout.simple_spinner_item, columnCounts)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        columnCountSpinner.adapter = adapter
        columnCountSpinner.setSelection(key.columnSpan - 1)

        val rowCountSpinner = view.findViewById(R.id.rowCountSpinner) as Spinner
        val rowCounts = arrayOf(1, 2, 3, 4, 5)
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
                        descriptionTil.error = "必須"
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
                    addRightKeyLayout.visibility = View.VISIBLE
                    addLeftKeyLayout.visibility = View.VISIBLE
                    addKeyLayout.visibility = GONE
                } else {
                    addRightKeyLayout.visibility = View.GONE
                    addLeftKeyLayout.visibility = View.GONE
                }

                if (type == KeyType.FLICK) {
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
        keyTypeSpinner.setSelection(key.keyType.ordinal)

        val keyCodesLayout = view.findViewById(R.id.keyCodesLayout) as LinearLayout
        val rightKeyCodesLayout = view.findViewById(R.id.rotate_right_keyCodesLayout) as LinearLayout
        val leftKeyCodesLayout = view.findViewById(R.id.rotate_left_keyCodesLayout) as LinearLayout
        val flickUpKeyCodesLayout = view.findViewById(R.id.flick_up_keyCodesLayout) as LinearLayout
        val flickDownKeyCodesLayout = view.findViewById(R.id.flick_down_keyCodesLayout) as LinearLayout
        val flickRightKeyCodesLayout = view.findViewById(R.id.flick_right_keyCodesLayout) as LinearLayout
        val flickLeftKeyCodesLayout = view.findViewById(R.id.flick_left_keyCodesLayout) as LinearLayout
        when (key.keyType) {
            KeyType.RELEASED, KeyType.LONG_PRESS, KeyType.PRESSING -> initKeyCodesLayout(key.keyCodesMap["normal"] ?: listOf(), keyCodeViewList, keyCodesLayout)
            KeyType.KNOB -> {
                initKeyCodesLayout(key.keyCodesMap["right"] ?: listOf(), rightKeyCodeViewList, rightKeyCodesLayout)
                initKeyCodesLayout(key.keyCodesMap["left"] ?: listOf(), leftKeyCodeViewList, leftKeyCodesLayout)
            }
            KeyType.FLICK -> {
                initKeyCodesLayout(key.keyCodesMap["up"] ?: listOf(), flickUpKeyCodeViewList, flickUpKeyCodesLayout)
                initKeyCodesLayout(key.keyCodesMap["down"] ?: listOf(), flickDownKeyCodeViewList, flickDownKeyCodesLayout)
                initKeyCodesLayout(key.keyCodesMap["right"] ?: listOf(), flickRightKeyCodeViewList, flickRightKeyCodesLayout)
                initKeyCodesLayout(key.keyCodesMap["left"] ?: listOf(), flickLeftKeyCodeViewList, flickLeftKeyCodesLayout)
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
                }
                KeyType.EMPTY -> key = EmptyKey(columnCount, rowCount, description, type)
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

            listener.onKeyUpdated(arguments.getInt("position"), key)
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
            listener = context
        } else {
            throw RuntimeException(context!!.toString() + " must implement OnKeyUpdatedListener")
        }

        alphabetList = resources.getStringArray(R.array.alphabet).asList()
        numberList = resources.getStringArray(R.array.number).asList()
        controlKeyList = resources.getStringArray(R.array.control_key).asList()
        functionKeyList = resources.getStringArray(R.array.function_key).asList()
        specialList = resources.getStringArray(R.array.special_key).asList()
        //symbolList = resources.getStringArray(R.array.symbol).asList()
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

    private fun toKeyStrList(keyViewList: List<View>): List<String> {
        return keyViewList
                .map { it.findViewById(R.id.keyCodeSpinner) as Spinner }
                .map { it.selectedItem as String }
    }

    interface OnKeyUpdatedListener {
        fun onKeyUpdated(position: Int, abstractKey: BaseKey)
    }

    companion object {
        fun newInstance(position: Int, emptySpace: Int, key: BaseKey): EditKeyDialogFragment {
            val fragment = EditKeyDialogFragment()
            val args = Bundle()
            args.putInt("position", position)
            if (key.keyType == KeyType.EMPTY) {
                args.putInt("emptySpace", emptySpace)
            } else {
                args.putInt("emptySpace", emptySpace + key.columnSpan * key.rowSpan)
            }
            args.putSerializable("key", key)
            fragment.arguments = args
            return fragment
        }
    }

}
