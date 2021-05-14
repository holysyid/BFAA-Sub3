package com.bangkitproject.gituser.Adapter

import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bangkitproject.gituser.Listener.CustomOnItemClickListener
import com.bangkitproject.gituser.DB.DataHelper
import com.bangkitproject.gituser.DetailedActivity
import com.bangkitproject.gituser.Entities.Favourite
import com.bangkitproject.gituser.R
import com.bumptech.glide.Glide
import de.hdodenhof.circleimageview.CircleImageView

class SavedDataAdapter (private val activity: Activity) :
    RecyclerView.Adapter<SavedDataAdapter.MyViewHolder>() {

    private lateinit var gitHelper: DataHelper

    var listSaved = ArrayList<Favourite>()
        set(listAccount) {
        if (listAccount.size > 0) {
            this.listSaved.clear()
        }
        this.listSaved.addAll(listAccount)
        notifyDataSetChanged()
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var name: TextView = itemView.findViewById<View>(R.id.tv_itemName_fav) as TextView
        var username: TextView = itemView.findViewById<View>(R.id.tv_itemDetail_fav) as TextView
        var company: TextView = itemView.findViewById<View>(R.id.tv_itemDetail2_fav) as TextView
        var imgPhoto: CircleImageView = itemView.findViewById(R.id.img_item_photo_fav)
        var deletebtn: ImageButton = itemView.findViewById(R.id.deletethis)


        fun bind(fav: Favourite){
            with(itemView){
                name.text = fav.name
                username.text = fav.username
                company.text = fav.company

                Glide.with(itemView.context)
                    .load(fav.avatar)
                    .into(imgPhoto)


                itemView.setOnClickListener(
                    CustomOnItemClickListener(
                        adapterPosition,
                        object : CustomOnItemClickListener.OnItemClickCallback {
                            override fun onItemClicked(view: View, position: Int) {
                                val intent = Intent(activity, DetailedActivity::class.java)
                                intent.putExtra(DetailedActivity.EXTRA_POSITION, position)
                                intent.putExtra(DetailedActivity.EXTRA_NOTE, fav)
                                activity.startActivity(intent)
                            }
                        }
                    )
                )
                deletebtn.setOnClickListener{
                    AlertDialog.Builder(context)
                        // Judul
                        .setTitle("Hapus Data Tersimpan")
                        // Pesan yang di tamopilkan
                        .setMessage("Apa kamu yakin ingin menghapus data ini?")
                        .setPositiveButton("hapus", DialogInterface.OnClickListener { dialogInterface, i ->
                            gitHelper.deleteById(fav.username!!)
                            activity.recreate()
                            Toast.makeText(context, "Berhasil dihapus", Toast.LENGTH_SHORT).show()
                        })
                        .setNegativeButton("tidak", DialogInterface.OnClickListener { dialogInterface, i ->
                            Toast.makeText(context, "Anda memilih tombol tidak", Toast.LENGTH_LONG).show()
                        })
                        .show()
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.row_fav, parent, false)
        gitHelper = DataHelper.getInstance(activity)
        gitHelper.open()
        return MyViewHolder(v)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        holder.bind(listSaved[position])
    }

    override fun getItemCount(): Int {
        return listSaved.size
    }
}