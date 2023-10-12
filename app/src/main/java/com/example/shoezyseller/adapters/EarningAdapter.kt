package com.example.shoezyseller.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.shoezyseller.R

import com.example.shoezyseller.models.earningsModelClass

class EarningAdapter(val context: Context, val list:ArrayList<earningsModelClass>):Adapter<EarningAdapter.viewholder>() {

    inner class viewholder( itemView: View): ViewHolder(itemView){
        val date = itemView.findViewById<TextView>(R.id.date)
        val pname = itemView.findViewById<TextView>(R.id.pnametext)
        val price = itemView.findViewById<TextView>(R.id.ppricetext)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewholder {
        return viewholder(LayoutInflater.from(context).inflate(R.layout.earnings_item,parent,false))
    }

    override fun getItemCount(): Int {
        return list.count()
    }

    override fun onBindViewHolder(holder: viewholder, position: Int) {
        holder.date.text = list[position].date
        holder.pname.text = list[position].name
        holder.price.text = list[position].price
    }
}