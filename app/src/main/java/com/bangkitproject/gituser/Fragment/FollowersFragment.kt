package com.bangkitproject.gituser.Fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bangkitproject.gituser.Adapter.FragmentAdapter
import com.bangkitproject.gituser.BuildConfig
import com.bangkitproject.gituser.Entities.Account
import com.bangkitproject.gituser.R
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONArray
import org.json.JSONObject

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class FollowersFragment(name:String) : Fragment() {
    private lateinit var rvAccount: RecyclerView
    private var coba1: String? = name
    private lateinit var progressBar2: ProgressBar
    companion object {
        private var list: ArrayList<Account> = arrayListOf()

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootview = inflater.inflate(R.layout.fragment_followers, container, false)

        FollowersFragment.list.clear()
        progressBar2 = rootview.findViewById(R.id.progressBarer)
        getvalData(coba1)
        rvAccount = rootview.findViewById(R.id.rvfollowers)
//                rvAccount.setHasFixedSize(true)
        rvAccount.setHasFixedSize(true);
        rvAccount.layoutManager = LinearLayoutManager(context)

        return rootview
    }

    private fun getvalData(query: String?) {
        progressBar2.visibility = View.VISIBLE

        //api config
        val client = AsyncHttpClient()
        client.addHeader("User-Agent", "request")
        client.addHeader("Authorization",  BuildConfig.GITHUB_TOKEN)
            val url = "https://api.github.com/users/${query}/followers"
            var result: String? = null
            client.get(url, object : AsyncHttpResponseHandler() {
                override fun onSuccess(statusCode: Int, headers: Array<Header>, responseBody: ByteArray) {
                    // if ok
                    progressBar2.visibility = View.INVISIBLE //

                    result = String(responseBody)
                    //datanya ada ga



                }

                override fun onFinish() {
                    try {
                        val jsonArray = JSONArray(result)
                        for (i in 0 until jsonArray.length()) {
                            val jsonObject = jsonArray.getJSONObject(i)
                            val key: String = jsonObject.getString("login")

                            getDetailedData(key)
                        }
                    }

                    catch (e: Exception) {
                        Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
                        e.printStackTrace()
                    }
                }
                override fun onFailure(statusCode: Int, headers: Array<Header>, responseBody: ByteArray, error: Throwable) {
                    // if not ok, like ur heart :(
                    progressBar2.visibility = View.INVISIBLE

                    val errorMessage = when (statusCode) {
                        401 -> "$statusCode : Bad Request"
                        403 -> "$statusCode : Forbidden"
                        404 -> "$statusCode : Not Found"
                        else -> "$statusCode : ${error.message}"
                    }
                    Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
                }
            })

    }

    private fun getDetailedData(key: String) {

        progressBar2.visibility = View.VISIBLE

        val client = AsyncHttpClient()
        client.addHeader("User-Agent", "request")
        client.addHeader("Authorization",  BuildConfig.GITHUB_TOKEN)
        val url = "https://api.github.com/users/$key"

        client.get(url, object : AsyncHttpResponseHandler(){
            override fun onSuccess(statusCode: Int, headers: Array<Header>, responseBody: ByteArray) {

                progressBar2.visibility = View.INVISIBLE
                val result = String(responseBody)


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

                }

                catch (e: Exception) {
                    Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
                    e.printStackTrace()
                }

            }override fun onFailure(statusCode: Int, headers: Array<Header>, responseBody: ByteArray, error: Throwable) {
                progressBar2.visibility = View.INVISIBLE
                val errorMessage = when (statusCode) {
                    401 -> "$statusCode : Bad Request"
                    403 -> "$statusCode : Forbidden"
                    404 -> "$statusCode : Not Found"
                    else -> "$statusCode : ${error.message}"
                }
                Toast.makeText(context, errorMessage, Toast.LENGTH_LONG)
                    .show()
            }

            override fun onFinish() {
                val listAccAdapter = FragmentAdapter(list)
                rvAccount.adapter = listAccAdapter

            }

        })

    }



}