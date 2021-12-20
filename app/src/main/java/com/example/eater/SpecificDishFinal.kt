package com.example.eater

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso

class SpecificDishFinal : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_specific_dish_final)

        var dishname = intent.getStringExtra("dishname")
        var dishId = intent.getIntExtra("dishId", 0)
        var dishImg = intent.getStringExtra("dishImg")
        var dishType = intent.getStringExtra("dishType")
        var dishPrice = intent.getIntExtra("dishPrice", 0)
        var dishContent = intent.getStringExtra("dishContent")


        val DishName = findViewById<TextView>(R.id.dishName)
        val DishImage = findViewById<ImageView>(R.id.dishImage)
        val DishType = findViewById<TextView>(R.id.type)
        val DishContent = findViewById<TextView>(R.id.contents)
        val DishPrice = findViewById<TextView>(R.id.priceServing)
        var CreateOrd = findViewById<Button>(R.id.createorder)

        DishName.text = dishname
        DishPrice.text = "Price per serving $$dishPrice"
        DishContent.text = "Contents : $dishContent"
        DishType.text = "Type : $dishType"
        Picasso.get().load(dishImg).into(DishImage)

        CreateOrd.setOnClickListener{

            val intent = Intent(this@SpecificDishFinal,CreateOrder::class.java)
            intent.putExtra("name",dishname)
            intent.putExtra("id",dishId.toString())
            intent.putExtra("img",dishImg)
            intent.putExtra("price",dishPrice.toString())
            startActivity(intent)
        }

    }
}