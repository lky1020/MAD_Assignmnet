package com.example.mad_assignment.Staff.facility.Model

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mad_assignment.Staff.facility.Class.Facility
import com.google.firebase.database.*

class FacilityViewModel : ViewModel() {
    private val mutableData = MutableLiveData<MutableList<Facility>>()

    private val _facility = MutableLiveData<ArrayList<Facility>>()
    private val facilityList = ArrayList<Facility>()

    val room : LiveData<ArrayList<Facility>>
        get()= _facility

    fun getFacility(): MutableLiveData<ArrayList<Facility>> {
        return _facility
    }

    //Status
    private var _status = MutableLiveData<Boolean>()

    val status : LiveData<Boolean>
        get()= _status

    fun getStatus(): MutableLiveData<Boolean> {
        return _status
    }

    fun fetchFacilityData(){

        val ref: DatabaseReference = FirebaseDatabase.getInstance().getReference("Facility")

        ref.addValueEventListener(object : ValueEventListener {
            @RequiresApi(Build.VERSION_CODES.O)
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    // prevent duplicate
                    facilityList.clear()

                    for(i in snapshot.children){
                        // get the item from firebase
                        val facility = i.getValue(Facility::class.java)

                        // add the item and pass to observer for the adapter
                        facilityList.add(facility!!)
                        _facility.value = facilityList
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }
}