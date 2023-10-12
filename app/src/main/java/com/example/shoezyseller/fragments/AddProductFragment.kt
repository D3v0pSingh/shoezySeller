package com.example.shoezyseller.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import com.example.shoezyseller.R
import com.example.shoezyseller.adapters.addingProductAdapter
import com.example.shoezyseller.adapters.productListAdapter
import com.example.shoezyseller.databinding.FragmentAddProductBinding
import com.example.shoezyseller.models.Product
import com.example.shoezyseller.models.productList
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class AddProductFragment : Fragment(),productListAdapter.ONItemClickListener,productListAdapter.OnItemLongClickListener {

    private lateinit var binding: FragmentAddProductBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAddProductBinding.inflate(layoutInflater)
        //this data1 is not used currently, it represents the category name of the shoes.
        val preferences = requireContext().getSharedPreferences("catName",AppCompatActivity.MODE_PRIVATE)
        val data1 = preferences.getString("name",null)

        //this function fetches data from firebase database if the data is present there.
        getData()

        //This button is used to add new item in the database.
        binding.addBtn.setOnClickListener {
            findNavController().navigate(R.id.action_addProductFragment_to_addingProductFragment)
        }

        return binding.root
    }

    //working of the function is listed here.
    private fun getData() {
        //pref denotes the username of the seller who is adding the product in the database.
        val pref = requireContext().getSharedPreferences("userName",AppCompatActivity.MODE_PRIVATE)
        val name = pref.getString("user",null)
        //preferences is category name that is being added.
        val preferences = requireContext().getSharedPreferences("catName",AppCompatActivity.MODE_PRIVATE)
        val data = preferences.getString("name",null)
        //All info form data is saved in list so that it can be listed as one object.
        val list = ArrayList<Product>()
        //fetching data from firestore with the collection name as category i.e. puma, adidas etc.
        Firebase.firestore.collection(data!!).get()
            .addOnSuccessListener {
                list.clear()
                    for (i in it.documents) {
                        val items = i.toObject(Product::class.java)
                        if (name == items?.sellerName) {
                            list.add(items!!)
                        }
                    }
                binding.recyclerView.adapter = productListAdapter(requireContext(),list,this,this)
        }
    }
//This is an onClickListener to the recyclerView to view all the products added
    override fun onItemClick(view: View, item: Product) {
        Toast.makeText(requireContext(), "This is OnClick", Toast.LENGTH_SHORT).show()
    //this line takes us to next fragment
        findNavController().navigate(R.id.action_addProductFragment_to_addingProductFragment)
    //here we are storing id of the item Clicked so that the information can be fetched in next fragment
        val pref = requireContext().getSharedPreferences("uniqueId",AppCompatActivity.MODE_PRIVATE)
        val editor = pref.edit()
        editor.putString("id",item.productId)
        editor.apply()
    }

    override fun OnLongClick(view: View, item: Product) {
        val popupMenu = PopupMenu(requireContext(),view)
        popupMenu.inflate(R.menu.delete)
        popupMenu.setOnMenuItemClickListener {
            //pref denotes the username of the seller who is adding the product in the database.
            val pref = requireContext().getSharedPreferences("userName",AppCompatActivity.MODE_PRIVATE)
            val name = pref.getString("user",null)
            //preferences is category name that is being added.
            val preferences = requireContext().getSharedPreferences("catName",AppCompatActivity.MODE_PRIVATE)
            val data = preferences.getString("name",null)
            //All info form data is saved in list so that it can be listed as one object.
            //fetching data from firestore with the collection name as category i.e. puma, adidas etc.
            Firebase.firestore.collection(data!!).document(item.productId!!).delete()
                .addOnSuccessListener {
                    Toast.makeText(requireContext(), "Item deleted. CONGRATULATIONS", Toast.LENGTH_SHORT).show()
                }
            true
        }
        popupMenu.show()
    }

}