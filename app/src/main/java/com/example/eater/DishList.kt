package com.example.eater

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.navigation.NavigationView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

import android.widget.ImageView
import java.io.File
import java.io.FileInputStream

import android.content.Context
import android.content.SharedPreferences
import android.graphics.*
import com.amulyakhare.textdrawable.TextDrawable
import android.graphics.BitmapFactory
import android.view.View
import android.widget.ProgressBar


class DishList : AppCompatActivity() {

    lateinit var toggle: ActionBarDrawerToggle
    lateinit var preference: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dish_list)

        //val loginIntentToken = intent.getStringExtra("tokenIntent")

        //setting custom toolbar as default
        val my_toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.my_toolbar)
        setSupportActionBar(my_toolbar)
        //ends


        //drawer layout code starts here
        val drawerLayout = findViewById<DrawerLayout>(R.id.drawerLayout)
        val nav_view = findViewById<NavigationView>(R.id.nav_view)

        toggle = ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close)

        drawerLayout.addDrawerListener(toggle)

        toggle.syncState()

        //setting custom toolbar image
        supportActionBar?.setHomeButtonEnabled(true);
        supportActionBar?.setDisplayHomeAsUpEnabled(true);
        supportActionBar?.setHomeAsUpIndicator(R.drawable.eater_logo);//your icon here
        //ends


        //change email id in drawer
        changeEmailDrawer(nav_view)
        //ends

        //change photo in drawer
        changePhotoDrawer(nav_view)
        //ends

        //on item click listener
        nav_view.setNavigationItemSelectedListener {
            when (it.itemId) {

                R.id.profile -> {
                    val intent = Intent(this, Profile::class.java)
                    startActivity(intent)
                }

                R.id.myOrders -> {
                    val intent = Intent(this, MyOrderActivity::class.java)
                    startActivity(intent)
//                    Toast.makeText(
//                        applicationContext,
//                        "my orders", Toast.LENGTH_SHORT
//                    ).show()
                }

                R.id.otherUsers -> {
                    val intent = Intent(this, OtherUsersActivity::class.java)
                    startActivity(intent)
//                    Toast.makeText(
//                        applicationContext,
//                        "other users", Toast.LENGTH_SHORT
//                    ).show()
                }

            }
            true

        }


        //drawer layout code ends here


        val progressBarDishList = findViewById<ProgressBar>(R.id.progressBarDishList)
        progressBarDishList.setVisibility(View.VISIBLE)


        // here on recycler view code


        var dishes: DishListResponse
        var dishesAdapter = listOf<Dish>()

        val allDishes = findViewById<RecyclerView>(R.id.allDishes)

        val myApp = application as MyDishApplication
        val httpApiService = myApp.httpApiService

        CoroutineScope(Dispatchers.IO).launch {

            dishes = httpApiService.getAllDishes()     //http req here

            if (dishes.dishes != null) {
                dishesAdapter = dishes.dishes!!
            }

            withContext(Dispatchers.Main) {
                val adapter = MyDishAdapter(dishesAdapter, applicationContext)
                allDishes.adapter = adapter

                allDishes.layoutManager = LinearLayoutManager(applicationContext)
            }

        }


        //rc view code ends here

        progressBarDishList.setVisibility(View.INVISIBLE)

    }

    private fun changePhotoDrawer(nav_view: NavigationView) {
        preference = (application as MyDishApplication).preference

        val drawerEmailIdValue = preference.getString("email", "abc")
        val photoText = drawerEmailIdValue?.substring(0, 1)?.toUpperCase()

        val drawerPhoto = nav_view.getHeaderView(0).findViewById<ImageView>(R.id.drawerProfileImage)

        val f = File("/data/data/com.example.eater/app_imageDirectory", "profileImage.jpg")

        if (f.exists()) {
            val b = BitmapFactory.decodeStream(FileInputStream(f))
            drawerPhoto.setImageBitmap(b)
        } else {
            val drawableDrawerImage = TextDrawable.builder()
                .buildRound(photoText, Color.RED) // radius in px

            drawerPhoto.setImageDrawable(drawableDrawerImage)
        }


    }

    override fun onBackPressed() {
        finishAffinity()
        finish()
    }


    private fun changeEmailDrawer(nav_view: NavigationView) {
        preference = (application as MyDishApplication).preference

        val drawerEmailIdValue = preference.getString("email", "abc")

        val drawerEmailId = nav_view.getHeaderView(0).findViewById<TextView>(R.id.drawerEmailId)
        drawerEmailId.text = drawerEmailIdValue
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)) {
            return true
        }

        return super.onOptionsItemSelected(item)
    }

}