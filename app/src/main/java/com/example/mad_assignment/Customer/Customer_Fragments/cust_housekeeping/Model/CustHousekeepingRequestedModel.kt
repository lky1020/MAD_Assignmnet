package com.example.mad_assignment.Customer.Customer_Fragments.cust_housekeepingRequested.Model

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mad_assignment.Class.User
import com.example.mad_assignment.Customer.Customer_Fragments.cust_housekeeping.Class.BookedHousekeepingService
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class CustHousekeepingRequestedModel() : ViewModel()  {

    private val _housekeepingRequested = MutableLiveData<ArrayList<BookedHousekeepingService>>()
    private val housekeepingRequestedList = ArrayList<BookedHousekeepingService>()

    val housekeeping : LiveData<ArrayList<BookedHousekeepingService>>
        get()= _housekeepingRequested

    fun gethousekeepingRequestedList(): MutableLiveData<ArrayList<BookedHousekeepingService>> {
        return _housekeepingRequested
    }

    //Status
    private var _status = MutableLiveData<Boolean>()

    val status : LiveData<Boolean>
        get()= _status

    fun getStatus(): MutableLiveData<Boolean>{
        return _status
    }

    fun retrieveHousekeepingRequestedFromDB(serviceType: String){
        val uid = FirebaseAuth.getInstance().uid
        val ref = FirebaseDatabase.getInstance().getReference("/User/$uid")

        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            @RequiresApi(Build.VERSION_CODES.O)
            override fun onDataChange(snapshot: DataSnapshot) {
                val currentUser = snapshot.getValue(User::class.java)!!

                val query: Query = FirebaseDatabase.getInstance().getReference("Housekeeping")
                        .child(serviceType)
                        .child("ServicesBooked")
                        .orderByChild("user")
                        .equalTo(currentUser.name)

                query.addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if(snapshot.exists()){
                            for (i in snapshot.children) {
                                // get the item from firebase
                                val housekeepingRequested = i.getValue(BookedHousekeepingService::class.java)

                                if (!housekeepingRequestedList.contains(housekeepingRequested)) {
                                    // add the item and pass to observer for the adapter
                                    housekeepingRequestedList.add(housekeepingRequested!!)
                                }
                            }
                        }else{
                            // To prevent previous data been removed (>= 2 firebase query)
                            if(housekeepingRequestedList.size == 0){
                                housekeepingRequestedList.clear()
                            }
                        }

                        _status.value = housekeepingRequestedList.size > 0

                        _housekeepingRequested.value = housekeepingRequestedList
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