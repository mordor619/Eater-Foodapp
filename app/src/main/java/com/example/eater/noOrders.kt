package com.example.eater

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class noOrders : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_no_orders)
    }
    override fun onBackPressed() {
        val int3 = Intent(applicationContext, DishList::class.java)
        startActivity(int3)
    }
}