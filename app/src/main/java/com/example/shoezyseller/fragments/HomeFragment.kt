package com.example.shoezyseller.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import com.example.shoezyseller.R
import com.example.shoezyseller.databinding.FragmentHomeBinding
import com.example.shoezyseller.models.earningsModelClass
import com.example.shoezyseller.models.sellers
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase


class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var list: ArrayList<String>
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(layoutInflater)

        getUserName()

        list = ArrayList()

        val preferences =
            requireContext().getSharedPreferences("userName", AppCompatActivity.MODE_PRIVATE)
        val name = preferences.getString("user", null)

        Firebase.firestore.collection("TotalPrice").get()
            .addOnSuccessListener {
                list.clear()
                var sum = 0
                for (i in it) {
                    val data1 = i.toObject(earningsModelClass::class.java)
                    if (name == data1.sellerName) {
                        sum += data1.price?.toInt()!!
                    }
                }
                binding.textView2.text = "$  ${sum.toString()}"

            }

        binding.myaccount.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_profileFragment)
        }

        binding.earning.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_earningFragment)
        }

        binding.incoming.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_ordersFragment)
        }

        binding.products.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_productsFragment)
        }

        binding.apply {
            cardView.setOnClickListener {
                findNavController().navigate(R.id.action_homeFragment_to_profileFragment)
            }
            cardView2.setOnClickListener {
                findNavController().navigate(R.id.action_homeFragment_to_ordersFragment)

            }
            cardView3.setOnClickListener {
                findNavController().navigate(R.id.action_homeFragment_to_earningFragment)
            }
            cardView4.setOnClickListener {
                findNavController().navigate(R.id.action_homeFragment_to_productsFragment)
            }
        }

        return binding.root
    }

    private fun getUserName() {
        val preferences =
            requireContext().getSharedPreferences("userName", AppCompatActivity.MODE_PRIVATE)
        val name = preferences.getString("user", null)
        if (name != null) {
            Firebase.firestore.collection("sellerProfile").document(name)
                .get().addOnSuccessListener {
                    val data = it.toObject<sellers>()
                    binding.profileName.text = "${data?.name}!"
                    // Glide.with(requireContext()).load(data!!.profileImage).into(binding.profileImage)

                }
        }
    }

}