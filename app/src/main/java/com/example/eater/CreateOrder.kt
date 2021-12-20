package com.example.eater

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import com.google.gson.GsonBuilder
import com.google.gson.JsonParser
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.logging.HttpLoggingInterceptor
import org.json.JSONObject
import retrofit2.Retrofit

class CreateOrder : AppCompatActivity() {

    var cnt = 0
    public lateinit var preference : SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_order)

        val profileName=intent.getStringExtra("name")
        val profileId=intent.getStringExtra("id")
        val profileImg=intent.getStringExtra("img")
        var profilePrice=intent.getStringExtra("price")

        val ProgressBar = findViewById<ProgressBar>(R.id.progressBar)
        ProgressBar.setVisibility(View.INVISIBLE)

        val dishName = findViewById<TextView>(R.id.dishName)
        dishName.text = profileName

        val dishImage = findViewById<ImageView>(R.id.dishImage)
        Picasso.get().load(profileImg).into(dishImage)


        val ItemCountView = findViewById<TextView>(R.id.itemCountView)
        val IncButton = findViewById<Button>(R.id.incButton)
        val DecButton = findViewById<Button>(R.id.decButton)
        val Price = findViewById<TextView>(R.id.priceTextView)
        val CreateOrderBtn = findViewById<Button>(R.id.confirmorder)




        IncButton.setOnClickListener{
            cnt++
            if(cnt > 0){
                DecButton.setClickable(true)
                CreateOrderBtn.setClickable(true)
            }
            ItemCountView.text = "$cnt"

            var price = cnt*profilePrice.toString().toInt()
            Price.text = "Total Price: $price"

        }
        DecButton.setOnClickListener{
            if(cnt>0){
                cnt--
            }
            if(cnt == 0){
                DecButton.setClickable(false)
                CreateOrderBtn.setClickable(false)
            }
            ItemCountView.text = "$cnt"
            var price = cnt*10
            Price.text = "Total Price: $price"
        }



        CreateOrderBtn.setOnClickListener {

            if (cnt != 0) {



            ProgressBar.setVisibility(View.VISIBLE)
//            viewOnProgress.setVisibility(View.VISIBLE)
            IncButton.setVisibility(View.INVISIBLE)
            DecButton.setVisibility(View.INVISIBLE)
            CreateOrderBtn.setVisibility(View.INVISIBLE)

            preference = (application as MyDishApplication).preference

            val token = preference.getString("token", "defaultToken")

            val loggingInterceptor = HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)

            val client = OkHttpClient.Builder().addInterceptor { chain ->
                val newRequest: Request = chain.request().newBuilder()
                    .addHeader("Authorization", "Bearer $token")
                    .build()
                chain.proceed(newRequest)
            }.addInterceptor(loggingInterceptor)          //gives details about connection
                .build()

            val retrofit = Retrofit.Builder()
                .client(client)
                .baseUrl("https://android-kanini-course.cloud/eaterapp/")
                .build()

            // Create Service
            val service = retrofit.create(EaterInterface::class.java)

            // Create JSON using JSONObject
            val jsonObject = JSONObject()
            jsonObject.put("dishId", profileId.toString().toInt())
            jsonObject.put("count", cnt)

            val jsonObjectString = jsonObject.toString()

            // Create RequestBody ( We're not using any converter, like GsonConverter, MoshiConverter e.t.c, that's why we use RequestBody )
            val requestBody = jsonObjectString.toRequestBody("application/json".toMediaTypeOrNull())

            println("Request body is : $requestBody")
            println("Request body is : $jsonObjectString")
            CoroutineScope(Dispatchers.IO).launch {
                // Do the POST request and get response
                val response = service.createOrder(requestBody)

                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {

                        // Convert raw JSON to pretty JSON using GSON library
                        val gson = GsonBuilder().setPrettyPrinting().create()
                        val prettyJson = gson.toJson(
                            JsonParser.parseString(
                                response.body()
                                    ?.string() // About this thread blocking annotation : https://github.com/square/retrofit/issues/3255
                            )
                        )
                        ProgressBar.setVisibility(View.INVISIBLE)
                        Log.d("Pretty Printed JSON :", prettyJson)
                        Toast.makeText(this@CreateOrder, "Order placed", Toast.LENGTH_LONG).show()

                        val loginIntent = Intent(this@CreateOrder, MyOrderActivity::class.java)
                        loginIntent.putExtra("message1", "This is Home Page")
                        loginIntent.putExtra("message2", prettyJson)
                        this@CreateOrder.startActivity(loginIntent)


                    } else {
                        ProgressBar.setVisibility(View.INVISIBLE)
//            viewOnProgress.setVisibility(View.VISIBLE)
                        IncButton.setVisibility(View.VISIBLE)
                        DecButton.setVisibility(View.VISIBLE)
                        CreateOrderBtn.setVisibility(View.VISIBLE)
                        Log.e("RETROFIT_ERROR", response.code().toString())
                        Toast.makeText(this@CreateOrder, "Some error occurred", Toast.LENGTH_LONG)
                            .show()
                    }
                }
            }

        }
            else{
                Toast.makeText(applicationContext, "Add items first", Toast.LENGTH_LONG).show()
            }
        }
    }
}