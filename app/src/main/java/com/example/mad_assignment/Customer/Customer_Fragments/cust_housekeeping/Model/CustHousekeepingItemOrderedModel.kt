package com.example.mad_assignment.Customer.Customer_Fragments.cust_housekeeping.Model

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mad_assignment.Class.User
import com.example.mad_assignment.Customer.Customer_Fragments.cust_housekeeping.Class.HousekeepingOrderedItem
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class CustHousekeepingItemOrderedModel() : ViewModel()  {

    private val _housekeepingItemOrdered = MutableLiveData<ArrayList<HousekeepingOrderedItem>>()
    private val housekeepingItemorderedList = ArrayList<HousekeepingOrderedItem>()

    val housekeepingItemOrdered : LiveData<ArrayList<HousekeepingOrderedItem>>
        get()= _housekeepingItemOrdered

    fun gethousekeepingItemOrderedList(): MutableLiveData<ArrayList<HousekeepingOrderedItem>> {
        return _housekeepingItemOrdered
    }

    //Status
    private var _status = MutableLiveData<Boolean>()

    val status : LiveData<Boolean>
        get()= _status

    fun getStatus(): MutableLiveData<Boolean> {
        return _status
    }

    fun retrieveHousekeepingItemOrderedFromDB(serviceType: String){
        val uid = FirebaseAuth.getInstance().uid
        val ref = FirebaseDatabase.getInstance().getReference("/User/$uid")

        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            @RequiresApi(Build.VERSION_CODES.O)
            override fun onDataChange(snapshot: DataSnapshot) {
                val currentUser = snapshot.getValue(User::class.java)!!

                val query: Query = FirebaseDatabase.getInstance().getReference("Housekeeping")
                    .child(serviceType)
                    .child("ItemOrdered")
                    .orderByChild("user")
                    .equalTo(currentUser.name)

                query.addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if(snapshot.exists()){
                            for (i in snapshot.children) {
                                // get the item from firebase
                                val housekeepingItemOrdered = i.getValue(HousekeepingOrderedItem::class.java)

                                if (!housekeepingItemorderedList.contains(housekeepingItemOrdered)) {
                                    // add the item and pass to observer for the adapter
                                    housekeepingItemorderedList.add(housekeepingItemOrdered!!)
                                }
                            }
                        }else{
                            // To prevent previous data been removed (>= 2 firebase query)
                            if(housekeepingItemorderedList.size == 0){
                                housekeepingItemorderedList.clear()
                            }

                        }

                        _status.value = housekeepingItemorderedList.size > 0

                        _housekeepingItemOrdered.value = housekeepingItemorderedList
                    }

                    override fun onCancelled(error: DatabaseError) {

                    }
                })
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }
}