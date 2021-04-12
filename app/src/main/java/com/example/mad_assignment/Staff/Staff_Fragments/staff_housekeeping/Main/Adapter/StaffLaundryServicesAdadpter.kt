package com.example.mad_assignment.Staff.Staff_Fragments.staff_housekeeping.Main.Adapter

import android.annotation.SuppressLint
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.mad_assignment.Class.User
import com.example.mad_assignment.Customer.Customer_Fragments.cust_housekeeping.Class.BookedHousekeepingService
import com.example.mad_assignment.Customer.Customer_Fragments.cust_housekeeping.Class.LaundryService
import com.example.mad_assignment.Customer.Customer_Fragments.cust_housekeeping.Class.RoomCleaningService
import com.example.mad_assignment.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class StaffLaundryServicesAdadpter(private var availableLaundryServicesList: ArrayList<LaundryService>, private var mContext: FragmentActivity): RecyclerView.Adapter<StaffLaundryServicesAdadpter.StaffHousekeepingLaundryServicesViewHolder>(){
    
    class StaffHousekeepingLaundryServicesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val tvTime: TextView = itemView.findViewById(R.id.tv_services_time)
        val tvRemark: TextView = itemView.findViewById(R.id.tv_services_remark)
        val tvStatus: TextView = itemView.findViewById(R.id.tv_services_status)
        val viewServiceStatus: View = itemView.findViewById(R.id.view_services_status)
        val ivEdit: ImageView = itemView.findViewById(R.id.iv_service_edit)
        val ivDelete: ImageView = itemView.findViewById(R.id.iv_service_delete)
    }

    override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
    ): StaffHousekeepingLaundryServicesViewHolder {
        val itemView = LayoutInflater.from(mContext)
                .inflate(R.layout.cardview_staff_housekeeping_services, parent, false)

        return StaffHousekeepingLaundryServicesViewHolder(itemView)
    }

    @SuppressLint("SetTextI18n", "ResourceAsColor")
    override fun onBindViewHolder(holder: StaffHousekeepingLaundryServicesViewHolder, position: Int) {
        val currentItem = availableLaundryServicesList[position]

        holder.tvRemark.visibility = View.VISIBLE

        holder.tvTime.text = "Pick-Up: " + currentItem.timePickUp
        holder.tvRemark.text = "Complete: " + currentItem.date + "," + currentItem.timeComplete
        holder.tvStatus.text = "Status: " + currentItem.status

        if(currentItem.status == "Not Available"){
            holder.viewServiceStatus.backgroundTintList = ContextCompat.getColorStateList(mContext, R.color.red);

        }else{
            holder.viewServiceStatus.backgroundTintList = ContextCompat.getColorStateList(mContext, R.color.green);
        }

        //Set onclicklisterner
        holder.ivEdit.setOnClickListener {
            //TODO
        }

        holder.ivDelete.setOnClickListener {
            //TODO
        }
    }

    override fun getItemCount(): Int {
        return availableLaundryServicesList.size
    }
}