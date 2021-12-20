package com.example.eater

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

class OtherUsersActivity : AppCompatActivity() {

    private var layoutManager: RecyclerView.LayoutManager? = null
    private var adapter: RecyclerView.Adapter<OtherUsersAdapter.MyViewHolder>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_achal)

        val progressBarOtherUser = findViewById<ProgressBar>(R.id.progressBarOtherUser)

        progressBarOtherUser.setVisibility(View.VISIBLE)


        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager
        val adapter = OtherUsersAdapter()
        recyclerView.adapter = adapter

        val petsApplication = application as MyDishApplication
        val petService = petsApplication.pets

            CoroutineScope(Dispatchers.IO).launch {
                val decodedpets = petService.getUsers()
                println("decoded result is :${decodedpets.users[0]}")
                withContext(Dispatchers.Main)
                {
                    adapter.setData(decodedpets)
                    progressBarOtherUser.setVisibility(View.INVISIBLE)
                }
            }
    }
}

