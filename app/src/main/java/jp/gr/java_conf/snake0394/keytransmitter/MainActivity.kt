package jp.gr.java_conf.snake0394.keytransmitter

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import io.plaidapp.ui.recyclerview.SpannedGridLayoutManager

class MainActivity : AppCompatActivity() {
    private val TAG = "MainActivity"

    private lateinit var recyclerAdapter: KeyRecyclerViewAdapter
    private lateinit var recyclerView: RecyclerView
    private val adView: AdView by lazy { findViewById(R.id.adView) as AdView }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Log.d(TAG, "onCreate")

        MobileAds.initialize(this, "ca-app-pub-1067539886647773~9620833844");
        val adRequest = AdRequest.Builder()
                .addTestDevice("CB89206D3413270C73A3DBEA8C304BFC")
                .build()
        adView.loadAd(adRequest)

        val toolbar = findViewById(R.id.toolbar) as Toolbar
        toolbar.title = "Programmable Keyboard"
        setSupportActionBar(toolbar)

        recyclerView = findViewById(R.id.recyclerview) as RecyclerView
        recyclerView.setHasFixedSize(false)
        recyclerView.layoutManager = SpannedGridLayoutManager(SpannedGridLayoutManager.GridSpanLookup { position ->
            val key = recyclerAdapter[position]
            SpannedGridLayoutManager.SpanInfo(key.columnSpan, key.rowSpan)
        }, 4, 1f)

        var dataSource: ArrayList<BaseKey> = ArrayList()
        val sp = PreferenceManager.getDefaultSharedPreferences(applicationContext)

        val type = object : TypeToken<ArrayList<BaseKey>>() {}.type
        val gson = GsonBuilder()
                .registerTypeAdapter(type, KeySerializer())
                .registerTypeAdapter(type, KeyDeserializer())
                .create()

        if (sp.getString(SAVE_KEY, "").isNullOrEmpty()) {
            for (i in 0..19) {
                dataSource.add(EmptyKey())
            }
            sp.edit().putString(SAVE_KEY, gson.toJson(dataSource)).apply()
        } else {
            dataSource = gson.fromJson<ArrayList<BaseKey>>(sp.getString(SAVE_KEY, ""), type)
        }

        recyclerAdapter = KeyRecyclerViewAdapter()
        recyclerAdapter.addAllView(dataSource)
        recyclerView.adapter = recyclerAdapter

        recyclerView.addItemDecoration(SpaceItemDecoration(0, 10, 20, 0))

        KeyTransmitter.run(sp.getString("ip", ""), sp.getInt("port", 8080), applicationContext)
    }

    override fun onDestroy() {
        super.onDestroy()
        adView.destroy()
        Log.d(TAG, "onDestroy")
        KeyTransmitter.stop()
    }

    override fun onPause() {
        super.onPause()
        adView.pause()
        Log.d(TAG, "onPause")
    }

    override fun onResume() {
        super.onResume()
        adView.resume()
        Log.d(TAG, "onResume")
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.editMode -> {
                var intent = Intent(applicationContext, EditActivity::class.java)
                startActivityForResult(intent, EDIT_REQUEST_CODE)
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
            }
            R.id.menu_item_config -> {
                intent = Intent(applicationContext, ConfigureActivity::class.java)
                startActivity(intent)
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        //EditActivityでの編集結果を反映
        if (resultCode == Activity.RESULT_OK && requestCode == EDIT_REQUEST_CODE) {
            recyclerAdapter = KeyRecyclerViewAdapter()
            val sp = PreferenceManager.getDefaultSharedPreferences(applicationContext)
            val type = object : TypeToken<ArrayList<BaseKey>>() {}.type
            val gson = GsonBuilder()
                    .registerTypeAdapter(type, KeyDeserializer())
                    .create()
            recyclerAdapter.keyList = gson.fromJson<ArrayList<BaseKey>>(sp.getString(SAVE_KEY, ""), type)
            recyclerView.swapAdapter(recyclerAdapter, false)
        }
    }

    companion object {
        private val EDIT_REQUEST_CODE = 1
        val SAVE_KEY = "keyboard1"
    }
}

