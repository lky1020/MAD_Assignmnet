package com.example.mad_assignment.Customer.Customer_Fragments.cust_housekeeping.Adapter

import android.annotation.SuppressLint
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
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


class AvailableRoomCleaningServicesAdadpter(private var availableRoomCleaningServicesList: ArrayList<RoomCleaningService>, private var mContext: FragmentActivity, private val servicesType: String, private val imgUrl: String): RecyclerView.Adapter<AvailableRoomCleaningServicesAdadpter.AvailableHousekeepingServicesViewHolder>(){

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
            bookServiceForCurrentUser(currentItem, position)
        }
    }

    override fun getItemCount(): Int {
        return availableRoomCleaningServicesList.size
    }

    private fun bookServiceForCurrentUser(currentItem: RoomCleaningService, position: Int){
        val uid = FirebaseAuth.getInstance().uid
        val ref = FirebaseDatabase.getInstance().getReference("/User/$uid")

        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            @RequiresApi(Build.VERSION_CODES.O)
            override fun onDataChange(snapshot: DataSnapshot) {
                val currentUser = snapshot.getValue(User::class.java)!!

                val timeZone: ZoneId = ZoneId.of("Asia/Kuala_Lumpur")
                val now: LocalTime = LocalTime.now(timeZone)
                val dtf: DateTimeFormatter = DateTimeFormatter.ofPattern("hh:mm:ss a")

                val bookedTime = currentItem.date + " " + now.format(dtf)

                // Book Service for User
                val myRef = FirebaseDatabase.getInstance().getReference("Housekeeping").child(servicesType).child("ServicesBooked").child(currentUser.name + " - " + bookedTime)
                val serviceInfo = BookedHousekeepingService(
                        servicesType,
                        imgUrl,
                        currentUser.name,
                        currentItem.date,
                        currentItem.timeFrom,
                        currentItem.timeTo,
                        bookedTime)

                myRef.setValue(serviceInfo)

                //Update services status
                updateServices(currentItem, position)

                Toast.makeText(mContext, "Services Booked", Toast.LENGTH_SHORT).show()

            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

    private fun updateServices(currentItem: RoomCleaningService, position: Int) {
        val query: Query = FirebaseDatabase.getInstance().getReference("Housekeeping")
                .child(servicesType).child("ServicesAvailable").child(currentItem.date)
                .orderByChild("timeFrom")
                .equalTo(currentItem.timeFrom)

        query.addListenerForSingleValueEvent(object : ValueEventListener {
            @RequiresApi(Build.VERSION_CODES.O)
            @SuppressLint("SimpleDateFormat")
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (i in snapshot.children) {
                        val currentRoomCleaningService = i.getValue(RoomCleaningService::class.java)

                        val dbTimeTo = currentRoomCleaningService?.timeTo

                        if(dbTimeTo.equals(currentItem.timeTo)){
                            val updateRoomCleaningService = RoomCleaningService(currentItem.date, currentItem.timeFrom, currentItem.timeTo, "Not Available")
                            snapshot.ref.child(position.toString()).setValue(updateRoomCleaningService)
                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }
}