package com.bangkitproject.gituser

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bangkitproject.gituser.Adapter.SavedDataAdapter
import com.bangkitproject.gituser.DB.DataHelper
import com.bangkitproject.gituser.Helper.MappingHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch


class FavActivity : AppCompatActivity() {

    private lateinit var adapter: SavedDataAdapter
    private lateinit var progessbar: ProgressBar
    private lateinit var rv_fav: RecyclerView
    private lateinit var empty: ConstraintLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fav)

        actionBar?.setDisplayHomeAsUpEnabled(true);

        progessbar = findViewById(R.id.progressBar_fav)
        empty = findViewById(R.id.Empty)
        rv_fav = findViewById(R.id.recyclerFav)
        adapter = SavedDataAdapter(this)

        rv_fav.layoutManager = LinearLayoutManager(this)
        rv_fav.setHasFixedSize(true)
        rv_fav.adapter = adapter

        empty.visibility = View.INVISIBLE

        loadNotesAsync()
    }

    private fun loadNotesAsync() {
        GlobalScope.launch(Dispatchers.Main) {
            progessbar.visibility = View.VISIBLE
            val gitHelper = DataHelper.getInstance(applicationContext)
            gitHelper.open()
            val defferedNotes = async(Dispatchers.IO) {
                val cursor = gitHelper.queryAll()
                MappingHelper.mapCursorToArrayList(cursor)
            }
            val TotalData = defferedNotes.await()
            progessbar.visibility = View.INVISIBLE

            if (TotalData.size >0) {
                adapter.listSaved= TotalData
            } else {
                adapter.listSaved = ArrayList()
                empty.visibility = View.VISIBLE
            }

        }
    }

    override fun onRestart() {
        var action = intent.action
        if(action==null || !action.equals("Already Created")){
            val new = Intent(this, FavActivity::class.java)
            startActivity(new)
            finish()
        }else{
            intent.setAction(null)
        }
        super.onRestart()
    }


}