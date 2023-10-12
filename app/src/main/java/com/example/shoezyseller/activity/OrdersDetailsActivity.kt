package com.example.shoezyseller.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.shoezyseller.databinding.ActivityOrdersBinding
import com.example.shoezyseller.models.allOrderModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class OrdersDetailsActivity : AppCompatActivity() {
    private lateinit var binding : ActivityOrdersBinding
    private lateinit var list:ArrayList<allOrderModel>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOrdersBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val id = intent.getStringExtra("id")
        Firebase.firestore.collection("AllOrders").document(id!!)
            .get().addOnSuccessListener {
                val userId = it.getString("userId")
                binding.productName.text = it.getString("name")
                binding.productId.text = "product Id : ${it.getString("productId")}"
                binding.price.text = "Price : ${it.getString("price")}"
                binding.size.text = "Size : ${it.getString("shoeSize")}"
                binding.qty.text = "Qty : ${it.getString("quantity")}"
                Firebase.firestore.collection("usersProfile").document(userId!!)
                    .get().addOnSuccessListener {
                        binding.username.text = it.getString("name")
                        binding.userphone.text = it.getString("phoneNumber")
                        binding.mainaddress.text = it.getString("address")
                        binding.city.text = it.getString("city")
                        binding.state.text = it.getString("state")
                        binding.pin.text = it.getString("pincode")
                    }
            }
    }
}