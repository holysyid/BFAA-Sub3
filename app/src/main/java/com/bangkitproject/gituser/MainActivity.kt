package com.bangkitproject.gituser

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ProgressBar

import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bangkitproject.gituser.Adapter.AccountAdapter
import com.bangkitproject.gituser.Entities.Account

import com.loopj.android.http.*
import cz.msebera.android.httpclient.Header

import org.json.JSONArray
import org.json.JSONObject

import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {

    private var list: ArrayList<Account> = arrayListOf()
    private lateinit var rvAccount: RecyclerView
    private lateinit var progressBar: ProgressBar
    private var coba: String? = null

    companion object {
        private val TAG = MainActivity::class.java.simpleName
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        progressBar = findViewById(R.id.progressBar)
        rvAccount = findViewById(R.id.recyclerView)
        rvAccount.setHasFixedSize(true)

        getData(coba)


    }



    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.option_menu, menu)

     return super.onCreateOptionsMenu(menu)
    }

    private fun getData(query: String?) {
        progressBar.visibility = View.VISIBLE

        //api config
        val client = AsyncHttpClient()
        client.addHeader("User-Agent", "request")
        client.addHeader("Authorization", BuildConfig.GITHUB_TOKEN)
        if (query.isNullOrEmpty()){
            val url = "https://api.github.com/users"

            client.get(url, object : AsyncHttpResponseHandler() {
                override fun onSuccess(statusCode: Int, headers: Array<Header>, responseBody: ByteArray) {

                    progressBar.visibility = View.INVISIBLE //

                    var result = String(responseBody)
                    //datanya ada ga
                    Log.d(TAG, result!!)
                    try {
                        val jsonArray = JSONArray(result)

                        for (i in 0 until jsonArray.length()) {
                            val jsonObject = jsonArray.getJSONObject(i)
                            val key: String = jsonObject.getString("login")

                            getDetailedData(key)
                        }
                    }

                    catch (e: Exception) {
                        Toast.makeText(this@MainActivity, e.message, Toast.LENGTH_SHORT).show()
                        e.printStackTrace()
                    }

                }

                override fun onFailure(statusCode: Int, headers: Array<Header>, responseBody: ByteArray, error: Throwable) {
                    // if not ok, like ur heart :(
                    progressBar.visibility = View.INVISIBLE
                    var result = String(responseBody)
                    val errorMessage = when (statusCode) {
                        401 -> "$statusCode : Bad Request"
                        403 -> "$statusCode : Forbidden"
                        404 -> "$statusCode : Not Found"
                        else -> "$statusCode : ${error.message}"
                    }
                    Log.d(TAG, result!!)
                    Toast.makeText(this@MainActivity, errorMessage, Toast.LENGTH_SHORT).show()
                }
            })
        }else{
            val url = "https://api.github.com/search/users?q=$query"

            client.get(url, object : AsyncHttpResponseHandler() {
                override fun onSuccess(statusCode: Int, headers: Array<Header>, responseBody: ByteArray) {
                    // if ok
                    progressBar.visibility = View.INVISIBLE //

                    val result = String(responseBody)
                    //datanya ada ga
                    Log.d(TAG, result)

                    try {
                        val jsonArray = JSONObject(result)
                        val items = jsonArray.getJSONArray("items")
                        for (i in 0 until jsonArray.length()) {
                            val jsonObject = items.getJSONObject(i)
                            val key: String = jsonObject.getString("login")

                            getDetailedData(key)
                        }
                    }

                    catch (e: Exception) {
                        Toast.makeText(this@MainActivity, e.message, Toast.LENGTH_SHORT).show()
                        e.printStackTrace()
                    }

                }
                override fun onFailure(statusCode: Int, headers: Array<Header>, responseBody: ByteArray, error: Throwable) {
                    // if not ok, like ur heart :(
                    progressBar.visibility = View.INVISIBLE

                    val errorMessage = when (statusCode) {
                        401 -> "$statusCode : Bad Request"
                        403 -> "$statusCode : Forbidden"
                        404 -> "$statusCode : Not Found"
                        else -> "$statusCode : ${error.message}"
                    }
                    Toast.makeText(this@MainActivity, errorMessage, Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

    private fun getDetailedData(key: String) {

        progressBar.visibility = View.VISIBLE

        val client = AsyncHttpClient()
        client.addHeader("User-Agent", "request")
        client.addHeader("Authorization", BuildConfig.GITHUB_TOKEN)
        val url = "https://api.github.com/users/$key"

        client.get(url, object : AsyncHttpResponseHandler(){
            override fun onSuccess(statusCode: Int, headers: Array<Header>, responseBody: ByteArray) {

                progressBar.visibility = View.INVISIBLE
                val result = String(responseBody)
                Log.d(TAG, result)

                try {
                    val userDetail = JSONObject(result)
                    val user = Account(
                        userDetail.getString("login").toString(),
                        userDetail.getString("name").toString(),
                        userDetail.getString("avatar_url").toString(),
                        userDetail.getString("company").toString(),
                        userDetail.getString("location").toString(),
                        userDetail.getString("public_repos").toString(),
                        userDetail.getString("followers").toString(),
                        userDetail.getString("following").toString()
                    )
                    list.add(user)
                    rvAccount.layoutManager = LinearLayoutManager(this@MainActivity)
                    val listHeroAdapter = AccountAdapter(this@MainActivity,list)
                    rvAccount.adapter = listHeroAdapter
                }

                catch (e: Exception) {
                    Toast.makeText(this@MainActivity, e.message, Toast.LENGTH_SHORT).show()
                    e.printStackTrace()
                }

            }override fun onFailure(statusCode: Int, headers: Array<Header>, responseBody: ByteArray, error: Throwable) {
                progressBar.visibility = View.INVISIBLE
                val errorMessage = when (statusCode) {
                    401 -> "$statusCode : Bad Request"
                    403 -> "$statusCode : Forbidden"
                    404 -> "$statusCode : Not Found"
                    else -> "$statusCode : ${error.message}"
                }
                Toast.makeText(this@MainActivity, errorMessage, Toast.LENGTH_LONG)
                    .show()
            }



        })

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId){
        R.id.search -> {
            if (item != null) {
                val searchView = item.actionView as SearchView
                searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                    override fun onQueryTextSubmit(query: String?): Boolean {
                        if (query!!.isEmpty()) {

                        } else {
                            list.clear()
                            getData(query)
                            rvAccount.layoutManager = LinearLayoutManager(this@MainActivity)
                            val listHeroAdapter = AccountAdapter(this@MainActivity, list)
                            rvAccount.adapter = listHeroAdapter
                        }
                        return true
                    }

                    override fun onQueryTextChange(newText: String?): Boolean {

                        if (newText!!.isEmpty()) {

                            return true


                        } else {
                            list.clear()
                            getData(newText)
                            rvAccount.layoutManager = LinearLayoutManager(this@MainActivity)
                            val listHeroAdapter = AccountAdapter(this@MainActivity, list)
                            rvAccount.adapter = listHeroAdapter

                            return true
                        }
                    }


                })
            }
        }
            R.id.fav ->{
                val intent = Intent(this, FavActivity::class.java)
                startActivity(intent)

            }
            R.id.setting ->{
                val intent = Intent(this, SettingActivity::class.java)
                startActivity(intent)

            }
        }
        return super.onOptionsItemSelected(item)
    }


}