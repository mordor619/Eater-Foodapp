package com.example.eater

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import com.google.gson.GsonBuilder
import com.google.gson.JsonParser
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import retrofit2.Retrofit

class signUp : AppCompatActivity() {

    var emailRegister = ""
    var passwordRegister = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        val registerMe =findViewById<Button>(R.id.registerMe)
        val inputEmailRegister =findViewById<EditText>(R.id.inputEmailRegister)
        val inputPasswordRegister =findViewById<EditText>(R.id.inputPasswordRegister)
        val progressRegister =findViewById<ProgressBar>(R.id.progressRegister)
        val viewProgressRegister =findViewById<View>(R.id.viewProgressRegister)

//        val message1=intent.getStringExtra("message1")
//        val message2=intent.getStringExtra("message2")
//        Log.d("Message1", ""+message1)
//        Log.d("Message2", ""+message2)

        registerMe.setOnClickListener {

            emailRegister=inputEmailRegister.text.toString()
            passwordRegister=inputPasswordRegister.text.toString()
            if(emailRegister.isNullOrEmpty() || passwordRegister.isNullOrEmpty()){
                Toast.makeText(this@signUp, "Please fill the Details", Toast.LENGTH_LONG).show()
            }
            else
            {

                progressRegister.setVisibility(View.VISIBLE)
                viewProgressRegister.setVisibility(View.VISIBLE)
                registerMe.setVisibility(View.INVISIBLE)


                // Create Retrofit
                val retrofit = Retrofit.Builder()
                    .baseUrl("https://android-kanini-course.cloud")
                    .build()

                // Create Service
                val service = retrofit.create(Api::class.java)

                // Create JSON using JSONObject
                val jsonObject = JSONObject()
                jsonObject.put("email", emailRegister)
                jsonObject.put("password", passwordRegister)

                // Convert JSONObject to String
                val jsonObjectString = jsonObject.toString()

                // Create RequestBody ( We're not using any converter, like GsonConverter, MoshiConverter e.t.c, that's why we use RequestBody )
                val requestBody = jsonObjectString.toRequestBody("application/json".toMediaTypeOrNull())

                CoroutineScope(Dispatchers.IO).launch {
                    // Do the POST request and get response
                    val response = service.userRegister(requestBody)

                    withContext(Dispatchers.Main) {
                        if (response.isSuccessful) {

//                            progressBar.setVisibility(View.INVISIBLE)
                            Toast.makeText(this@signUp, "Register Successfully", Toast.LENGTH_LONG).show()

                            val Intenttologin= Intent(this@signUp,login::class.java)
                            this@signUp.startActivity(Intenttologin)
                        }
                        else{
                            registerMe.setVisibility(View.VISIBLE)
                            progressRegister.setVisibility(View.INVISIBLE)
                            viewProgressRegister.setVisibility(View.INVISIBLE)
                            Log.e("RETROFIT_ERROR", response.code().toString())
                            Toast.makeText(this@signUp, "Please try later", Toast.LENGTH_LONG).show()
                        }
                    }
                }

            }
        }
    }
}