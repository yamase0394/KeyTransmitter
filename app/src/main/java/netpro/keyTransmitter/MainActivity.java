package netpro.keyTransmitter;

import android.graphics.Point;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.widget.RelativeLayout;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.LinkedList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

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
        //ScrollController controller = new ScrollController();
        //recyclerView.addOnItemTouchListener(controller);
        recyclerView.setHasFixedSize(false);
        recyclerView.setLayoutManager(new SpannedGridLayoutManager(position -> {
            Key key = adapter.get(position);
            return new SpannedGridLayoutManager.SpanInfo(key.getColumnSpan(), key.getRowSpan());
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
        datasource.add(new NormalKey(2, 1, "", "", Key.Type.RELEASED));
        datasource.add(new NormalKey(1, 3, "", "", Key.Type.RELEASED));
        datasource.add(new PressingKey(1, 1, "aaa", "aaa", Key.Type.PRESSING, 100));
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


    private class ScrollController implements RecyclerView.OnItemTouchListener {

        public ScrollController() {
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
            //return rv.onInterceptTouchEvent(e);
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (adapter == null) {
            return;
        }

        File dir = getFilesDir();
        String fileSeparator = File.separator;
        String filePath = dir.getAbsolutePath() + fileSeparator + adapter.getName();
        Log.d("path", filePath);
        try {
            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(filePath));
            out.writeObject(adapter);
            out.close();
            Log.d("main", "serialize");
        } catch (Exception e) {
            e.printStackTrace();
        }
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
               recyclerView.setEditable(true);
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}

