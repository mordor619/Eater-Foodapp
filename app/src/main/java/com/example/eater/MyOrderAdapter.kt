package com.example.eater

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit


class MyOrderAdapter(val token: String?, val applicationContext: Context) : RecyclerView.Adapter<MyOrderAdapter.MyViewHolder>() {

    var newpetInterests : MutableList<Dish1> = mutableListOf<Dish1>()
    var newpets :MutableList<Orders> = mutableListOf<Orders>()
    class MyViewHolder(itemView: View):RecyclerView.ViewHolder(itemView) {

    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return  MyViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.card_layout,parent,false))
    }
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

            val currentItem = newpets[position]
            for (item in newpetInterests) {
                if (item.id == currentItem.dishId) {
                    var getPr = item.price.toString()
                    holder.itemView.findViewById<TextView>(R.id.pet_name).text = item.name
                    holder.itemView.findViewById<TextView>(R.id.getPrice).text = "$$getPr"
                    holder.itemView.findViewById<TextView>(R.id.pet_age).text = item.type
                    val imageview = holder.itemView.findViewById<ImageView>(R.id.pet_image)
                    Picasso.get().load(item.url).into(imageview)
                    val button = holder.itemView.findViewById<Button>(R.id.cancelOrder)
                    button.setOnClickListener {
                        //println("---------------cancel button clicked---------")
                        val loggingInterceptor = HttpLoggingInterceptor();
                        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
                        val client = OkHttpClient.Builder().addInterceptor { chain ->
                            val newRequest: Request = chain.request().newBuilder()
                                .addHeader("Authorization", "Bearer $token")
                                .build()
                            chain.proceed(newRequest)
                        }
                            .addInterceptor(loggingInterceptor)          //gives details about connection
                            .build()
                        val retrofit = Retrofit.Builder()
                            .client(client)
                            .baseUrl("https://android-kanini-course.cloud/eaterapp/")
                            .build()
                        // Create Service
                        val service = retrofit.create(EaterInterface::class.java)
                        CoroutineScope(Dispatchers.IO).launch {
                            // Do the POST request and get response
                            val response = service.deleteOrder(currentItem.orderId)
                            withContext(Dispatchers.Main) {
                                if (response.isSuccessful) {
                                    val intent = Intent(
                                        applicationContext,
                                        MyOrderActivity::class.java
                                    )
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    applicationContext.startActivity(intent)
                                } else {
                                    Log.e("RETROFIT_ERROR", response.code().toString())
                                    //Toast.makeText(this@RecyclerAdapter, "Invalid Credential", Toast.LENGTH_LONG).show()
                                }
                            }
                        }

                    }
                }


           }
    }

    override fun getItemCount(): Int {
        return newpets.size
    }

    fun setData(pets: MutableList<Orders>, _newpets: MutableList<Dish1>)
    {
        this.newpets=pets
        this.newpetInterests=_newpets
        notifyDataSetChanged()
    }

}