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

    fun retrieveHousekeepingServicesFromDB(servicesType: String){

        val ref: DatabaseReference = FirebaseDatabase.getInstance().getReference("Housekeeping")
                .child(servicesType).child("ServicesAvailable").child("Apr 17 2021")

        ref.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                if (servicesType == "Room Cleaning") {
                    val roomService = snapshot.getValue(RoomCleaningService::class.java)!!

                    // add the item and pass to observer for the adapter
                    roomCleaningServicesList.add(roomService)
                    _roomCleaningServices.value = roomCleaningServicesList

                } else {
                    val laundryService = snapshot.getValue(LaundryService::class.java)!!

                    // add the item and pass to observer for the adapter
                    laundryServicesList.add(laundryService)
                    _laundryServices.value = laundryServicesList
                }
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                TODO("Not yet implemented")
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                TODO("Not yet implemented")
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                TODO("Not yet implemented")
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })

        //            override fun onDataChange(snapshot: DataSnapshot) {
//                if(snapshot.exists()){
//                    // Clear list to prevent duplicate item appear
//                    roomCleaningServicesList.clear()
//                    laundryServicesList.clear()
//
//                    for(i in snapshot.children){
//
//                        val roomServiceList: ArrayList<RoomCleaningService> = i.getValue(RoomCleaningService::class.java)!!
//
//                        for(j in roomServiceList){
//                            if(servicesType == "Room Cleaning"){
//                                val roomService = j.getValue(RoomCleaningService::class.java)!!
//
//                                // add the item and pass to observer for the adapter
//                                roomCleaningServicesList.add(roomService)
//                                _roomCleaningServices.value = roomCleaningServicesList
//
//                            }else{
//                                val laundryService = j.getValue(LaundryService::class.java)!!
//
//                                // add the item and pass to observer for the adapter
//                                laundryServicesList.add(laundryService)
//                                _laundryServices.value = laundryServicesList
//                            }
//                        }
//                    }
//                }
//            }
//
//            override fun onCancelled(error: DatabaseError) {
//
//            }
//        })
    }
}