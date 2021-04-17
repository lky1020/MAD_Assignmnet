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

    //Status
    private var _status = MutableLiveData<Boolean>()

    val status : LiveData<Boolean>
        get()= _status

    fun getStatus(): MutableLiveData<Boolean> {
        return _status
    }

    fun retrieveCheckInOutFromDB(status: String){

        val ref: DatabaseReference = FirebaseDatabase.getInstance().getReference("Reservation")

        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){

                    checkInOutList.clear()

                    for(i in snapshot.children){

                        val query: Query = FirebaseDatabase.getInstance().getReference("Reservation")
                                .child(i.key.toString())
                                .orderByChild("status")
                                .equalTo(status)

                        query.addValueEventListener(object : ValueEventListener {
                            override fun onDataChange(snapshot: DataSnapshot) {

                                if(snapshot.exists()){
                                    for (j in snapshot.children) {
                                        // get the item from firebase
                                        val reservationInfo = j.getValue(Reservation::class.java)

                                        if (!checkInOutList.contains(reservationInfo)) {
                                            // add the item and pass to observer for the adapter
                                            checkInOutList.add(reservationInfo!!)
                                        }
                                    }
                                }

                                _status.value = checkInOutList.size > 0

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