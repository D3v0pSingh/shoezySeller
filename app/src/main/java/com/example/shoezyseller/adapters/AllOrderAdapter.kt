package com.example.shoezyseller.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.shoezyseller.R
import com.example.shoezyseller.models.allOrderModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.NonDisposableHandle.parent
import kotlinx.coroutines.cancelFutureOnCompletion
import org.w3c.dom.Text
import java.text.DateFormat
import java.util.*
import kotlin.collections.ArrayList

class AllOrderAdapter(val context: Context, val list:ArrayList<allOrderModel>, val listener: OnItemClickListener):
    Adapter<AllOrderAdapter.viewholder>() {

    inner class viewholder(itemView: View):ViewHolder(itemView){

        val name = itemView.findViewById<TextView>(R.id.productName)
        val price = itemView.findViewById<TextView>(R.id.productPrice)
        val size = itemView.findViewById<TextView>(R.id.sizeText)
        val qty = itemView.findViewById<TextView>(R.id.qtyText)
        val accept = itemView.findViewById<Button>(R.id.statusBtn)
        val cancel = itemView.findViewById<Button>(R.id.cancelBtn)
        val cardView = itemView.findViewById<CardView>(R.id.crdview)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewholder {
        return viewholder(LayoutInflater.from(context).inflate(R.layout.all_orders_layout,parent,false))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: viewholder, position: Int) {
        holder.name.text = list[position].name
        holder.price.text = list[position].price
        holder.size.text = list[position].shoeSize
        holder.qty.text = list[position].quantity
        holder.cardView.setOnClickListener {
            listener.onItemClick(position,list[position].orderId!!)
        }
        holder.cancel.setOnClickListener {
            holder.accept.visibility = View.GONE
            updateStatus("Cancelled", list[position].orderId!!)
        }

        when(list[position].status){
            "ORDERED" -> {
                holder.accept.text = "Dispatch"
                holder.accept.setOnClickListener {
                    updateStatus("Dispatched", list[position].orderId!!)

                }
            }

            "Dispatched" -> {holder.accept.text = "Delivered"
                holder.accept.setOnClickListener {
                    updateStatus("Delivered", list[position].orderId!!)
                    savePrice(list[position].price!!, list[position].name!!, list[position].sellerName!!)
                }
            }

            "Delivered" -> {holder.cancel.visibility = View.GONE
                holder.accept.text = "Delivery Done"
                }

            "Cancelled" -> {holder.accept.visibility = View.GONE}
        }
    }

    private fun savePrice(price: String, name:String, sellerName:String) {
        val calendar = Calendar.getInstance().time
        val date = DateFormat.getDateInstance().format(calendar)

        val data = hashMapOf<String,Any>()
        data["price"] = price
        data["name"] = name
        data["date"] = date
        data["sellerName"] = sellerName
        Firebase.firestore.collection("TotalPrice").add(data)
            .addOnSuccessListener {
                Toast.makeText(context, "Price added to new database", Toast.LENGTH_SHORT).show()
            }
    }

    fun updateStatus(str:String, doc:String ){
        val data = hashMapOf<String,Any>()
        data["status"] = str
        Firebase.firestore.collection("AllOrders").document(doc)
            .update(data).addOnSuccessListener {
                Toast.makeText(context, "Status updated to $str", Toast.LENGTH_SHORT).show()
            }
    }

    interface OnItemClickListener {
        fun onItemClick(position: Int, id:String)
    }
}