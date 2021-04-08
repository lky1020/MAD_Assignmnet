package com.example.mad_assignment.Customer.Customer_Fragments.cust_housekeeping.Adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.mad_assignment.Customer.Customer_Fragments.cust_housekeeping.Class.LaundryService
import com.example.mad_assignment.R

class AvailableLaundryServicesAdadpter(private var availableLaundryServicesList: ArrayList<LaundryService>, private var mContext: FragmentActivity, val selectedDate: String): RecyclerView.Adapter<AvailableLaundryServicesAdadpter.AvailableHousekeepingServicesViewHolder>(){
    
    class AvailableHousekeepingServicesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val tvTime: TextView = itemView.findViewById(R.id.tv_services_time)
        val tvRemark: TextView = itemView.findViewById(R.id.tv_services_remark)
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
        val currentItem = availableLaundryServicesList[position]

        holder.tvRemark.visibility = View.VISIBLE

        holder.tvTime.text = "Pick-Up: " + currentItem.timePickUp
        holder.tvRemark.text = "Complete: " + selectedDate + "," + currentItem.timeComplete
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
        return availableLaundryServicesList.size
    }
}