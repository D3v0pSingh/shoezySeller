package com.example.shoezyseller.fragments

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import androidx.fragment.app.Fragment
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.shoezyseller.R
import com.example.shoezyseller.adapters.addingProductAdapter
import com.example.shoezyseller.databinding.FragmentAddingProductBinding
import com.example.shoezyseller.models.Product
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.log

class addingProductFragment : Fragment() {
    private lateinit var binding: FragmentAddingProductBinding
    private lateinit var list: ArrayList<Uri>
    private lateinit var listImages: ArrayList<String>
    private lateinit var adapter: addingProductAdapter
    private var listSize = ArrayList<String>()
    private var coverImage: Uri? = null
    private lateinit var dialog: Dialog
    private var coverImageUrl: String? = null
    private var category: String? = null
    private lateinit var listing: ArrayList<String>
    private lateinit var productSize: ArrayList<String>
    private var trending: Boolean = false

    //This is for displaying cover image of the product
    private var launchGalleryActivity = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == Activity.RESULT_OK) {
            coverImage = it.data!!.data
            binding.coverImage.setImageURI(coverImage)
            binding.coverpictext.visibility = View.GONE
        }
    }

    //This is for displaying extra images of the product
    private var launchProductActivity = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == Activity.RESULT_OK) {
            val images = it.data!!.data
            list.add(images!!)
            adapter.notifyDataSetChanged()
        }
    }

    @SuppressLint("SuspiciousIndentation")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAddingProductBinding.inflate(layoutInflater)

        //Initializing few variables
        list = ArrayList()
        listImages = ArrayList()
        productSize = ArrayList()
        listing = ArrayList()
        dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.loadingpreview)
        dialog.setCancelable(false)

        //again fetching product id that was saved in previous fragment
        val preference =
            requireContext().getSharedPreferences("uniqueId", AppCompatActivity.MODE_PRIVATE)
        val data1 = preference.getString("id", null)


        //This if statement denotes that when this fragment starts then check if the request of starting the fragment is coming
        //from already saved item or for creating new item.
        //if the request is from already existed item then it will have an id which is saved in data1.
        //And if the data1 is null then it means that new item is to be created.
        if (data1 != null) {
            Log.d("uniqueId", data1)
            val preferences =
                requireContext().getSharedPreferences("catName", AppCompatActivity.MODE_PRIVATE)
            val data = preferences.getString("name", null)
            //Here we are fetching all the data that needs to be fetched once the already saved item is clicked from firebase database.
            Firebase.firestore.collection(data!!).document(data1).get()
                .addOnSuccessListener {
                    //this line is used to clear the data of preference once the data is fetched so that
                    //when new item is to be created then this data does not interrupts the procedure.
                    //I was getting previous data even after trying to create new data so had to involve resetting the preference.
                    //first clearing all the sizes button
                    binding.button6.visibility = View.GONE
                    binding.button7.visibility = View.GONE
                    binding.button8.visibility = View.GONE
                    binding.button9.visibility = View.GONE
                    binding.button10.visibility = View.GONE
                    binding.button11.visibility = View.GONE
                    preference.edit().clear().commit()
                    val itemsit = it.toObject(Product::class.java)
                    binding.nameEditText.setText(itemsit?.productName)
                    binding.descEditText.setText(itemsit?.productDesc)
                    binding.priceEditText.setText(itemsit?.productPrice)
                    Glide.with(requireContext()).load(itemsit?.productCoverImage)
                        .into(binding.coverImage)
                    //now again displaying all the sizes available in list that is saved before.
                    for (i in itemsit?.productSize!!) {
                        when (i) {
                            "6" -> binding.button6.visibility = View.VISIBLE
                            "7" -> binding.button7.visibility = View.VISIBLE
                            "8" -> binding.button8.visibility = View.VISIBLE
                            "9" -> binding.button9.visibility = View.VISIBLE
                            "10" -> binding.button10.visibility = View.VISIBLE
                            "11" -> binding.button11.visibility = View.VISIBLE
                        }
                    }
                }
        }
        //adding all the required shoe sizes in listSize
        binding.button6.setOnClickListener {
            listSize.add("6")
            Toast.makeText(requireContext(), "size ${binding.button6.text} added", Toast.LENGTH_SHORT).show()
        }
        binding.button7.setOnClickListener {
            listSize.add("7")
            Toast.makeText(requireContext(), "size ${binding.button7.text} added", Toast.LENGTH_SHORT).show()
        }
        binding.button8.setOnClickListener {
            listSize.add("8")
            Toast.makeText(requireContext(), "size ${binding.button8.text} added", Toast.LENGTH_SHORT).show()
        }
        binding.button9.setOnClickListener {
            listSize.add("9")
            Toast.makeText(requireContext(), "size ${binding.button9.text} added", Toast.LENGTH_SHORT).show()
        }
        binding.button10.setOnClickListener {
            listSize.add("10")
            Toast.makeText(requireContext(), "size ${binding.button10.text} added", Toast.LENGTH_SHORT).show()
        }
        binding.button11.setOnClickListener {
            listSize.add("11")
            Toast.makeText(requireContext(), "size ${binding.button11.text} added", Toast.LENGTH_SHORT).show()
        }

        //intent for cover image
        binding.coverImage.setOnClickListener {
            val intent = Intent("android.intent.action.GET_CONTENT")
            intent.type = "image/*"
            launchGalleryActivity.launch(intent)
        }


        //intent for extra images
        binding.addproductbtn.setOnClickListener {
            val intent = Intent("android.intent.action.GET_CONTENT")
            intent.type = "image/*"
            launchProductActivity.launch(intent)
        }

        //this declaration of the adapter is for images in horizontal orientation
        adapter = addingProductAdapter(requireContext(), list)
        binding.recyclerView.adapter = adapter
//
//        //This declaration of adapter is for shoe size.
//        //Here getData() function is defined in place of a list.
//        sizeAdapter = shoesizeAdapter(requireContext(), getData())
//        binding.sizeRecycler.adapter = sizeAdapter


            binding.donebtn.setOnClickListener {
                validateData()
            }


        return binding.root
    }

    private fun uploadImage() {
        dialog.show()
        val fileName = UUID.randomUUID().toString() + ".jpg"

        val refStorage = FirebaseStorage.getInstance().reference.child("products/$fileName")
        refStorage.putFile(coverImage!!)
            .addOnSuccessListener {
                it.storage.downloadUrl.addOnSuccessListener {
                    coverImageUrl = it.toString()
                    uploadProductImage()
                }.addOnFailureListener {
                    dialog.dismiss()
                    Toast.makeText(requireContext(), "Something went wrong!", Toast.LENGTH_SHORT)
                        .show()
                }

            }
    }

    private var i = 0
    private fun uploadProductImage() {
        dialog.show()
        val fileName = UUID.randomUUID().toString() + ".jpg"
        val refStorage = FirebaseStorage.getInstance().reference.child("products/$fileName")
        refStorage.putFile(list[i])
            .addOnSuccessListener {
                it.storage.downloadUrl.addOnSuccessListener {
                    listImages.add(it.toString())
                    //using recursion here to add all the required images for the item.
                    if (list.size == listImages.size) {
                        storeData()
                    } else {
                        i += 1
                        uploadProductImage()
                    }
                }.addOnFailureListener {
                    dialog.dismiss()
                    Toast.makeText(requireContext(), "Something went wrong!", Toast.LENGTH_SHORT)
                        .show()
                }

            }
    }

    private fun storeData() {
        //Here I have used if statement to distinguish between trending and non-trending products

        if (binding.trending.isChecked) {
            val preferences =
                requireContext().getSharedPreferences("catName", AppCompatActivity.MODE_PRIVATE)
            val data = preferences.getString("name", null)
            val pref =
                requireContext().getSharedPreferences("userName", AppCompatActivity.MODE_PRIVATE)
            val name = pref.getString("user", null)
            val db = Firebase.firestore.collection(data!!)
            val key = db.document().id
            trending = true

            val dataitem = Product(
                binding.nameEditText.text.toString(),
                binding.descEditText.text.toString(),
                binding.priceEditText.text.toString(),
                data,
                coverImageUrl.toString(),
                key,
                listSize,
                listImages,
                name,
                trending
            )
            //Here the product is being stored in defined collection were collection name is BRAND NAME.
            db.document(key).set(dataitem).addOnSuccessListener {
                dialog.dismiss()
                Toast.makeText(requireContext(), "Product added", Toast.LENGTH_SHORT).show()
            }.addOnFailureListener {
                dialog.dismiss()
                Toast.makeText(requireContext(), "Something went wrong", Toast.LENGTH_SHORT).show()
            }
            //Here the product is being stored in collection were collectin name is TRENDING.
            Firebase.firestore.collection("Trending").document(key).set(dataitem)
                .addOnSuccessListener {
                    Log.d("trending", "Trending Item settled.")
                }.addOnFailureListener {
                    Log.d("trending", "Trending Item not settled.")
                }
        }
        //Here in else field I have stored products which are not denoted as trending.
        else {
            val preferences =
                requireContext().getSharedPreferences("catName", AppCompatActivity.MODE_PRIVATE)
            val data = preferences.getString("name", null)
            val pref =
                requireContext().getSharedPreferences("userName", AppCompatActivity.MODE_PRIVATE)
            val name = pref.getString("user", null)
            val db = Firebase.firestore.collection(data!!)
            val key = db.document().id

            val dataitem = Product(
                binding.nameEditText.text.toString(),
                binding.descEditText.text.toString(),
                binding.priceEditText.text.toString(),
                data,
                coverImageUrl.toString(),
                key,
                listSize,
                listImages,
                name,
                trending
            )

            db.document(key).set(dataitem).addOnSuccessListener {
                dialog.dismiss()
                Toast.makeText(requireContext(), "Product added", Toast.LENGTH_SHORT).show()
            }.addOnFailureListener {
                dialog.dismiss()
                Toast.makeText(requireContext(), "Something went wrong", Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun validateData() {
        if (binding.nameEditText.text.toString().isEmpty()) {
            binding.nameEditText.requestFocus()
            binding.nameEditText.error = "Empty"
        } else if (binding.descEditText.text.toString().isEmpty()) {
            binding.descEditText.requestFocus()
            binding.descEditText.error = "Empty"
        } else if (coverImage == null) {
            Toast.makeText(requireContext(), "Please add a cover Image", Toast.LENGTH_SHORT).show()
        } else if (list.size < 1) {
            Toast.makeText(
                requireContext(),
                "Please add few more images of the product",
                Toast.LENGTH_SHORT
            ).show()
        } else {
            uploadImage()
        }
    }

}