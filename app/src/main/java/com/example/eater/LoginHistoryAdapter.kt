package com.example.eater

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.*

class LoginHistoryAdapter: RecyclerView.Adapter<LoginHistoryAdapter.MyViewHolder>() {
//    var newpetInterests = emptyList<PetInterestList>()
    var ts= emptyList<TimeStamp>()

    class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return  MyViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.interest_layout_achal,parent,false))
    }

    @SuppressLint("SimpleDateFormat")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = ts[position]

//        holder.itemView.findViewById<TextView>(R.id.petint_name).text= ts[0].loginTimestamp.toString()
        if (position == 0){
            val sdf = SimpleDateFormat("dd-MMM-YYYY")
            val netDate = Date(currentItem.loginTimestamp)
            val displayThisDate = sdf.format(netDate)
            holder.itemView.findViewById<TextView>(R.id.loginhistory).text= "Member since : $displayThisDate"
        }
        else{
            val sdf = SimpleDateFormat("dd-MMM-YYYY")
            val netDate = Date(currentItem.loginTimestamp)
            val newdate = sdf.format(netDate)
            holder.itemView.findViewById<TextView>(R.id.loginhistory).text= "Logged In : $newdate"
        }


    }

    override fun getItemCount(): Int {
        return ts.size
    }

    fun setData(pets: List<TimeStamp>)
    {
        this.ts=pets
        notifyDataSetChanged()
    }
}