package com.example.mad_assignment.Customer_Fragments.cust_housekeeping

import android.app.Activity
import android.app.PendingIntent
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mad_assignment.Customer.Customer_Fragments.cust_housekeeping.Class.Housekeeping
import com.example.mad_assignment.MainActivity
import com.google.firebase.database.*

class CustHousekeepingModel() : ViewModel() {

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