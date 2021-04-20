package com.example.mad_assignment.Staff.Staff_Fragments.staff_housekeeping.Main.Adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.mad_assignment.Customer.Customer_Fragments.cust_housekeeping.Class.RoomCleaningService
import com.example.mad_assignment.R
import com.google.firebase.database.*


class StaffRoomCleaningServicesAdadpter(private var availableRoomCleaningServicesList: ArrayList<RoomCleaningService>, private var mContext: FragmentActivity): RecyclerView.Adapter<StaffRoomCleaningServicesAdadpter.StaffHousekeepingRoomServicesViewHolder>(){

    class StaffHousekeepingRoomServicesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
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
    ): StaffHousekeepingRoomServicesViewHolder {
        val itemView = LayoutInflater.from(mContext)
            .inflate(R.layout.cardview_staff_housekeeping_services, parent, false)

        return StaffHousekeepingRoomServicesViewHolder(itemView)
    }

    @SuppressLint("SetTextI18n", "ResourceAsColor")
    override fun onBindViewHolder(holder: StaffHousekeepingRoomServicesViewHolder, position: Int) {
        val currentItem = availableRoomCleaningServicesList[position]

        holder.tvRemark.visibility = View.GONE

        val params: RelativeLayout.LayoutParams = RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT)
        params.addRule(RelativeLayout.END_OF, R.id.view_services_status);
        params.setMargins(0, 35, 0, 0)
        holder.tvTime.layoutParams = params

        holder.tvTime.text = "Time: " + currentItem.timeFrom + " - " + currentItem.timeTo
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
        return availableRoomCleaningServicesList.size
    }

    private fun updateServices(currentItem: RoomCleaningService, position: Int) {
        val query: Query = FirebaseDatabase.getInstance().getReference("Housekeeping")
            .child("Room Cleaning").child("ServicesAvailable").child(currentItem.date)
            .orderByChild("timeFrom")
            .equalTo(currentItem.timeFrom)

        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (i in snapshot.children) {
                        val currentRoomCleaningService = i.getValue(RoomCleaningService::class.java)

                        val dbTimeTo = currentRoomCleaningService?.timeTo
                        var status = ""

                        status = if(currentItem.status == "Not Available"){
                            "Available"
                        }else{
                            "Not Available"
                        }

                        if (dbTimeTo.equals(currentItem.timeTo)) {
                            val updateRoomCleaningService = RoomCleaningService(
                                currentItem.date,
                                currentItem.timeFrom,
                                currentItem.timeTo,
                                status
                            )
                            snapshot.ref.child(position.toString()).setValue(updateRoomCleaningService)

                            Toast.makeText(mContext, "Status Updated", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

    private fun deleteServices(currentItem: RoomCleaningService, position: Int){

        // Delete service for User
        val myRef = FirebaseDatabase.getInstance().getReference("Housekeeping")
            .child("Room Cleaning").child("ServicesAvailable")
            .child(currentItem.date)

        myRef.child(position.toString()).removeValue()

        availableRoomCleaningServicesList.remove(currentItem);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, availableRoomCleaningServicesList.size);

        Toast.makeText(mContext, "Room Services Deleted", Toast.LENGTH_SHORT).show()
    }
}