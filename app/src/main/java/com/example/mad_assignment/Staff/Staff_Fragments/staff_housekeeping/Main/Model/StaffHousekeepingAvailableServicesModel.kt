package com.example.mad_assignment.Staff.Staff_Fragments.staff_housekeeping.Main.Model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mad_assignment.Customer.Customer_Fragments.cust_housekeeping.Class.Housekeeping
import com.example.mad_assignment.Customer.Customer_Fragments.cust_housekeeping.Class.LaundryService
import com.example.mad_assignment.Customer.Customer_Fragments.cust_housekeeping.Class.RoomCleaningService
import com.google.firebase.database.*

class StaffHousekeepingAvailableServicesModel() : ViewModel() {

    //Room Cleaning
    private val _roomCleaningServices = MutableLiveData<ArrayList<RoomCleaningService>>()
    private val roomCleaningServicesList = ArrayList<RoomCleaningService>()

    val roomCleaningServices : LiveData<ArrayList<RoomCleaningService>>
        get()= _roomCleaningServices

    fun getRoomCleaningServicesList(): MutableLiveData<ArrayList<RoomCleaningService>> {
        return _roomCleaningServices
    }

    //Laundry Services
    private val _laundryServices = MutableLiveData<ArrayList<LaundryService>>()
    private val laundryServicesList = ArrayList<LaundryService>()

    val laundryServices : LiveData<ArrayList<LaundryService>>
        get()= _laundryServices

    fun getLaundryServicesList(): MutableLiveData<ArrayList<LaundryService>> {
        return _laundryServices
    }

    //Status
    private var _status = MutableLiveData<Boolean>()

    val status : LiveData<Boolean>
        get()= _status

    fun getStatus(): MutableLiveData<Boolean>{
        return _status
    }

    fun retrieveHousekeepingServicesFromDB(servicesType: String, date: String){

        val ref: DatabaseReference = FirebaseDatabase.getInstance().getReference("Housekeeping")
                .child(servicesType).child("ServicesAvailable").child(date)

        //Check got data or not
        ref.addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {

                    _status.value = true

                } else {
                    roomCleaningServicesList.clear()
                    laundryServicesList.clear()

                    _status.value = false

                    if (servicesType == "Room Cleaning") {
                        _roomCleaningServices.value = roomCleaningServicesList

                    } else {
                        _laundryServices.value = laundryServicesList
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })

        //add and display data
        ref.addChildEventListener(object : ChildEventListener {

            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                if (servicesType == "Room Cleaning") {

                    val roomService = snapshot.getValue(RoomCleaningService::class.java)!!

                    //Prevent duplicate data
                    if(!roomCleaningServicesList.contains(roomService)){
                        // add the item and pass to observer for the adapter
                        roomCleaningServicesList.add(roomService)
                        _roomCleaningServices.value = roomCleaningServicesList
                    }

                } else {
                    val laundryService = snapshot.getValue(LaundryService::class.java)!!

                    //Prevent duplicate data
                    if(!laundryServicesList.contains(laundryService)){
                        // add the item and pass to observer for the adapter
                        laundryServicesList.add(laundryService)
                        _laundryServices.value = laundryServicesList
                    }

                }

            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {

            }

            override fun onChildRemoved(snapshot: DataSnapshot) {

            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {

            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }
}