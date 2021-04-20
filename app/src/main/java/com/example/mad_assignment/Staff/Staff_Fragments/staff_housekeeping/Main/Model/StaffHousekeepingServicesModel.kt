package com.example.mad_assignment.Staff.Staff_Fragments.staff_housekeeping.Main.Model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mad_assignment.Customer.Customer_Fragments.cust_housekeeping.Class.Housekeeping
import com.google.firebase.database.*

class StaffHousekeepingServicesModel : ViewModel() {

    private var ref: DatabaseReference = FirebaseDatabase.getInstance().getReference("Housekeeping")

    private val _housekeeping = MutableLiveData<ArrayList<Housekeeping>>()
    private val housekeepingList = ArrayList<Housekeeping>()

    val housekeeping : LiveData<ArrayList<Housekeeping>>
        get()= _housekeeping

    fun getHousekeepingList(): MutableLiveData<ArrayList<Housekeeping>> {
        return _housekeeping
    }

    fun retrieveHousekeepingFromDB(){

        ref.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    // Clear housekeepingList to prevent duplicate item appear
                    housekeepingList.clear()

                    for(i in snapshot.children){
                        // get the item from firebase
                        /*
                            ItemAvailable, ServicesAvailable, ServicesBooked will not include into houskeeping class
                         */
                        val housekeeping = i.getValue(Housekeeping::class.java)

                        // add the item and pass to observer for the adapter
                        housekeepingList.add(housekeeping!!)
                        _housekeeping.value = housekeepingList
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }
}