package com.example.mad_assignment.Staff.Staff_Fragments.staff_housekeeping.Main.Model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mad_assignment.Customer.Customer_Fragments.cust_housekeeping.Class.HousekeepingItem
import com.google.firebase.database.*

class StaffHousekeepingItemModel: ViewModel() {
    private val _housekeepingOrdered = MutableLiveData<ArrayList<HousekeepingItem>>()
    private val housekeepingItemList = ArrayList<HousekeepingItem>()

    val housekeepingItem : LiveData<ArrayList<HousekeepingItem>>
        get()= _housekeepingOrdered

    fun gethousekeepingItemList(): MutableLiveData<ArrayList<HousekeepingItem>> {
        return _housekeepingOrdered
    }

    //Status
    private var _status = MutableLiveData<Boolean>()

    val status : LiveData<Boolean>
        get()= _status

    fun getStatus(): MutableLiveData<Boolean> {
        return _status
    }

    fun retrieveHousekeepingItemFromDB(serviceType: String){

        val ref: DatabaseReference = FirebaseDatabase.getInstance().getReference("Housekeeping")
            .child(serviceType).child("ItemAvailable")

        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){

                    housekeepingItemList.clear()

                    for (i in snapshot.children) {
                        // get the item from firebase
                        val housekeepingItem = i.getValue(HousekeepingItem::class.java)

                        if (!housekeepingItemList.contains(housekeepingItem)) {
                            // add the item and pass to observer for the adapter
                            housekeepingItemList.add(housekeepingItem!!)
                        }
                    }
                }else{
                    housekeepingItemList.clear()
                }

                _status.value = housekeepingItemList.size > 0

                _housekeepingOrdered.value = housekeepingItemList
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }
}