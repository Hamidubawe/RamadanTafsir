package com.arewabeatz.ramadantafsir2021.Adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.arewabeatz.ramadantafsir2021.MalamActivity
import com.arewabeatz.ramadantafsir2021.R
import com.arewabeatz.ramadantafsir2021.UploadActivity

class MalamAdapter(private val context: Context, private var name: Array<String>, private var photo: IntArray):
    RecyclerView.Adapter<MalamAdapter.Holder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {

        val v = LayoutInflater.from(context).inflate(R.layout.malam_layout, parent, false)
        return Holder(v)

    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val malam = name[position]
        val malamPhoto = photo[position]

        holder.malamName.text = malam
        holder.malamPhoto.setImageResource(malamPhoto)

        holder.itemView.setOnClickListener {

            val i = Intent(context, MalamActivity::class.java)
            i.putExtra("malam", malam)
            context.startActivity(i)
        }

        holder.itemView.setOnLongClickListener {
            val i = Intent(context, UploadActivity::class.java)
            context.startActivity(i)

            true
        }
    }

    override fun getItemCount(): Int {
        return name.size
    }

    class Holder(itemView: View) : RecyclerView.ViewHolder(itemView){

        val malamName: TextView = itemView.findViewById(R.id.albumName)
        val malamPhoto: ImageView = itemView.findViewById(R.id.albumImage)

    }
}