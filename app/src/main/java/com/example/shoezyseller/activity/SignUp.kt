package com.example.shoezyseller.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.shoezyseller.R
import com.example.shoezyseller.databinding.ActivitySignUpBinding
import com.example.shoezyseller.models.sellers
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class SignUp : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        auth = Firebase.auth

        val name = binding.fullnameEditText.text.toString()
        val username = binding.usernameEditText.text.toString()
        val password = binding.passwordEditText.text.toString()
        val confpassword = binding.confpasswordEditText.text.toString()


        binding.appCompatButton.setOnClickListener {
            validataData()
        }
    }

    private fun validataData() {
        if (binding.fullnameEditText.text.isEmpty() || binding.usernameEditText.text.isEmpty()
            || binding.passwordEditText.text.isEmpty() || binding.confpasswordEditText.text.isEmpty()
        ) {
            Toast.makeText(this, "Please fill all the details!", Toast.LENGTH_SHORT).show()
        } else{
            storeData()
            authenticate()
        }
    }

    private fun authenticate() {
        val username = binding.usernameEditText.text.toString()
        val password = binding.passwordEditText.text.toString()
        
        auth.createUserWithEmailAndPassword(username,password).addOnCompleteListener { 
            if (it.isSuccessful){
                Toast.makeText(this, "User registered", Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(this, "${it.exception?.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun storeData() {
        val builder = AlertDialog.Builder(this)
            .setTitle("Loading....")
            .setMessage("Please Wait")
            .setCancelable(false)
            .create()
        builder.show()

        val db = Firebase.firestore

        val data = sellers(name = binding.fullnameEditText.text.toString(), username = binding.usernameEditText.text.toString(),
        password = binding.passwordEditText.text.toString(), confirmPassword = binding.confpasswordEditText.text.toString())
        db.collection("sellerProfile").document(binding.usernameEditText.text.toString()).set(data)
            .addOnSuccessListener {
                builder.dismiss()
                Toast.makeText(this, "Seller details saved!", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, SignIn::class.java))
            }.addOnFailureListener {
                builder.dismiss()
                    Toast.makeText(this, "Something went Wrong!", Toast.LENGTH_SHORT).show()
            }
    }
}