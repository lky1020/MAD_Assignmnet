package com.example.mad_assignment.Customer.Customer_Fragments.cust_housekeeping

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.mad_assignment.Customer.Customer_Fragments.cust_housekeeping.Class.HousekeepingService
import com.example.mad_assignment.Customer.Customer_Fragments.cust_housekeeping.Services.CustHousekeepingAvailableServicesActivity
import com.example.mad_assignment.R

class AvailableHousekeepingServicesAdadpter (private var availableHousekeepingServicesList: ArrayList<HousekeepingService>, private var mContext: FragmentActivity): RecyclerView.Adapter<AvailableHousekeepingServicesAdadpter.AvailableHousekeepingServicesViewHolder>(){

    class AvailableHousekeepingServicesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val tvTime: TextView = itemView.findViewById(R.id.tv_services_time)
        val tvStatus: TextView = itemView.findViewById(R.id.tv_services_status)
        val viewServiceStatus: View = itemView.findViewById(R.id.view_services_status)
        val btnServiceBook: View = itemView.findViewById(R.id.btn_service_book)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AvailableHousekeepingServicesViewHolder {
        val itemView = LayoutInflater.from(mContext)
            .inflate(R.layout.cardview_item_available_housekeeping_services, parent, false)

        return AvailableHousekeepingServicesViewHolder(itemView)
    }

    @SuppressLint("SetTextI18n", "ResourceAsColor")
    override fun onBindViewHolder(holder: AvailableHousekeepingServicesViewHolder, position: Int) {
        val currentItem = availableHousekeepingServicesList[position]

        holder.tvTime.text = "Time: " + currentItem.timeFrom + " - " + currentItem.timeTo
        holder.tvStatus.text = "Status: " + currentItem.status

        if(currentItem.status == "Not Available"){
            holder.viewServiceStatus.backgroundTintList = ContextCompat.getColorStateList(mContext, R.color.red);

            holder.btnServiceBook.alpha = 0.25f
            holder.btnServiceBook.isClickable = false
            holder.btnServiceBook.isEnabled = false

        }else{
            holder.viewServiceStatus.backgroundTintList = ContextCompat.getColorStateList(mContext, R.color.green);

            holder.btnServiceBook.alpha = 1.0f
            holder.btnServiceBook.isClickable = true
            holder.btnServiceBook.isEnabled = true
        }

        //Set onclicklisterner
        holder.btnServiceBook.setOnClickListener {
            Toast.makeText(mContext, "Hihi", Toast.LENGTH_SHORT).show()
        }
    }

    override fun getItemCount(): Int {
        return availableHousekeepingServicesList.size
    }
}