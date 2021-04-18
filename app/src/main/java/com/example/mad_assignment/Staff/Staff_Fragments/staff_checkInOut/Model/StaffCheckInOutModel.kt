package com.example.mad_assignment.Staff.Staff_Fragments.staff_checkInOut.Model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mad_assignment.Customer.Booking.Class.Reservation
import com.example.mad_assignment.Customer.Customer_Fragments.cust_housekeeping.Class.HousekeepingItem
import com.google.firebase.database.*

class StaffCheckInOutModel() : ViewModel()   {
    private val _checkInOut = MutableLiveData<ArrayList<Reservation>>()
    private val checkInOutList = ArrayList<Reservation>()

    val checkInOut : LiveData<ArrayList<Reservation>>
        get()= _checkInOut

    fun getCheckInOutList(): MutableLiveData<ArrayList<Reservation>> {
        return _checkInOut
    }

    fun retrieveCheckInOutFromDB(status: String, date: String){

        val ref: DatabaseReference = FirebaseDatabase.getInstance().getReference("Reservation")

        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){

                    for(i in snapshot.children){

                        val query: Query = FirebaseDatabase.getInstance().getReference("Reservation")
                                .child(i.key.toString())
                                .orderByChild("status")
                                .equalTo(status)

                        query.addValueEventListener(object : ValueEventListener {
                            override fun onDataChange(snapshot: DataSnapshot) {

                                checkInOutList.clear()

                                if(snapshot.exists()){
                                    for (j in snapshot.children) {
                                        // get the item from firebase
                                        val reservationInfo = j.getValue(Reservation::class.java)

                                        if (reservationInfo != null) {
                                            if(status == "paid") {
                                                if (reservationInfo.checkInDate == date && !checkInOutList.contains(reservationInfo)) {
                                                    // add the item and pass to observer for the adapter
                                                    checkInOutList.add(reservationInfo)
                                                }
                                            }else if(status == "check in"){
                                                if (reservationInfo.checkOutDate == date && !checkInOutList.contains(reservationInfo)) {
                                                    // add the item and pass to observer for the adapter
                                                    checkInOutList.add(reservationInfo)
                                                }
                                            }
                                        }
                                    }
                                }else{
                                    checkInOutList.clear()
                                }

                                _checkInOut.value = checkInOutList
                            }

                            override fun onCancelled(error: DatabaseError) {

                            }
                        })
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }
}