package netpro.keytransmitter;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int EDIT_REQUEST_CODE = 1;
    private KeyRecyclerViewAdapter adapter;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(false);
        recyclerView.setLayoutManager(new SpannedGridLayoutManager(new SpannedGridLayoutManager.GridSpanLookup() {
            @Override
            public SpannedGridLayoutManager.SpanInfo getSpanInfo(int position) {
                Key key = adapter.get(position);
                return new SpannedGridLayoutManager.SpanInfo(key.getColumnSpan(), key.getRowSpan());
            }
        }, 4, 1f));

        List<Key> datasource = new ArrayList<>();
        File dir = getFilesDir();
        String filePath = dir.getAbsolutePath() + File.separator + "keyboard";
        File file = new File(filePath);
        if (file.exists()) {
            try {
                ObjectInputStream in = new ObjectInputStream(new FileInputStream(file));
                datasource = (List<Key>) in.readObject();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        } else {
            for (int i = 0; i < 20; i++) {
                datasource.add(new EmptyKey());
            }
            try {
                ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(filePath));
                out.writeObject(datasource);
                out.flush();
                out.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        adapter = new KeyRecyclerViewAdapter(getApplicationContext());
        adapter.addAllView(datasource);
        recyclerView.setAdapter(adapter);

        recyclerView.addItemDecoration(new SpaceItemDecoration(0, 15, 20, 0));

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        KeyTransmitter.INSTANCE.setIp(sp.getString("ip", null));
        KeyTransmitter.INSTANCE.setPort(sp.getInt("port",8080));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.editMode:
                Intent intent = new Intent(getApplicationContext(), EditActivity.class);
                startActivityForResult(intent, EDIT_REQUEST_CODE);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                break;
            case R.id.menu_item_config:
                intent = new Intent(getApplicationContext(), ConfigureActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //EditActivityでの編集結果を反映
        if (resultCode == RESULT_OK && requestCode == EDIT_REQUEST_CODE) {
            File dir = getFilesDir();
            String filePath = dir.getAbsolutePath() + File.separator + "keyboard";
            File file = new File(filePath);
            if (file.exists()) {
                try {
                    ObjectInputStream in = new ObjectInputStream(new FileInputStream(file));
                    List<Key> datasource = (List<Key>) in.readObject();
                    adapter = new KeyRecyclerViewAdapter(getApplicationContext());
                    adapter.setDatasource(datasource);
                    recyclerView.swapAdapter(adapter, false);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

