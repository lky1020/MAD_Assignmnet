package com.example.mad_assignment.Customer.Customer_Fragments.cust_housekeeping.Model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mad_assignment.Customer.Customer_Fragments.cust_housekeeping.Class.HousekeepingItem
import com.google.firebase.database.*

class CustHousekeepingItemModel() : ViewModel()  {

    private val _housekeepingItem = MutableLiveData<ArrayList<HousekeepingItem>>()
    private val housekeepingItemList = ArrayList<HousekeepingItem>()

    val housekeepingItem : LiveData<ArrayList<HousekeepingItem>>
        get()= _housekeepingItem

    fun getHousekeepingItemList(): MutableLiveData<ArrayList<HousekeepingItem>> {
        return _housekeepingItem
    }

    fun retrieveHousekeepingItemFromDB(serviceType: String){

        val ref: DatabaseReference = FirebaseDatabase.getInstance().getReference("Housekeeping").child(serviceType).child("ItemAvailable")

        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    // Clear housekeepingList to prevent duplicate item appear
                    housekeepingItemList.clear()

                    for(i in snapshot.children){
                        // get the item from firebase
                        val housekeepingItem = i.getValue(HousekeepingItem::class.java)

                        // add the item and pass to observer for the adapter
                        housekeepingItemList.add(housekeepingItem!!)
                        _housekeepingItem.value = housekeepingItemList
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }
}