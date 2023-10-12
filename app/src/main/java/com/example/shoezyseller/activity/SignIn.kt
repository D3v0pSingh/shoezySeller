package com.example.shoezyseller.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.shoezyseller.MainActivity
import com.example.shoezyseller.databinding.ActivitySignInBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class SignIn : AppCompatActivity() {
    private lateinit var binding: ActivitySignInBinding
    private lateinit var auth : FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val email = binding.usernameEditText.text.toString()
        val password = binding.passwordEditText.text.toString()


        auth = Firebase.auth

        binding.textView4.setOnClickListener {
            startActivity(Intent(this,ForgetActivty::class.java))
            finish()
        }

        binding.textView6.setOnClickListener {
            startActivity(Intent(this,SignUp::class.java))
            finish()
        }

        binding.appCompatButton.setOnClickListener {
            val email = binding.usernameEditText.text.toString()
            val password = binding.passwordEditText.text.toString()

            val preferences = this.getSharedPreferences("userName", MODE_PRIVATE)
            val editor = preferences.edit()
            editor.putString("user", email)
            editor.apply()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                auth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
                    if (it.isSuccessful){
                        startActivity(Intent(this, MainActivity::class.java))
                    }else{
                        Toast.makeText(this, "${it.exception?.message}", Toast.LENGTH_SHORT).show()
                    }
                }
            }

        }
    }
}