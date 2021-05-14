package com.bangkitproject.gituser.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bangkitproject.gituser.Entities.Account
import com.bangkitproject.gituser.R
import com.bumptech.glide.Glide
import de.hdodenhof.circleimageview.CircleImageView

class FragmentAdapter(listUser: ArrayList<Account>): RecyclerView.Adapter<FragmentAdapter.FragmentViewHolder>()  {
    val x = listUser
    class FragmentViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        var userAvatar: CircleImageView = itemView.findViewById(R.id.img_item_photo_fragment)
        var userName: TextView = itemView.findViewById(R.id.tv_itemName_fragment)
        var userusername: TextView = itemView.findViewById(R.id.tv_itemDetail_fragment)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FragmentViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.row_fragment, parent, false)

        return FragmentViewHolder(v)
    }

    override fun onBindViewHolder(holder: FragmentViewHolder, position: Int) {

        val acc = x[position]
        holder.userName.text = acc.name
        holder.userusername.text = acc.username


        Glide.with(holder.itemView.context)
            .load(acc.avatar)
            .into(holder.userAvatar)



    }

    override fun getItemCount(): Int {
        return x.size
    }
}