package com.example.eater

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
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
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.logging.HttpLoggingInterceptor
import org.json.JSONObject
import retrofit2.Callback
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit



class login : AppCompatActivity() {

    var email = ""
    var password = ""
    lateinit var sharedPreference : SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf<String>(Manifest.permission.CAMERA), 1001);
        }             //ask camera permissions


        val buttonLogin =findViewById<Button>(R.id.buttonLogin)
        val buttonSignup =findViewById<Button>(R.id.buttonSignup)
        val inputEmail =findViewById<EditText>(R.id.inputEmail)
        val inputPassword =findViewById<EditText>(R.id.inputPassword)
        val progressBar =findViewById<ProgressBar>(R.id.progressBar)
        val viewOnProgress =findViewById<View>(R.id.viewOnProress)

//        inputPassword.setTransformationMethod(new PasswordTransformationMethod())

        buttonSignup.setOnClickListener {
            val signUpIntent= Intent(this@login,signUp::class.java)
//            signUpIntent.putExtra("message1","This is Home Page")
//            signUpIntent.putExtra("message2","Hello")
            this@login.startActivity(signUpIntent)
            //finish()
        }


        buttonLogin.setOnClickListener {

            email=inputEmail.text.toString()
            password=inputPassword.text.toString()
            if(email.isNullOrEmpty() || password.isNullOrEmpty()){
                Toast.makeText(this@login, "Please fill empty field", Toast.LENGTH_LONG).show()
            }
            else
            {
                progressBar.setVisibility(View.VISIBLE)
                viewOnProgress.setVisibility(View.VISIBLE)
                buttonLogin.setVisibility(View.INVISIBLE)
                buttonSignup.setVisibility(View.INVISIBLE)

                val loggingInterceptor = HttpLoggingInterceptor()
                loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)

                val client = OkHttpClient.Builder().addInterceptor(loggingInterceptor)          //gives details about connection
                    .build()

                // Create Retrofit
                val retrofit = Retrofit.Builder()
                    .client(client)
                    .baseUrl("https://android-kanini-course.cloud")
                    .build()

                // Create Service
                val service = retrofit.create(Api::class.java)

                // Create JSON using JSONObject
                val jsonObject = JSONObject()
                jsonObject.put("email", email)
                jsonObject.put("password", password)

                // Convert JSONObject to String
                val jsonObjectString = jsonObject.toString()

                // Create RequestBody ( We're not using any converter, like GsonConverter, MoshiConverter e.t.c, that's why we use RequestBody )
                val requestBody = jsonObjectString.toRequestBody("application/json".toMediaTypeOrNull())

                CoroutineScope(Dispatchers.IO).launch {
                    // Do the POST request and get response
                    val response = service.userLogin(requestBody)

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

                            val userInfo = JSONObject(prettyJson)
                            sharedPreference =  (application as MyDishApplication).preference

                            var editor:SharedPreferences.Editor = sharedPreference.edit()
                            editor.putInt("id",userInfo.getInt("id"))
                            editor.putString("token",userInfo.getString("token"))
                            editor.putString("email",userInfo.getString("email"))
                            editor.putLong("l",userInfo.getLong("memberSince"))
                            editor.putBoolean("loginStatus",true)
                            editor.commit()

                            progressBar.setVisibility(View.INVISIBLE)
                            Log.d("Pretty Printed JSON after shared preference :", prettyJson)
                            Toast.makeText(this@login, "Login Successful", Toast.LENGTH_LONG).show()

                            val loginIntent= Intent(this@login,DishList::class.java)
                            loginIntent.putExtra("tokenIntent", userInfo.getString("token"))
                            this@login.startActivity(loginIntent)
                            //finish()
                        }
                        else{
                            buttonSignup.setVisibility(View.VISIBLE)
                            buttonLogin.setVisibility(View.VISIBLE)
                            progressBar.setVisibility(View.INVISIBLE)
                            viewOnProgress.setVisibility(View.INVISIBLE)
                            Log.e("RETROFIT_ERROR", response.code().toString())
                            Toast.makeText(this@login, "Invalid Credential", Toast.LENGTH_LONG).show()
                        }
                    }
                }

            }
        }
    }

    override fun onBackPressed() {
        finishAffinity()
        finish()
    }

}



