package com.example.mad_assignment.Customer.Customer_Fragments.cust_housekeeping.Adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.mad_assignment.Customer.Customer_Fragments.cust_housekeeping.Class.HousekeepingOrderedItem
import com.example.mad_assignment.R
import com.google.android.material.imageview.ShapeableImageView
import com.squareup.picasso.Picasso
import kotlin.collections.ArrayList

class HousekeepingItemOrderedAdapter(private var housekeepingItemOrderedList: ArrayList<HousekeepingOrderedItem>, private var mContext: FragmentActivity): RecyclerView.Adapter<HousekeepingItemOrderedAdapter.HousekeepingItemOrderedViewHolder>() {

    class HousekeepingItemOrderedViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val ivItemOrdered: ShapeableImageView = itemView.findViewById(R.id.iv_item_ordered)
        val tvOrderedTitle: TextView = itemView.findViewById(R.id.tv_ordered_title)
        val tvOrderedQuantity: TextView = itemView.findViewById(R.id.tv_ordered_quantity)
        val tvOrderedEstimateTime: TextView = itemView.findViewById(R.id.tv_ordered_estimate_time)
        val btnCancelItemOrdered: ImageView = itemView.findViewById(R.id.btn_cancel_item_ordered)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): HousekeepingItemOrderedViewHolder {
        val itemView = LayoutInflater.from(mContext).inflate(R.layout.cardview_item_housekeeping_services_ordered, parent, false)

        return HousekeepingItemOrderedViewHolder(itemView)
    }

    @SuppressLint("SetTextI18n", "SimpleDateFormat")
    override fun onBindViewHolder(holder: HousekeepingItemOrderedViewHolder, position: Int) {
        val currentItem = housekeepingItemOrderedList[position]

        Picasso.get().load(currentItem.img).into(holder.ivItemOrdered);
        holder.tvOrderedTitle.text = currentItem.title
        holder.tvOrderedQuantity.text = "Quantity: " + currentItem.quantity.toString()
        holder.tvOrderedEstimateTime.text = "Estimate Receive Time: " + currentItem.receiveTime

        holder.btnCancelItemOrdered.setOnClickListener {

        }

    }

    override fun getItemCount(): Int {
        return housekeepingItemOrderedList.size
    }
}