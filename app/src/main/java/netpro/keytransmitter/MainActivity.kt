package netpro.keytransmitter

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.util.Base64
import android.view.Menu
import android.view.MenuItem
import io.plaidapp.ui.recyclerview.SpannedGridLayoutManager
import java.io.*
import java.util.*

class MainActivity : AppCompatActivity() {
    private lateinit var recyclerAdapter: KeyRecyclerViewAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var keyStoreManager: RSAManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar = findViewById(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)

        recyclerView = findViewById(R.id.recyclerview) as RecyclerView
        recyclerView.setHasFixedSize(false)
        recyclerView.layoutManager = SpannedGridLayoutManager(SpannedGridLayoutManager.GridSpanLookup { position ->
            val key = recyclerAdapter[position]
            SpannedGridLayoutManager.SpanInfo(key.columnSpan, key.rowSpan)
        }, 4, 1f)

        var datasource: MutableList<BaseKey> = ArrayList()
        val dir = filesDir
        val filePath = dir.absolutePath + File.separator + "keyboard"
        val file = File(filePath)
        if (file.exists()) {
            try {
                val input = ObjectInputStream(FileInputStream(file))
                datasource = input.readObject() as MutableList<BaseKey>
            } catch (e: IOException) {
                e.printStackTrace()
            } catch (e: ClassNotFoundException) {
                e.printStackTrace()
            }
        } else {
            for (i in 0..19) {
                datasource.add(EmptyKey())
            }
            try {
                val out = ObjectOutputStream(FileOutputStream(filePath))
                out.writeObject(datasource)
                out.flush()
                out.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        recyclerAdapter = KeyRecyclerViewAdapter()
        recyclerAdapter.addAllView(datasource)
        recyclerView.adapter = recyclerAdapter

        recyclerView.addItemDecoration(SpaceItemDecoration(0, 15, 20, 0))

        val sp = PreferenceManager.getDefaultSharedPreferences(applicationContext)
        keyStoreManager = RSAManager.getInstance(applicationContext)
        val cipherText = sp.getString("pass", "")
        var plainPass:String
        if(cipherText.isNullOrEmpty()){
            plainPass = ""
        } else {
            plainPass = String(keyStoreManager.decrypt(Base64.decode(cipherText, Base64.DEFAULT)))
        }
        KeyTransmitter.init(sp.getString("ip", null), sp.getInt("port", 8080), plainPass)
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
            val dir = filesDir
            val filePath = dir.absolutePath + File.separator + "keyboard"
            val file = File(filePath)
            if (file.exists()) {
                try {
                    val input = ObjectInputStream(FileInputStream(file))
                    val keyList = input.readObject() as MutableList<BaseKey>
                    recyclerAdapter = KeyRecyclerViewAdapter()
                    recyclerAdapter.keyList = keyList
                    recyclerView.swapAdapter(recyclerAdapter, false)
                } catch (e: IOException) {
                    e.printStackTrace()
                } catch (e: ClassNotFoundException) {
                    e.printStackTrace()
                }

            }
        }
    }

    companion object {
        private val EDIT_REQUEST_CODE = 1
    }
}

