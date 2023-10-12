package com.example.shoezyseller.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.example.shoezyseller.activity.OrdersDetailsActivity
import com.example.shoezyseller.adapters.AllOrderAdapter
import com.example.shoezyseller.databinding.FragmentOrdersBinding
import com.example.shoezyseller.models.allOrderModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class OrdersFragment : Fragment(),AllOrderAdapter.OnItemClickListener{
    private lateinit var binding: FragmentOrdersBinding
    private lateinit var list : ArrayList<allOrderModel>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentOrdersBinding.inflate(layoutInflater)

        list = ArrayList()

        val preferences = requireContext().getSharedPreferences("userName", AppCompatActivity.MODE_PRIVATE)
        val name = preferences.getString("user",null)

        Firebase.firestore.collection("AllOrders").get()
            .addOnSuccessListener {
                list.clear()
                for (i in it){
                    val data = i.toObject(allOrderModel::class.java)
                    if (name == data.sellerName) {
                        list.add(data)
                    }
                }
                binding.recyc.adapter = AllOrderAdapter(requireContext(),list,this)
            }

        return binding.root
    }

    override fun onItemClick(position: Int, id:String) {
        Log.d("working","its working")
        val intent = Intent(requireContext(),OrdersDetailsActivity::class.java)
        intent.putExtra("id",id)
        startActivity(intent)
    }


}