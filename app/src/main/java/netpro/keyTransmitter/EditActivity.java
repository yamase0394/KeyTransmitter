package netpro.keyTransmitter;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.LinkedList;
import java.util.List;

public class EditActivity extends AppCompatActivity implements EditMenuDialogFragment.OnListItemClickListener, AddKeyDIalogFragment.OnKeyGeneratedListener, EditKeyDIalogFragment.OnKeyUpdatedListener {

    private EditKeyRecyclerViewAdapter adapter;
    private MyRecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle("編集モード");

        recyclerView = (MyRecyclerView) findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(false);
        recyclerView.setLayoutManager(new SpannedGridLayoutManager(new SpannedGridLayoutManager.GridSpanLookup() {
            @Override
            public SpannedGridLayoutManager.SpanInfo getSpanInfo(int position) {
                Key key = adapter.get(position);
                return new SpannedGridLayoutManager.SpanInfo(key.getColumnSpan(), key.getRowSpan());
            }
        }, 4, 1f));

        //RecyclerViewに登録するデータの初期化
        List<Key> datasource = new LinkedList<>();
        File dir = getFilesDir();
        String filePath = dir.getAbsolutePath() + File.separator + "keyboard";
        File file = new File(filePath);
        //シリアライズされたデータがあるか
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
            for (int i = 0; i < 14; i++) {
                datasource.add(new EmptyKey());
            }
            datasource.add(new NormalKey(2, 1, "Ctrl+Shift", "ああああああ", Key.Type.RELEASED));
            datasource.add(new NormalKey(1, 3, "Enter", "エンター", Key.Type.RELEASED));
            datasource.add(new PressingKey(1, 1, "aaa", "aaaあうううううううううううううああ", Key.Type.PRESSING, 100));
        }

        adapter = new EditKeyRecyclerViewAdapter(getApplicationContext());
        adapter.setOnRecyclerClickListener(new OnRecyclerClickListener() {
            @Override
            public void onClickListener(final int position, Key key) {
                android.support.v4.app.DialogFragment dialogFragment = EditMenuDialogFragment.newInstance(position, key.getName(), key.getDescription());
                dialogFragment.show(getSupportFragmentManager(), "fragment_dialog");
            }
        });
        adapter.addAllView(datasource);
        recyclerView.setAdapter(adapter);

        //子View間の幅を設定する
        recyclerView.addItemDecoration(new SpaceItemDecoration(0, 20, 20, 0));

        //子Viewをドラッグで移動できるようにする
        ItemTouchHelper itemDecor = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT, ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                final int fromPos = viewHolder.getAdapterPosition();
                final int toPos = target.getAdapterPosition();
                adapter.move(fromPos, toPos);
                return true;
            }

            //スワイプできないようにする
            @Override
            public int getSwipeDirs(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                if (viewHolder instanceof EditKeyViewHolder) {
                    return 0;
                }
                return super.getSwipeDirs(recyclerView, viewHolder);
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

            }
        });
        itemDecor.attachToRecyclerView(recyclerView);

        //編集モード
        recyclerView.setEditable(true);

        //キー追加ボタン
        FloatingActionButton button = (FloatingActionButton) findViewById(R.id.addViewButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("edit", "add");
                android.support.v4.app.DialogFragment dialogFragment = AddKeyDIalogFragment.newInstance(adapter.getEmptySpace());
                dialogFragment.show(getSupportFragmentManager(), "fragment_dialog");
            }
        });
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
                break;
            case R.id.save:
                //datasourceをシリアライズ
                File dir = getFilesDir();
                String filePath = dir.getAbsolutePath() +  File.separator + adapter.getName();
                try {
                    ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(filePath));
                    out.writeObject(adapter.getDatasource());
                    out.flush();
                    out.close();
                    Log.d("main", "serialize");
                    setResult(RESULT_OK, new Intent());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
        finish();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
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

    @Override
    public void onListItemClicked(int position, String selectedStr) {
        switch (selectedStr) {
            case "編集":
                android.support.v4.app.DialogFragment dialogFragment = EditKeyDIalogFragment.newInstance(adapter.getEmptySpace(), adapter.get(position));
                dialogFragment.show(getSupportFragmentManager(), "fragment_dialog");
                break;
            case "削除":
                adapter.removeView(position, true);
                break;
        }
    }

    @Override
    public void onKeyGenerated(Key key) {
        int keySize = key.getColumnSpan() * key.getRowSpan();

        //keySize分のEmptyKeyを削除する
        for (int i = 0; i < adapter.getItemCount(); i++) {
            if (adapter.get(i) instanceof EmptyKey) {
                Key empty = adapter.get(i);
                keySize -= empty.getColumnSpan() * empty.getRowSpan();
                adapter.removeView(i, false);
                //削除された要素の位置を詰めてしまうのでインデックスを巻き戻す
                i--;
            }

            if (keySize == 0) {
                adapter.addView(key);
                break;
            }

            //EmptyKeyのサイズが1ではない場合、余分に削除してしまうのでサイズ1のEmptyKeyで埋める
            if (keySize < 0) {
                keySize *= -1;
                for (int j = 0; j < keySize ; j++) {
                    adapter.addView(new EmptyKey());
                }
                adapter.addView(key);
                break;
            }
        }
    }

    @Override
    public void onKeyUpdated(Key key) {
        Log.d("updated", key.getName());
    }
}

