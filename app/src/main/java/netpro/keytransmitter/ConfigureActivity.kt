package netpro.keytransmitter

import android.os.Bundle
import android.preference.PreferenceManager
import android.support.design.widget.TextInputLayout
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.util.Base64
import android.view.KeyEvent
import android.view.MenuItem
import android.widget.Button
import java.nio.ByteBuffer
import java.nio.CharBuffer
import java.util.*

class ConfigureActivity : AppCompatActivity() {

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

        val ipTextInputLayout = findViewById(R.id.text_input_layout_ip) as TextInputLayout
        ipTextInputLayout.editText!!.setText(sp.getString("ip", null))

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

            val ipStr = ipTextInputLayout.editText!!.text.toString()
            editor.putString("ip", ipStr)

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

            KeyTransmitter.restart(ipStr, port)

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

