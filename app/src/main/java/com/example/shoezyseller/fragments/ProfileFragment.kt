package com.example.shoezyseller.fragments

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.shoezyseller.databinding.FragmentProfileBinding
import com.example.shoezyseller.models.sellers
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import java.util.*


class ProfileFragment : Fragment() {
    private lateinit var binding:FragmentProfileBinding
    private var imageUrl: Uri? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(layoutInflater)

        getInfo()

        binding.saveBtn.setOnClickListener {

           saveInfo()
        }

        return binding.root
    }


//    private fun uploadImage(uri: Uri) {
//
//        val fileName = "profile.jpg"
//
//        val refStorage = FirebaseStorage.getInstance().reference.child("profilePic/$fileName")
//        refStorage.putFile(uri)
//            .addOnSuccessListener {
//                it.storage.downloadUrl.addOnSuccessListener {image ->
//                    storeImage(image.toString())
//                }
//            }.addOnFailureListener {
//                Toast.makeText(requireContext(), "Something went wrong with storage", Toast.LENGTH_SHORT).show()
//            }
//    }

//    private fun storeImage(image: String) {
//        val builder = AlertDialog.Builder(requireContext())
//                .setTitle("Loading....")
//                .setMessage("Please Wait")
//                .setCancelable(false)
//                .create()
//            builder.show()
//
//        val data = hashMapOf<String,Any>(
//            "profileImage" to image
//        )
//        val preferences = requireContext().getSharedPreferences("userName",AppCompatActivity.MODE_PRIVATE)
//        val name = preferences.getString("user",null)
//        Firebase.firestore.collection("sellerProfile").document(name!!)
//            .set(data).addOnSuccessListener {
//                builder.dismiss()
//                val pref = requireContext().getSharedPreferences("profileImage",AppCompatActivity.MODE_PRIVATE)
//                val editor = pref.edit()
//                editor.putString("image", data.toString())
//                editor.apply()
//                Toast.makeText(requireContext(), "profile picture uploaded", Toast.LENGTH_SHORT).show()
//            }.addOnFailureListener {
//                builder.dismiss()
//                Toast.makeText(requireContext(), "Something went wrong try again!", Toast.LENGTH_SHORT).show()
//            }
//    }

    private fun saveInfo() {
        val builder = AlertDialog.Builder(requireContext())
            .setTitle("Loading....")
            .setMessage("Please Wait")
            .setCancelable(false)
            .create()
        builder.show()
        val phoneNumber = binding.phonedit.text.toString()
        val gstNumber = binding.gstedit.text.toString()
        val state = binding.stateedit.text.toString()
        val companyName = binding.companyedit.text.toString()
        val username = binding.emailedit.text.toString()
        val name = binding.nameedit.text.toString()

        val data = sellers(
            username = username,
            name = name,
            phoneNumber = phoneNumber,
            gstNumber = gstNumber,
            state = state,
            companyName = companyName
        )



        val preferences = requireContext().getSharedPreferences("userName", AppCompatActivity.MODE_PRIVATE)
        val Name = preferences.getString("user",null)
        Firebase.firestore.collection("sellerProfile").document(Name!!)
            .set(data, SetOptions.merge()).addOnSuccessListener {
                builder.dismiss()
                Toast.makeText(requireContext(), "Your profile is updated.", Toast.LENGTH_SHORT).show()

            }.addOnFailureListener { 
                builder.dismiss()
                Toast.makeText(requireContext(), "Something went wrong", Toast.LENGTH_SHORT).show()
            }

    }


    private fun getInfo() {
        val dialog = AlertDialog.Builder(requireContext())
            .setTitle("Loading....")
            .setMessage("Please Wait")
            .setCancelable(false)
            .create()
        dialog.show()
        val preferences = requireContext().getSharedPreferences("userName", AppCompatActivity.MODE_PRIVATE)
        val name = preferences.getString("user",null)
        Firebase.firestore.collection("sellerProfile").document(name!!)
            .get().addOnSuccessListener {
                dialog.dismiss()
                val data = it.toObject<sellers>()
                binding.emailedit.setText(data!!.username)
                binding.nameedit.setText(data.name)
                binding.phonedit.setText(data.phoneNumber)
                binding.gstedit.setText(data.gstNumber)
                binding.companyedit.setText(data.companyName)
                binding.stateedit.setText(data.state)
            }.addOnFailureListener {
                dialog.dismiss()
                Toast.makeText(requireContext(), "Unable to fetch data right now!", Toast.LENGTH_SHORT).show()
            }
    }
}