package com.example.eater

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class OtherUsersAdapter: RecyclerView.Adapter<OtherUsersAdapter.MyViewHolder>() {
    var petList = emptyList<User>()

    class MyViewHolder(itemView: View):RecyclerView.ViewHolder(itemView) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return  MyViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.card_layout_achal,parent,false))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = petList[position]
        holder.itemView.findViewById<TextView>(R.id.pet_name).text= currentItem.email
        var Orders = currentItem.orderedDishes
        var EmailLabel = currentItem.email[0]
        holder.itemView.findViewById<TextView>(R.id.ordered).text= "Ordered: $Orders"
        holder.itemView.findViewById<TextView>(R.id.emailLabel).text= EmailLabel.toString()

    }

    override fun getItemCount(): Int {
        return petList.size
    }

    fun setData(pets: Users)
    {
        this.petList=pets.users
        notifyDataSetChanged()
    }

}