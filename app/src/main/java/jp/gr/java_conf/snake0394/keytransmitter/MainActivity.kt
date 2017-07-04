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
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import io.plaidapp.ui.recyclerview.SpannedGridLayoutManager

class MainActivity : AppCompatActivity() {
    private val TAG = "MainActivity"

    private lateinit var recyclerAdapter: KeyRecyclerViewAdapter
    private val recyclerView by lazy { findViewById(R.id.recyclerview) as RecyclerView }
    private val adView by lazy { findViewById(R.id.adView) as AdView }
    private val sp by lazy { PreferenceManager.getDefaultSharedPreferences(applicationContext) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Logger.d(TAG, "onCreate")

        MobileAds.initialize(this, "ca-app-pub-1067539886647773~9620833844");
        val adRequest = AdRequest.Builder()
                .addTestDevice("CB89206D3413270C73A3DBEA8C304BFC")
                .build()
        adView.loadAd(adRequest)

        val toolbar = findViewById(R.id.toolbar) as Toolbar
        val spinner = toolbar.findViewById(R.id.spinner_name_ip) as Spinner
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, pos: Int, id: Long) {
                val spinner = parent as Spinner
                if (!spinner.isFocusable) {
                    Logger.d(TAG, "spinner_name_ip is not focusable")
                    spinner.isFocusable = true
                    return
                }

                Logger.d(TAG, "spinner_name_ip:${spinner.selectedItem as String} was selected")
                val json = sp.getString(SP_KEY_IP_MAP, "")
                val ipToNameMap = Gson().fromJson<MutableMap<String, String>>(json, object : TypeToken<LinkedHashMap<String, String>>() {}.type)
                val index = ipToNameMap.values.indexOf(spinner.selectedItem as String)
                val ip = ipToNameMap.keys.elementAt(index)

                KeyTransmitter.restart(ip, sp.getInt("port", 8080))
            }
        }

        val typeBaseKeyArray = object : TypeToken<ArrayList<BaseKey>>() {}.type
        val gson = GsonBuilder()
                .registerTypeAdapter(typeBaseKeyArray, KeySerializer())
                .registerTypeAdapter(typeBaseKeyArray, KeyDeserializer())
                .create()
        val json = sp.getString(SP_KEY_IP_MAP, "")
        val ipToNameMap: MutableMap<String, String>
        if (json.isNullOrBlank()) {
            Logger.d(TAG, "ipMap json is null or empty")
            val adapter = ArrayAdapter<String>(this, R.layout.spinner_item)
            adapter.add("未接続")
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter

            ipToNameMap = mutableMapOf()
        } else {
            Logger.d(TAG, "generate ipMap from json")

            ipToNameMap = gson.fromJson<MutableMap<String, String>>(json, object : TypeToken<LinkedHashMap<String, String>>() {}.type)
            Logger.d(TAG, "recentDest = ${sp.getString(SP_KEY_RECENT_DEST, "")}")
            var index = ipToNameMap.keys.indexOf(sp.getString(SP_KEY_RECENT_DEST, ""))
            if (index == -1) {
                index = ipToNameMap.values.indexOfFirst { it.startsWith("未接続") }
                if (index == -1) {
                    index = 0
                }
            }

            val adapter = ArrayAdapter<String>(this, R.layout.spinner_item)
            if (ipToNameMap.isEmpty()) {
                adapter.add("未接続")
            } else {
                adapter.addAll(ipToNameMap.values.toList())
            }
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
            spinner.isFocusable = false
            spinner.setSelection(index)
        }

        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayShowTitleEnabled(false)

        recyclerView.setHasFixedSize(false)
        recyclerView.layoutManager = SpannedGridLayoutManager(SpannedGridLayoutManager.GridSpanLookup { position ->
            val key = recyclerAdapter[position]
            SpannedGridLayoutManager.SpanInfo(key.columnSpan, key.rowSpan)
        }, 4, 1f)

        var dataSource: ArrayList<BaseKey> = ArrayList()

        if (sp.getString(SP_KEY_KEYBOARD, "").isNullOrEmpty()) {
            for (i in 0..19) {
                dataSource.add(EmptyKey())
            }
            sp.edit().putString(SP_KEY_KEYBOARD, gson.toJson(dataSource)).apply()
        } else {
            dataSource = gson.fromJson<ArrayList<BaseKey>>(sp.getString(SP_KEY_KEYBOARD, ""), typeBaseKeyArray)
        }

        recyclerAdapter = KeyRecyclerViewAdapter()
        recyclerAdapter.addAllView(dataSource)
        recyclerView.adapter = recyclerAdapter

        recyclerView.addItemDecoration(SpaceItemDecoration(0, 10, 20, 0))

        Log.d(TAG, "spinner.selectedItem = ${spinner.selectedItem as String}")
        KeyTransmitter.run(ipToNameMap.keys.elementAt(ipToNameMap.values.indexOf(spinner.selectedItem as String)), sp.getInt("port", 8080), this)
    }

    override fun onDestroy() {
        super.onDestroy()
        adView.destroy()
        Logger.d(TAG, "onDestroy")
        KeyTransmitter.stop()
    }

    override fun onPause() {
        super.onPause()
        adView.pause()
        Logger.d(TAG, "onPause")
    }

    override fun onResume() {
        super.onResume()
        adView.resume()
        Logger.d(TAG, "onResume")
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
                startActivityForResult(intent, CONFIG_REQUEST_CODE)
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode != Activity.RESULT_OK) {
            return
        }

        when (requestCode) {
            EDIT_REQUEST_CODE -> {
                recyclerAdapter = KeyRecyclerViewAdapter()
                val type = object : TypeToken<ArrayList<BaseKey>>() {}.type
                val gson = GsonBuilder()
                        .registerTypeAdapter(type, KeyDeserializer())
                        .create()
                recyclerAdapter.keyList = gson.fromJson<ArrayList<BaseKey>>(sp.getString(SP_KEY_KEYBOARD, ""), type)
                recyclerView.swapAdapter(recyclerAdapter, false)
            }
            CONFIG_REQUEST_CODE -> {
                val toolbar = findViewById(R.id.toolbar) as Toolbar
                val spinner = toolbar.findViewById(R.id.spinner_name_ip) as Spinner
                val preSelected = spinner.selectedItem as String

                val gson = Gson()
                val json = sp.getString(SP_KEY_IP_MAP, "")
                if (json.isNullOrBlank()) {
                    Logger.d(TAG, "ipMap json is null or empty")
                    val adapter = ArrayAdapter<String>(this, R.layout.spinner_item)
                    val ip = data!!.getStringExtra("ip")
                    if (ip == null) {
                        adapter.add("未接続")
                    } else {
                        KeyTransmitter.restart(ip, sp.getInt("port", 8080))
                    }
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    spinner.adapter = adapter
                } else {
                    Logger.d(TAG, "generate ipMap from json")

                    val ipToNameMap = gson.fromJson<MutableMap<String, String>>(json, object : TypeToken<LinkedHashMap<String, String>>() {}.type)

                    val adapter = ArrayAdapter<String>(this, R.layout.spinner_item)
                    if (ipToNameMap.isEmpty()) {
                        adapter.add("未接続")
                    } else {
                        adapter.addAll(ipToNameMap.values.toList())
                    }
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    spinner.adapter = adapter
                    spinner.isFocusable = false
                    var index: Int
                    val ip = data!!.getStringExtra("ip")
                    if (ip == null) {
                        index = ipToNameMap.values.indexOf(preSelected)
                    } else {
                        index = ipToNameMap.keys.indexOf(ip)
                    }
                    if (index == -1) {
                        index = ipToNameMap.keys.indexOf(sp.getString(SP_KEY_RECENT_DEST, ""))
                        if (index == -1) {
                            index = ipToNameMap.values.indexOfFirst { it.startsWith("未接続") }
                            if (index == -1) {
                                index = 0
                            }
                        }
                    }
                    KeyTransmitter.restart(ipToNameMap.keys.elementAt(index), sp.getInt("port", 8080))
                    spinner.setSelection(index)
                }
            }
        }
    }

    companion object {
        private val EDIT_REQUEST_CODE = 1
        private val CONFIG_REQUEST_CODE = 2
        val SP_KEY_KEYBOARD = "keyboard1"
        val SP_KEY_IP_MAP = "ipToNameMap"
        val SP_KEY_RECENT_DEST = "recentDest"
    }
}

