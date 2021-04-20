package com.example.mad_assignment.Staff.Staff_Fragments.staff_housekeeping.Main.Model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mad_assignment.Customer.Customer_Fragments.cust_housekeeping.Class.LaundryService
import com.example.mad_assignment.Customer.Customer_Fragments.cust_housekeeping.Class.RoomCleaningService
import com.google.firebase.database.*

class StaffHousekeepingAvailableServicesModel : ViewModel() {

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

        val query: Query = FirebaseDatabase.getInstance().getReference("Housekeeping")
                .child(servicesType).child("ServicesAvailable").child(date).orderByChild("date")
                .equalTo(date)

        //Check got data or not
        query.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    roomCleaningServicesList.clear()
                    laundryServicesList.clear()

                    for(i in snapshot.children){
                        if (servicesType == "Room Cleaning") {

                            val roomService = i.getValue(RoomCleaningService::class.java)!!

                            //Prevent duplicate data
                            if(!roomCleaningServicesList.contains(roomService)){
                                // add the item and pass to observer for the adapter
                                roomCleaningServicesList.add(roomService)
                            }

                        } else {
                            val laundryService = i.getValue(LaundryService::class.java)!!

                            //Prevent duplicate data
                            if(!laundryServicesList.contains(laundryService)){
                                // add the item and pass to observer for the adapter
                                laundryServicesList.add(laundryService)
                            }
                        }
                    }

                } else {
                    roomCleaningServicesList.clear()
                    laundryServicesList.clear()
                }

                if(servicesType == "Room Cleaning"){
                    _status.value = roomCleaningServicesList.size > 0

                    _roomCleaningServices.value = roomCleaningServicesList

                }else{
                    _status.value = laundryServicesList.size > 0

                    _laundryServices.value = laundryServicesList
                }

            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }
}