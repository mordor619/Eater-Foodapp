package com.example.eater

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

class MyDishAdapter(val dishes: List<Dish>, val applicationContext: Context): RecyclerView.Adapter<MyDishAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.each_dish_layout, parent, false)

        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        //holder.txtTitle.text = songs[position].title
        //holder.txtDescription.text = songs[position].desc

        var dishType = dishes[position].type
        var dishPrice = dishes[position].price
        var dishImgUrl = dishes[position].url

        holder.dishName.text = dishes[position].name
        holder.dishType.text = "Type: $dishType"
        holder.dishPrice.text = "$dishPrice$ per serving"

        Picasso.get().load(dishImgUrl).into(holder.dishImage)

        holder.itemView.setOnClickListener {
            val int2 = Intent(applicationContext, SpecificDishFinal::class.java)
            int2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            int2.putExtra("dishname", dishes[position].name)
            int2.putExtra("dishId", dishes[position].id)
            int2.putExtra("dishImg", dishes[position].url)
            int2.putExtra("dishType", dishes[position].type)
            int2.putExtra("dishPrice", dishes[position].price)
            int2.putExtra("dishContent", dishes[position].contents)

            applicationContext.startActivity(int2)
        }

    }

    override fun getItemCount(): Int {
        return dishes.size
    }


    class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        var dishImage = itemView.findViewById<ImageView>(R.id.dishImage)
        var dishName = itemView.findViewById<TextView>(R.id.dishName)
        var dishType = itemView.findViewById<TextView>(R.id.dishType)
        var dishPrice = itemView.findViewById<TextView>(R.id.dishPrice)

    }

}