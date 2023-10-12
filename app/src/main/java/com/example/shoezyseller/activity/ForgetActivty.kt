package com.example.shoezyseller.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.shoezyseller.R
import com.example.shoezyseller.databinding.ActivityForgetActivtyBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class ForgetActivty : AppCompatActivity() {
    private lateinit var binding: ActivityForgetActivtyBinding
    private lateinit var auth : FirebaseAuth
     override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
         binding = ActivityForgetActivtyBinding.inflate(layoutInflater)
        setContentView(binding.root)

         auth = Firebase.auth

         binding.appCompatButton.setOnClickListener {
             val email = binding.usernameEditText.text.toString()
             if (email.isNotEmpty()) {
                 auth.sendPasswordResetEmail(email).addOnCompleteListener {
                     if (it.isSuccessful) {
                         Toast.makeText(
                             this,
                             "Check your Email to reset password",
                             Toast.LENGTH_SHORT
                         ).show()
                         startActivity(Intent(this, SignIn::class.java))
                         finish()
                     } else {
                         Toast.makeText(this, "${it.exception?.message}", Toast.LENGTH_SHORT).show()
                     }
                 }
             }
         }
    }
}