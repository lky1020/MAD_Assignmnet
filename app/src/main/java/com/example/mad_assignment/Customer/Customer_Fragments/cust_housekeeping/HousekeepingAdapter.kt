package com.example.mad_assignment.Customer.Customer_Fragments.cust_housekeeping

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mad_assignment.Customer.Customer_Fragments.cust_housekeeping.Class.Housekeeping
import com.example.mad_assignment.R
import com.squareup.picasso.Picasso


class HousekeepingAdapter(private var housekeepingList: ArrayList<Housekeeping>): RecyclerView.Adapter<HousekeepingAdapter.HousekeepingViewHolder>() {

    class HousekeepingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val tvTitle: TextView = itemView.findViewById(R.id.housekeeping_id)
        val ivHousekeeping: ImageView = itemView.findViewById(R.id.housekeeping_img)
    }

    override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
    ): HousekeepingViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.cardview_item_housekeeping, parent, false)

        return HousekeepingViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: HousekeepingViewHolder, position: Int) {
        val currentItem = housekeepingList[position]

        holder.tvTitle.text = currentItem.title
        Picasso.get().load(currentItem.img).into(holder.ivHousekeeping);
    }

    override fun getItemCount(): Int {
        return housekeepingList.size
    }
}