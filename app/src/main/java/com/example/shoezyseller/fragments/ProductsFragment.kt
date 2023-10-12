package com.example.shoezyseller.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import com.example.shoezyseller.R
import com.example.shoezyseller.databinding.FragmentProductsBinding


class ProductsFragment : Fragment() {
    private lateinit var binding:FragmentProductsBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentProductsBinding.inflate(layoutInflater)

        val preferences = requireContext().getSharedPreferences("catName",AppCompatActivity.MODE_PRIVATE)
        val editor = preferences.edit()

        binding.nike.setOnClickListener {
            findNavController().navigate(R.id.action_productsFragment_to_addProductFragment)
            editor.putString("name","nike")
            editor.apply()
        }

        binding.adidas.setOnClickListener {
            findNavController().navigate(R.id.action_productsFragment_to_addProductFragment)
            editor.putString("name","adidas")
            editor.apply()
        }

        binding.reebok.setOnClickListener {
            findNavController().navigate(R.id.action_productsFragment_to_addProductFragment)
            editor.putString("name","reebok")
            editor.apply()
        }

        binding.puma.setOnClickListener {
            findNavController().navigate(R.id.action_productsFragment_to_addProductFragment)
            editor.putString("name","puma")
            editor.apply()
        }

        return binding.root
    }
}