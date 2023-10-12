package com.example.shoezyseller

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import androidx.fragment.app.FragmentManager
import androidx.navigation.fragment.findNavController
import com.example.shoezyseller.activity.SignIn
import com.example.shoezyseller.databinding.ActivityMainBinding
import com.example.shoezyseller.fragments.HomeFragment
import com.example.shoezyseller.fragments.OrdersFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()



    }
}