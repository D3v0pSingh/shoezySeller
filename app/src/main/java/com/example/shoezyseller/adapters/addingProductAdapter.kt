package com.example.shoezyseller.adapters

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.shoezyseller.R

class addingProductAdapter(val context: Context, val list:ArrayList<Uri>):Adapter<addingProductAdapter.viewHolder>() {

    inner class viewHolder(itemView: View): ViewHolder(itemView){
        val ImageView:ImageView = itemView.findViewById(R.id.imageView3)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
        return viewHolder(LayoutInflater.from(context).inflate(R.layout.productimagesitemlist,parent,false))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: viewHolder, position: Int) {
        holder.ImageView.setImageURI(list[position])
    }
}