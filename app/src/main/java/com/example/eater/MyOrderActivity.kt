package com.example.eater

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

import kotlinx.coroutines.withContext

class MyOrderActivity : AppCompatActivity() {

    private var layoutManager: RecyclerView.LayoutManager? = null
    private var adapter: RecyclerView.Adapter<MyOrderAdapter.MyViewHolder>? = null

    public lateinit var preference : SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val progressBarMyOrder = findViewById<ProgressBar>(R.id.progressBarMyOrder)

        progressBarMyOrder.setVisibility(View.VISIBLE)


        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager

        preference = (application as MyDishApplication).preference

        val token = preference.getString("token", "defaultToken")

        val adapter = MyOrderAdapter(token, applicationContext)
        recyclerView.adapter = adapter
        adapter.notifyDataSetChanged()

        val petsApplication = application as MyDishApplication
        val petService = petsApplication.pets

            CoroutineScope(Dispatchers.IO).launch {
                val decodedpetsInterests = petService.getdishes()
                val decodedpets = petService.getMyorders()

                withContext(Dispatchers.Main)
                {
                    println("decodedpets=$decodedpets")
                    println("decodedpetsIntrest=$decodedpetsInterests")
                   //var dishOrders1: OrderList ={dishOrders= emptyList<Orders>() }
                    println(decodedpets.dishOrders.isEmpty())
                    if (decodedpets.dishOrders.isEmpty()) {
                        val int3 = Intent(applicationContext, noOrders::class.java)

                        progressBarMyOrder.setVisibility(View.INVISIBLE)

                        startActivity(int3)
                    } else {
                        adapter.setData(decodedpets.dishOrders, decodedpetsInterests.dishes)
                        progressBarMyOrder.setVisibility(View.INVISIBLE)
                    }
                }
            }
    }

    override fun onBackPressed() {
        val int3 = Intent(applicationContext, DishList::class.java)
        startActivity(int3)
    }
}

