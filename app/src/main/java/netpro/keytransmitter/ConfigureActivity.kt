package netpro.keytransmitter

import android.os.Bundle
import android.preference.PreferenceManager
import android.support.design.widget.TextInputLayout
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.KeyEvent
import android.view.MenuItem
import android.widget.Button

class ConfigureActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_configure)

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

        //final TextInputLayout passTextInputLayout = (TextInputLayout) findViewById(R.id.text_input_layout_pass);
        //passTextInputLayout.getEditText().setText(sp.getString("pass", null));

        val saveBtn = findViewById(R.id.button_save) as Button
        saveBtn.setOnClickListener {
            val ipStr = ipTextInputLayout.editText!!.text.toString()
            val port = Integer.parseInt(portTextInputLayout.editText!!.text.toString())

            val editor = sp.edit()
            editor.putString("ip", ipStr)
            editor.putInt("port", port)
            //editor.putString("pass", passTextInputLayout.getEditText().toString());
            editor.apply()

            val sp = PreferenceManager.getDefaultSharedPreferences(applicationContext)
            KeyTransmitter.ip = sp.getString("ip", null)
            KeyTransmitter.port = sp.getInt("port", 8080)

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

