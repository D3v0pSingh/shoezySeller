package com.example.shoezyseller.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.example.shoezyseller.R
import com.example.shoezyseller.fragments.addingProductFragment
import com.example.shoezyseller.models.Product
import com.example.shoezyseller.models.productList

//This adapter is for showing all the product added by a particular seller with onCLickListener on each itemView.
class productListAdapter (val context:Context, val list:ArrayList<Product>,
                         private val listener: ONItemClickListener, private val longListener:OnItemLongClickListener):Adapter<productListAdapter.viewholder>() {

    inner class viewholder(itemView:View):ViewHolder(itemView){
        val shoeName: TextView = itemView.findViewById(R.id.nametext)
        val shoePrice: TextView = itemView.findViewById(R.id.pricetext)
        val shoeImage: ImageView = itemView.findViewById(R.id.imageView2)
        val id:TextView = itemView.findViewById(R.id.id)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewholder{
        return viewholder(LayoutInflater.from(context).inflate(R.layout.productitemlist,parent,false))
    }

    override fun getItemCount(): Int {
       return list.size
    }

    override fun onBindViewHolder(holder: viewholder, position: Int) {
        holder.shoeName.text = list[position].productName
        holder.shoePrice.text = list[position].productPrice
        Glide.with(context).load(list[position].productCoverImage).into(holder.shoeImage)
        holder.id.text = list[position].productId

        holder.itemView.setOnClickListener {
            listener.onItemClick(it, list[position])
        }

        holder.itemView.setOnLongClickListener {
            longListener.OnLongClick(it,list[position])
            true
        }
    }

    interface ONItemClickListener{
        fun onItemClick(view: View, item: Product)
    }

    interface OnItemLongClickListener{
        fun OnLongClick(view: View, item:Product)
    }
}