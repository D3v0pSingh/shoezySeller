package com.example.shoezyseller.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.example.shoezyseller.R
import com.example.shoezyseller.adapters.EarningAdapter
import com.example.shoezyseller.databinding.FragmentEarningBinding
import com.example.shoezyseller.models.earningsModelClass
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class EarningFragment : Fragment() {
private lateinit var binding:FragmentEarningBinding
private lateinit var list:ArrayList<earningsModelClass>
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEarningBinding.inflate(layoutInflater)
        list = ArrayList()

        val preferences = requireContext().getSharedPreferences("userName", AppCompatActivity.MODE_PRIVATE)
        val name = preferences.getString("user",null)

        Firebase.firestore.collection("TotalPrice").get()
            .addOnSuccessListener {
                list.clear()
                for (i in it){
                    val data = i.toObject(earningsModelClass::class.java)
                    if (name == data.sellerName) {
                        list.add(data)
                    }
                }
                binding.recycl.adapter = EarningAdapter(requireContext(),list)
            }

        return binding.root
    }
}
