package jp.gr.java_conf.snake0394.keytransmitter

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.design.widget.TextInputLayout
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.util.Base64
import android.view.KeyEvent
import android.view.MenuItem
import android.widget.Button
import android.widget.Toast
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.nio.ByteBuffer
import java.nio.CharBuffer
import java.util.*

class ConfigureActivity : AppCompatActivity(), EditIpListDialog.OnCheckFinishedListener {

    private lateinit var map: MutableMap<String, String>
    private val resultIntent by lazy { Intent() }

    override fun onCheckFinished(checkedList: List<String>) {
        map = map.filterValues { !checkedList.contains(it) }.toMutableMap()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_configure)

        val keyStoreManager = KeyStoreManager.getInstance(applicationContext)

        val toolbar = findViewById(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setHomeButtonEnabled(true)
        supportActionBar!!.title = "設定"

        val sp = PreferenceManager.getDefaultSharedPreferences(applicationContext)

        val tokenType = object : TypeToken<LinkedHashMap<String, String>>() {}.type
        val gson = Gson()
        val json = sp.getString(MainActivity.SP_KEY_IP_MAP, "")
        map = if (json.isNullOrBlank()) mutableMapOf() else gson.fromJson<MutableMap<String, String>>(json, tokenType)

        val ipTextInputLayout = findViewById(R.id.text_input_layout_ip) as TextInputLayout

        val ipAddBtn = findViewById(R.id.btn_add_ip) as Button
        ipAddBtn.setOnClickListener {
            val ipStr = ipTextInputLayout.editText!!.text.toString()
            if (!ipStr.isNullOrBlank()) {
                map.put(ipStr, "未接続(${ipStr})")
                resultIntent.putExtra("ip", ipStr)
                Toast.makeText(this, "追加完了", Toast.LENGTH_SHORT).show()
            }
            ipTextInputLayout.editText!!.setText("")
        }

        val ipEditBtn = findViewById(R.id.btn_edit_ip) as Button
        ipEditBtn.setOnClickListener {
            if(map.isEmpty()){
                Toast.makeText(this, "リストが空です", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            EditIpListDialog.show(this, map.values.toTypedArray())
        }

        val portTextInputLayout = findViewById(R.id.text_input_layout_port) as TextInputLayout
        portTextInputLayout.editText!!.setText(sp.getInt("port", 8080).toString())

        val passTextInputLayout = findViewById(R.id.text_input_layout_pass) as TextInputLayout
        val cipherText = sp.getString("pass", "")
        if (cipherText.isNullOrBlank()) {
            passTextInputLayout.editText!!.setText("")
        } else {
            val pwBytes = keyStoreManager.decrypt(Base64.decode(cipherText, Base64.DEFAULT))
            val charBuf = Charsets.UTF_8.decode(ByteBuffer.wrap(pwBytes))
            val pwChars = CharArray(charBuf.limit())
            charBuf.get(pwChars)
            passTextInputLayout.editText!!.setText(pwChars, 0, pwChars.size)
            Arrays.fill(pwBytes, 0)
            Arrays.fill(pwChars, ' ')
        }

        val saveBtn = findViewById(R.id.button_save) as Button
        saveBtn.setOnClickListener {
            val editor = sp.edit()

            editor.putString("ipToNameMap", gson.toJson(map, tokenType))

            val port = Integer.parseInt(portTextInputLayout.editText!!.text.toString())
            editor.putInt("port", port)

            val pwEditable = passTextInputLayout.editText!!.text
            val pwChars = CharArray(pwEditable.length)
            pwEditable.getChars(0, pwEditable.length, pwChars, 0)
            val byteBuf = Charsets.UTF_8.encode(CharBuffer.wrap(pwChars))
            val pwBytes = ByteArray(byteBuf.limit())
            byteBuf.get(pwBytes)
            editor.putString("pass", Base64.encodeToString(keyStoreManager.encrypt(pwBytes), Base64.DEFAULT))
            Arrays.fill(pwChars, ' ')
            Arrays.fill(pwBytes, 0)

            editor.apply()

            setResult(Activity.RESULT_OK, resultIntent)
            finish()
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
        }

        val cancelBtn = findViewById(R.id.button_cancel) as Button
        cancelBtn.setOnClickListener {
            finish()
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish()
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
            return true
        }
        return false
    }
}

