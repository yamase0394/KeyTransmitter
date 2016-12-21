package netpro.keyTransmitter;

import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RelativeLayout;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.LinkedList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE = 1;
    private KeyRecyclerViewAdapter adapter;
    private static Point viewSize;
    private MyRecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        recyclerView = (MyRecyclerView) findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(false);
        recyclerView.setLayoutManager(new SpannedGridLayoutManager(new SpannedGridLayoutManager.GridSpanLookup() {
            @Override
            public SpannedGridLayoutManager.SpanInfo getSpanInfo(int position) {
                Key key = adapter.get(position);
                return new SpannedGridLayoutManager.SpanInfo(key.getColumnSpan(), key.getRowSpan());
            }
        }, 4, 1f));

        List<Key> datasource = new LinkedList<>();
        File dir = getFilesDir();
        String fileSeparator = File.separator;
        String filePath = dir.getAbsolutePath() + fileSeparator + "keyboard";
        File file = new File(filePath);
        Log.d("main", file.toString());
        if (file.exists()) {
            try {
                ObjectInputStream in = new ObjectInputStream(new FileInputStream(file));
                datasource = (List<Key>) in.readObject();
                Log.d("main", "deserialize");
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        } else {
            Log.d("main", "ない");
            for (int i = 0; i < 14; i++) {
                datasource.add(new EmptyKey());
            }
            datasource.add(new NormalKey(2, 1, "Ctrl+Shift", "ああああああ", Key.Type.RELEASED));
            datasource.add(new NormalKey(1, 3, "Enter", "エンター", Key.Type.RELEASED));
            datasource.add(new PressingKey(1, 1, "aaa", "aaaあうううううううううううううああ", Key.Type.PRESSING, 100));
        }

        adapter = new KeyRecyclerViewAdapter(getApplicationContext());
        adapter.addAllView(datasource);

        recyclerView.setAdapter(adapter);

        recyclerView.addItemDecoration(new SpaceItemDecoration(0, 1, 1, 0));
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        RelativeLayout r = (RelativeLayout) findViewById(R.id.activity_main);
        Point point = new Point(0, 0);
        point.set(r.getWidth(), r.getHeight());
        viewSize = point;
        Log.d("setViewSize", String.valueOf(point.x) + ":" + String.valueOf(point.y));
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
                startActivityForResult(intent, REQUEST_CODE);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
            File dir = getFilesDir();
            String fileSeparator = File.separator;
            String filePath = dir.getAbsolutePath() + fileSeparator + "keyboard";
            File file = new File(filePath);
            Log.d("main", file.toString());
            if (file.exists()) {
                try {
                    ObjectInputStream in = new ObjectInputStream(new FileInputStream(file));
                    List<Key> datasource = (List<Key>) in.readObject();
                    Log.d("main", "deserialize");
                    adapter.setDatasource(datasource);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

