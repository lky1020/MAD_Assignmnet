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
import com.example.mad_assignment.Customer.Customer_Fragments.cust_housekeeping.Class.LaundryService
import com.example.mad_assignment.R
import com.google.firebase.database.*

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
            updateServices(currentItem, position)
        }

        holder.ivDelete.setOnClickListener {
            deleteServices(currentItem, position)
        }
    }

    override fun getItemCount(): Int {
        return availableLaundryServicesList.size
    }

    private fun updateServices(currentItem: LaundryService, position: Int) {
        val query: Query = FirebaseDatabase.getInstance().getReference("Housekeeping")
            .child("Laundry Service").child("ServicesAvailable").child(currentItem.date)
            .orderByChild("timePickUp")
            .equalTo(currentItem.timePickUp)

        query.addListenerForSingleValueEvent(object : ValueEventListener {
            @RequiresApi(Build.VERSION_CODES.O)
            @SuppressLint("SimpleDateFormat")
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (i in snapshot.children) {
                        val currentLaundryService = i.getValue(LaundryService::class.java)

                        val dbCompleteTime = currentLaundryService?.timeComplete
                        var status = ""

                        status = if(currentItem.status == "Not Available"){
                            "Available"
                        }else{
                            "Not Available"
                        }

                        if(dbCompleteTime.equals(currentItem.timeComplete)){
                            val updateLaundryService = LaundryService(currentItem.date,currentItem.timePickUp, currentItem.timeComplete, status)
                            snapshot.ref.child(position.toString()).setValue(updateLaundryService)

                            Toast.makeText(mContext, "Status Updated", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

    private fun deleteServices(currentItem: LaundryService, position: Int){

        // Order item for User
        val myRef = FirebaseDatabase.getInstance().getReference("Housekeeping")
            .child("Laundry Service").child("ServicesAvailable")
            .child(currentItem.date)

        myRef.child(position.toString()).removeValue()

        availableLaundryServicesList.remove(currentItem);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, availableLaundryServicesList.size);

        Toast.makeText(mContext, "Laundry Services Deleted", Toast.LENGTH_SHORT).show()
    }
}