package jp.gr.java_conf.snake0394.keytransmitter

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.design.widget.FloatingActionButton
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.KeyEvent
import android.view.Menu
import android.view.MenuItem
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import io.plaidapp.ui.recyclerview.SpannedGridLayoutManager

class EditActivity : AppCompatActivity(), EditMenuDialogFragment.OnListItemClickListener, AddKeyDialogFragment.OnKeyGeneratedListener, EditKeyDialogFragment.OnKeyUpdatedListener {

    private lateinit var recyclerAdapter: EditKeyRecyclerViewAdapter
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)

        val toolbar = findViewById(R.id.toolbar) as Toolbar
        toolbar.title = "Edit"
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setHomeButtonEnabled(true)

        recyclerView = findViewById(R.id.recyclerview) as RecyclerView
        recyclerView.setHasFixedSize(false)
        recyclerView.layoutManager = SpannedGridLayoutManager(SpannedGridLayoutManager.GridSpanLookup { position ->
            val key = recyclerAdapter[position]
            SpannedGridLayoutManager.SpanInfo(key.columnSpan, key.rowSpan)
        }, 4, 1f)

        //RecyclerViewに登録するデータの初期化
        val sp = PreferenceManager.getDefaultSharedPreferences(applicationContext)
        val type = object : TypeToken<ArrayList<BaseKey>>() {}.type
        val gson = GsonBuilder()
                .registerTypeAdapter(type, KeyDeserializer())
                .create()
        var datasource: ArrayList<BaseKey> = gson.fromJson<ArrayList<BaseKey>>(sp.getString(MainActivity.SAVE_KEY, ""), object : TypeToken<ArrayList<BaseKey>>() {}.getType())

        recyclerAdapter = EditKeyRecyclerViewAdapter()
        recyclerAdapter.setOnRecyclerClickListener(OnRecyclerClickListener { position: Int, abstractKey: BaseKey ->
            val dialogFragment = EditMenuDialogFragment.newInstance(position, abstractKey.description)
            dialogFragment.show(supportFragmentManager, "fragment_dialog")
        })
        recyclerAdapter.addAllView(datasource)
        recyclerView.adapter = recyclerAdapter

        //子View間の幅を設定する
        recyclerView.addItemDecoration(SpaceItemDecoration(0, 10, 20, 0))

        //子Viewをドラッグで移動できるようにする
        val itemDecor = ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP or ItemTouchHelper.DOWN or ItemTouchHelper.RIGHT or ItemTouchHelper.LEFT, ItemTouchHelper.RIGHT) {
            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                val fromPos = viewHolder.adapterPosition
                val toPos = target.adapterPosition
                recyclerAdapter.move(fromPos, toPos)
                return true
            }

            //スワイプできないようにする
            override fun getSwipeDirs(recyclerView: RecyclerView?, viewHolder: RecyclerView.ViewHolder?): Int {
                if (viewHolder is EditKeyViewHolder) {
                    return 0
                }
                return super.getSwipeDirs(recyclerView, viewHolder)
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            }
        })
        itemDecor.attachToRecyclerView(recyclerView)

        //キー追加ボタン
        val button = findViewById(R.id.addViewButton) as FloatingActionButton
        button.setOnClickListener {
            val dialogFragment = AddKeyDialogFragment.newInstance(recyclerAdapter.emptySpace)
            dialogFragment.show(supportFragmentManager, "fragment_dialog")
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_edit, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home, R.id.cancel -> {
                val sp = PreferenceManager.getDefaultSharedPreferences(applicationContext)
                val pre = sp.getString(MainActivity.SAVE_KEY, "")
                val gson = GsonBuilder()
                        .registerTypeAdapter(object : TypeToken<ArrayList<BaseKey>>() {}.type, KeySerializer())
                        .create()
                val now = gson.toJson(recyclerAdapter.keyList)
                if (pre != now) {
                    AlertDialog.Builder(this@EditActivity)
                            .setMessage("変更を破棄して終了しますか？")
                            .setPositiveButton("Yes", { _, _ ->
                                finish()
                                overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
                            })
                            .setNegativeButton("No", null)
                            .show()
                    return super.onOptionsItemSelected(item)
                }
            }
            R.id.save -> {
                //datasourceをシリアライズ
                try {
                    val sp = PreferenceManager.getDefaultSharedPreferences(applicationContext)
                    val type = object : TypeToken<ArrayList<BaseKey>>() {}.type
                    val gson = GsonBuilder()
                            .registerTypeAdapter(type, KeySerializer())
                            .create()
                    sp.edit().putString(MainActivity.SAVE_KEY, gson.toJson(recyclerAdapter.keyList)).apply()
                    setResult(Activity.RESULT_OK, Intent())
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
        finish()
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
        return super.onOptionsItemSelected(item)
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            val sp = PreferenceManager.getDefaultSharedPreferences(applicationContext)
            val pre = sp.getString(MainActivity.SAVE_KEY, "")
            val gson = GsonBuilder()
                    .registerTypeAdapter(object : TypeToken<ArrayList<BaseKey>>() {}.type, KeySerializer())
                    .create()
            val now = gson.toJson(recyclerAdapter.keyList)
            if (pre != now) {
                AlertDialog.Builder(this@EditActivity)
                        .setMessage("変更を破棄して終了しますか？")
                        .setPositiveButton("Yes", { _, _ ->
                            finish()
                            overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
                        })
                        .setNegativeButton("No", null)
                        .show()
            } else {
                finish()
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
            }
        }
        return false
    }

    override fun onListItemClicked(position: Int, selectedStr: String) {
        when (selectedStr) {
            "編集" -> {
                val dialogFragment = EditKeyDialogFragment.newInstance(position, recyclerAdapter.emptySpace, recyclerAdapter[position])
                dialogFragment.show(supportFragmentManager, "fragment_dialog")
            }
            "削除" -> recyclerAdapter.removeView(position, true)
        }
    }

    override fun onKeyGenerated(abstractKey: BaseKey) {
        var keySize = abstractKey.columnSpan * abstractKey.rowSpan

        //keySize分のEmptyKeyを削除する
        var i = 0
        while (i < recyclerAdapter.itemCount) {
            if (recyclerAdapter[i] is EmptyKey) {
                val empty = recyclerAdapter[i]
                keySize -= empty.columnSpan * empty.rowSpan
                recyclerAdapter.removeView(i, false)
                //削除された要素の位置を詰めてしまうのでインデックスを巻き戻す
                i--
            }

            if (keySize == 0) {
                recyclerAdapter.addView(abstractKey)
                break
            }

            //EmptyKeyのサイズが1ではない場合、余分に削除してしまうのでサイズ1のEmptyKeyで埋める
            if (keySize < 0) {
                keySize *= -1
                for (j in 0..keySize - 1) {
                    recyclerAdapter.addView(EmptyKey())
                }
                recyclerAdapter.addView(abstractKey)
                break
            }
            i++
        }
    }

    override fun onKeyUpdated(position: Int, abstractKey: BaseKey) {
        recyclerAdapter.removeView(position, true)

        var keySize = abstractKey.columnSpan * abstractKey.rowSpan
        //keySize分のEmptyKeyを削除する
        for (i in recyclerAdapter.itemCount - 1 downTo 0) {
            if (recyclerAdapter[i] is EmptyKey) {
                val empty = recyclerAdapter[i]
                keySize -= empty.columnSpan * empty.rowSpan
                recyclerAdapter.removeView(i, false)
            }

            if (keySize == 0) {
                recyclerAdapter.addView(position, abstractKey)
                break
            }

            //EmptyKeyのサイズが1ではない場合、余分に削除してしまうのでサイズ1のEmptyKeyで埋める
            if (keySize < 0) {
                keySize *= -1
                for (j in 0..keySize - 1) {
                    recyclerAdapter.addView(EmptyKey())
                }
                recyclerAdapter.addView(position, abstractKey)
                break
            }
        }
    }
}

