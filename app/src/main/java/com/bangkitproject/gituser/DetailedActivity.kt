package com.bangkitproject.gituser



import android.content.ContentValues
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.bangkitproject.gituser.DB.DataHelper
import com.bangkitproject.gituser.DB.DatabaseContract.UserColumns.Companion.AVATAR
import com.bangkitproject.gituser.DB.DatabaseContract.UserColumns.Companion.COMPANY

import com.bangkitproject.gituser.DB.DatabaseContract.UserColumns.Companion.FOLLOWERS
import com.bangkitproject.gituser.DB.DatabaseContract.UserColumns.Companion.FOLLOWINGS
import com.bangkitproject.gituser.DB.DatabaseContract.UserColumns.Companion.LOCATION
import com.bangkitproject.gituser.DB.DatabaseContract.UserColumns.Companion.NAME
import com.bangkitproject.gituser.DB.DatabaseContract.UserColumns.Companion.REPOSITORY
import com.bangkitproject.gituser.DB.DatabaseContract.UserColumns.Companion.STATUS
import com.bangkitproject.gituser.DB.DatabaseContract.UserColumns.Companion.USERNAME
import com.bangkitproject.gituser.Entities.Account
import com.bangkitproject.gituser.Entities.Favourite
import com.bangkitproject.gituser.Helper.MappingHelper
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch


class DetailedActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var name: TextView
    private lateinit var username: TextView
    private lateinit var company: TextView
    private lateinit var location: TextView
    private lateinit var repository: TextView
    private lateinit var following: TextView
    private lateinit var followers: TextView
    private lateinit var imgPhoto: CircleImageView
    private lateinit var btnfav: ImageButton
    private lateinit var temppicture: String
    private var isFavorite: Boolean = false
    private var favdata: Favourite? = null

    private lateinit var gitHelper: DataHelper
    companion object {
        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.tab_text_1,
            R.string.tab_text_2
        )
        const val EXTRA_DATAS = "this is the place" //data from githubjson
        const val EXTRA_NOTE = "extra_note" //data from fav
        const val EXTRA_POSITION = "extra_position"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.detailed_layout)

        actionBar?.setDisplayHomeAsUpEnabled(true);

        name = findViewById(R.id.detailNama)
        username = findViewById(R.id.detailUsername)
        company= findViewById(R.id.detailCompany)
        location= findViewById(R.id.detailLocation)
        repository = findViewById(R.id.detailRepository)
        following= findViewById(R.id.detailFollowing)
        followers= findViewById(R.id.detailFollowers)

        btnfav = findViewById(R.id.fav_add)
        btnfav.setOnClickListener(this)

        gitHelper = DataHelper.getInstance(applicationContext)
        gitHelper.open()

        imgPhoto= findViewById(R.id.detailAvatar)

        favdata = intent.getParcelableExtra(EXTRA_NOTE) as Favourite?
        if (favdata != null) {
            SetFavData()
            isFavorite = true

        }
        else {
            setData()
        }
        ShowViewPager(username.text as String)
        setstatus()
    }

    private fun SetFavData() {
        name.text = favdata?.name
        username.text = favdata?.username
        company.text = favdata?.company
        location.text = favdata?.location
        repository.text = favdata?.repository
        following.text = favdata?.following
        followers.text = favdata?. followers


        Glide.with(this)
            .load(favdata?.avatar)
            .into(imgPhoto)

    }

    private fun setData() {
        var listdata = intent.getParcelableExtra(EXTRA_DATAS) as Account?
        name.text = listdata?.name
        username.text = listdata?.username
        company.text = listdata?.company
        location.text = listdata?.location
        repository.text = "${listdata?.repository}  total repo"
        following.text = "${listdata?.following} following"
        followers.text = "${listdata?. followers}  followers"

        this.title = listdata?.username

        temppicture = listdata?.avatar.toString()
        Glide.with(this)
            .load(listdata?.avatar)
            .into(imgPhoto)
    }

    private fun ShowViewPager(name: String) {
        val sectionsPagerAdapter = SectionsPagerAdapter(this,name)
        val viewPager: ViewPager2 = findViewById(R.id.viewPager)
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = findViewById(R.id.tabLayout)
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()

        supportActionBar?.elevation = 0f


    }

    fun setstatus(){
        GlobalScope.launch(Dispatchers.Main) {
        val defferedNotes = async(Dispatchers.IO) {
            val cursor = gitHelper.queryById(username.text as String)
            MappingHelper.mapCursorToArrayList(cursor)
        }
            val TotalData = defferedNotes.await()
            if (TotalData.size >0) {
                btnfav.setImageResource(R.drawable.ic_baseline_favorite_24)
            } else {
                btnfav.setImageResource(R.drawable.ic_baseline_favorite_border_24)
            }

        }
    }

    override fun onClick(v: View?) {
        if (v?.id == R.id.fav_add){
            if (!isFavorite){
                val img = temppicture
                val Name = name.text.toString()
                val Username = username.text.toString()
                val Company = company.text.toString()
                val Location = location.text.toString()
                val Repository = repository.text.toString()
                val Followers = followers.text.toString()
                val Following = following.text.toString()
                val dataFavorite = "1"
                val values = ContentValues()
                    values.put(NAME, Name)
                    values.put(USERNAME, Username)
                    values.put(AVATAR, img)
                    values.put(COMPANY, Company)
                    values.put(LOCATION, Location)
                    values.put(REPOSITORY, Repository)
                    values.put(FOLLOWERS, Followers)
                    values.put(FOLLOWINGS, Following)
                    values.put(STATUS, dataFavorite)
                isFavorite = true
                gitHelper.insert(values)
                Toast.makeText(this, "Berhasil ditambahkan", Toast.LENGTH_SHORT).show()
            }
            else{
                gitHelper.deleteById(favdata?.username.toString())
                    Toast.makeText(this, "Berhasil dihapuskan", Toast.LENGTH_SHORT).show()

                            isFavorite = false
            }
            setstatus()
        }
    }
}