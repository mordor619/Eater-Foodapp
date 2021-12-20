package com.example.eater

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log


class landingPage : AppCompatActivity() {

    lateinit var preference :SharedPreferences
    var islogin=false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_landing_page)

        preference = (application as MyDishApplication).preference
        islogin=preference.getBoolean("loginStatus",false);
        val handler = Handler()

        val runnable = Runnable {
            if(islogin){
                val landingIntent1 = Intent(this,DishList::class.java)
                this.startActivity(landingIntent1)
//              finish()
            }
            else{
                val landingIntent2 = Intent(this,login::class.java)
                this.startActivity(landingIntent2)
//              finish()
            }
        }
        handler.postDelayed(runnable, 3000)
    }
}