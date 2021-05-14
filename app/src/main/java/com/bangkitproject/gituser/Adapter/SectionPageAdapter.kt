package com.bangkitproject.gituser

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.bangkitproject.gituser.Entities.Account
import com.bangkitproject.gituser.Fragment.FollowersFragment
import com.bangkitproject.gituser.Fragment.FollowingFragment

class SectionsPagerAdapter(activity: AppCompatActivity,aname :String) : FragmentStateAdapter(activity) {
    private var list: ArrayList<Account> = arrayListOf()
    private var name: String = aname
    override fun getItemCount(): Int {
        return 2
    }
//olah datanya disini lae
    override fun createFragment(position: Int): Fragment {
        var fragment: Fragment? = null

        when (position) {
            0 -> fragment = FollowersFragment(name)
            1 -> fragment = FollowingFragment(name)
        }
        return fragment as Fragment
    }
//    fun refreshList(alist: ArrayList<Account> ,position: Int){
//        list = alist
//        when (position) {
//        0 -> FollowersFragment.refreshList(alist)
//        1 -> FollowersFragment.refreshList(alist)
//    }
//    }

}