package netpro.keyTransmitter;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.LinkedList;
import java.util.List;

public class EditActivity extends AppCompatActivity {

    private KeyRecyclerViewAdapter adapter;
    private MyRecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

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
        String filePath = dir.getAbsolutePath() + fileSeparator + "keyBoard.txt";
        File file = new File(filePath);
        Log.d("main", file.toString());
        if (file.exists()) {
            try {
                ObjectInputStream in = new ObjectInputStream(new FileInputStream(file));
                adapter = (KeyRecyclerViewAdapter) in.readObject();
                Log.d("main", "deserialize");
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        } else {

        }

        Log.d("main", "ない");
        for (int i = 0; i < 14; i++) {
            datasource.add(new EmptyKey());
        }
        datasource.add(new NormalKey(2, 1, "Ctrl+Shift", "ああああああ", Key.Type.RELEASED));
        datasource.add(new NormalKey(1, 3, "Enter", "エンター", Key.Type.RELEASED));
        datasource.add(new PressingKey(1, 1, "aaa", "aaaあうううううううううううううああ", Key.Type.PRESSING, 100));
        adapter = new KeyRecyclerViewAdapter(getApplicationContext());
        adapter.addAllView(datasource);

        recyclerView.setAdapter(adapter);

        recyclerView.addItemDecoration(new SpaceItemDecoration(0, 1, 1, 0));

        ItemTouchHelper itemDecor = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT, ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                final int fromPos = viewHolder.getAdapterPosition();
                final int toPos = target.getAdapterPosition();
                adapter.move(fromPos, toPos);
                return true;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                final int fromPos = viewHolder.getAdapterPosition();
                adapter.removeView(fromPos);
            }
        });
        itemDecor.attachToRecyclerView(recyclerView);

        recyclerView.setEditable(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_edit, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
            case R.id.cancel:
                finish();
                break;
            case R.id.save:
                File dir = getFilesDir();
                String fileSeparator = File.separator;
                String filePath = dir.getAbsolutePath() + fileSeparator + adapter.getName();
                Log.d("path", filePath);
                try {
                    ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(filePath));
                    out.writeObject(adapter);
                    out.flush();
                    out.close();
                    Log.d("main", "serialize");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}

