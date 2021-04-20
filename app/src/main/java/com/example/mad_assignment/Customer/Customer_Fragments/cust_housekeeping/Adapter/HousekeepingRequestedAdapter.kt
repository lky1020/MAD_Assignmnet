package com.example.mad_assignment.Customer.Customer_Fragments.cust_housekeeping.Adapter

import android.annotation.SuppressLint
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.mad_assignment.Class.User
import com.example.mad_assignment.Customer.Customer_Fragments.cust_housekeeping.Class.BookedHousekeepingService
import com.example.mad_assignment.Customer.Customer_Fragments.cust_housekeeping.Class.LaundryService
import com.example.mad_assignment.Customer.Customer_Fragments.cust_housekeeping.Class.RoomCleaningService
import com.example.mad_assignment.R
import com.google.android.material.imageview.ShapeableImageView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import java.text.SimpleDateFormat
import java.util.*

class HousekeepingRequestedAdapter(
    private var housekeepingRequestedList: ArrayList<BookedHousekeepingService>,
    private var mContext: FragmentActivity
): RecyclerView.Adapter<HousekeepingRequestedAdapter.HousekeepingRequestedViewHolder>() {

    class HousekeepingRequestedViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val ivService: ShapeableImageView = itemView.findViewById(R.id.iv_service_requested)
        val tvServiceTitle: TextView = itemView.findViewById(R.id.tv_service_title)
        val tvServiceDate: TextView = itemView.findViewById(R.id.tv_service_date)
        val tvServiceTime: TextView = itemView.findViewById(R.id.tv_service_time)
        val tvBookedTime: TextView = itemView.findViewById(R.id.tv_service_booked_time)
        val btnCancelRequest: ImageView = itemView.findViewById(R.id.btn_cancel_request)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): HousekeepingRequestedViewHolder {
        val itemView = LayoutInflater.from(mContext).inflate(R.layout.cardview_item_housekeeping_services_requested, parent, false)

        return HousekeepingRequestedViewHolder(itemView)
    }

    @SuppressLint("SetTextI18n", "SimpleDateFormat")
    override fun onBindViewHolder(holder: HousekeepingRequestedViewHolder, position: Int) {
        val currentItem = housekeepingRequestedList[position]

        // Process date
        val retrieveDate = currentItem.date
        val dayOfMonth = retrieveDate.substring(4, 6).toInt()
        val month = convertMonth(retrieveDate.substring(0, 3))
        val year = retrieveDate.substring(7).toInt()

        val simpleDateFormat = SimpleDateFormat("EEEE")
        val date = Date(year, month, dayOfMonth)
        val dayString = simpleDateFormat.format(date).substring(0, 3)

        val monthString = convertMonth(month)

        Picasso.get().load(currentItem.serviceImg).into(holder.ivService)
        holder.tvServiceTitle.text = currentItem.serviceType
        holder.tvServiceDate.text = "Date: $dayString, $monthString $dayOfMonth "
        holder.tvServiceTime.text = "Time: " + currentItem.timeFrom + " - " + currentItem.timeTo
        holder.tvBookedTime.text = "Book at " + currentItem.bookedTime

        holder.btnCancelRequest.setOnClickListener {
            deleteServicesRequested(currentItem, position)
            updateServices(currentItem)
        }
    }

    override fun getItemCount(): Int {
        return housekeepingRequestedList.size
    }

    private fun convertMonth(month: String): Int{
        when (month){
            "Jan" -> return 0
            "Feb" -> return 1
            "Mar" -> return 2
            "Apr" -> return 3
            "May" -> return 4
            "Jun" -> return 5
            "Jul" -> return 6
            "Aug" -> return 7
            "Sep" -> return 8
            "Oct" -> return 9
            "Nov" -> return 10
            "Dec" -> return 11
        }

        return 12
    }

    private fun convertMonth(month: Int): String{
        when (month){
            0 -> return "Jan"
            1 -> return "Feb"
            2 -> return "Mar"
            3 -> return "Apr"
            4 -> return "May"
            5 -> return "Jun"
            6 -> return "Jul"
            7 -> return "Aug"
            8 -> return "Sep"
            9 -> return "Oct"
            10 -> return "Nov"
            11 -> return "Dec"
        }

        return ""
    }

    private fun deleteServicesRequested(currentItem: BookedHousekeepingService, position: Int){
        val uid = FirebaseAuth.getInstance().uid
        val ref = FirebaseDatabase.getInstance().getReference("/User/$uid")

        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            @RequiresApi(Build.VERSION_CODES.O)
            override fun onDataChange(snapshot: DataSnapshot) {
                val currentUser = snapshot.getValue(User::class.java)!!

                // Order item for User
                val myRef = FirebaseDatabase.getInstance().getReference("Housekeeping").child(
                    currentItem.serviceType
                ).child("ServicesBooked").child(currentUser.name + " - " + currentItem.bookedTime)
                myRef.removeValue()

                housekeepingRequestedList.remove(currentItem)
                notifyItemRemoved(position)
                notifyItemRangeChanged(position, housekeepingRequestedList.size)

                Toast.makeText(mContext, "Service Booked Cancel", Toast.LENGTH_SHORT).show()
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

    private fun updateServices(currentItem: BookedHousekeepingService){

        val query = if(currentItem.serviceType == "Room Cleaning"){
            FirebaseDatabase.getInstance().getReference("Housekeeping")
                .child(currentItem.serviceType).child("ServicesAvailable").child(currentItem.date)
                .orderByChild("timeFrom")
                .equalTo(currentItem.timeFrom)
        }else{
            FirebaseDatabase.getInstance().getReference("Housekeeping")
                .child(currentItem.serviceType).child("ServicesAvailable").child(currentItem.date)
                .orderByChild("timePickUp")
                .equalTo(currentItem.timeFrom)
        }

        query.addListenerForSingleValueEvent (object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (i in snapshot.children) {
                        if (currentItem.serviceType == "Room Cleaning") {
                            val currentRoomCleaningService = i.getValue(RoomCleaningService::class.java)

                            val dbTimeTo = currentRoomCleaningService?.timeTo

                            if(currentRoomCleaningService != null){
                                if (dbTimeTo.equals(currentItem.timeTo)) {
                                    val updateRoomCleaningService = RoomCleaningService(
                                        currentRoomCleaningService.date,
                                        currentRoomCleaningService.timeFrom,
                                        currentRoomCleaningService.timeTo,
                                        "Available"
                                    )
                                    snapshot.ref.child(i.key.toString()).setValue(updateRoomCleaningService)
                                }
                            }


                        } else {
                            val currentLaundryService = i.getValue(LaundryService::class.java)

                            val dbCompleteTime = currentLaundryService?.timeComplete

                            if(currentLaundryService != null){
                                if (dbCompleteTime.equals(currentItem.timeTo)) {
                                    val updateLaundryService = LaundryService(
                                        currentLaundryService.date,
                                        currentLaundryService.timePickUp,
                                        currentLaundryService.timeComplete,
                                        "Available"
                                    )
                                    snapshot.ref.child(i.key.toString()).setValue(updateLaundryService)
                                }
                            }
                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })

    }

}