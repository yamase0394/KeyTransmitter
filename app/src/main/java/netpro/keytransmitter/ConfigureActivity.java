package netpro.keytransmitter;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class ConfigureActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configure);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle("設定");

        final SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        final TextInputLayout ipTextInputLayout = (TextInputLayout) findViewById(R.id.text_input_layout_ip);
        ipTextInputLayout.getEditText().setText(sp.getString("ip", null));

        final TextInputLayout portTextInputLayout = (TextInputLayout) findViewById(R.id.text_input_layout_port);
        portTextInputLayout.getEditText().setText(String.valueOf(sp.getInt("port", 8080)));

        //final TextInputLayout passTextInputLayout = (TextInputLayout) findViewById(R.id.text_input_layout_pass);
        //passTextInputLayout.getEditText().setText(sp.getString("pass", null));

        Button saveBtn = (Button) findViewById(R.id.button_save);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String ipStr = ipTextInputLayout.getEditText().getText().toString();
                int port = Integer.parseInt(portTextInputLayout.getEditText().getText().toString());

                SharedPreferences.Editor editor = sp.edit();
                editor.putString("ip", ipStr);
                editor.putInt("port", port);
                //editor.putString("pass", passTextInputLayout.getEditText().toString());
                editor.apply();

                SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                KeyTransmitter.INSTANCE.setIp(sp.getString("ip", null));
                KeyTransmitter.INSTANCE.setPort(sp.getInt("port",8080));

                finish();
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });

        Button cancelBtn = (Button) findViewById(R.id.button_cancel);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            return true;
        }
        return false;
    }
}

