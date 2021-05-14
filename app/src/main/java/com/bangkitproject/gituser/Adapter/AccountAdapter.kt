package com.bangkitproject.gituser.Adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bangkitproject.gituser.Entities.Account
import com.bangkitproject.gituser.DetailedActivity
import com.bangkitproject.gituser.R
import com.bumptech.glide.Glide
import de.hdodenhof.circleimageview.CircleImageView

class AccountAdapter(
    private val context: Context,
    private val listAccount: ArrayList<Account>
) :
RecyclerView.Adapter<AccountAdapter.MyViewHolder>() {



    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var name: TextView = itemView.findViewById<View>(R.id.tv_itemName) as TextView
        var username: TextView = itemView.findViewById<View>(R.id.tv_itemDetail) as TextView
        var company: TextView = itemView.findViewById<View>(R.id.tv_itemDetail2) as TextView
        var imgPhoto: CircleImageView = itemView.findViewById(R.id.img_item_photo)

    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.row_layout, parent, false)

        return MyViewHolder(v)
    }
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val acc = listAccount[position]
        holder.name.text = acc.name
        holder.username.text = acc.username
        holder.company.text = acc.company

        Glide.with(holder.itemView.context)
            .load(acc.avatar)
            .into(holder.imgPhoto)


        holder.itemView.setOnClickListener {
            Toast.makeText(context, acc.name, Toast.LENGTH_SHORT).show()

            val context= holder.itemView.context

            val DetailActivity = Intent(context, DetailedActivity::class.java)
            DetailActivity.putExtra(DetailedActivity.EXTRA_DATAS, acc)
            context.startActivity(DetailActivity)
        }
    }

    override fun getItemCount(): Int {
        return listAccount.size
    }



}