package com.example.mad_assignment.Staff.Staff_Fragments.staff_manager.Model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mad_assignment.Class.Staff
import com.google.firebase.database.*

class StaffManagerModel : ViewModel()  {

    private val _staff = MutableLiveData<ArrayList<Staff>>()
    private val staffList = ArrayList<Staff>()

    val staff : LiveData<ArrayList<Staff>>
        get()= _staff

    fun getStaffList(): MutableLiveData<ArrayList<Staff>> {
        return _staff
    }

    //Status
    private var _status = MutableLiveData<Boolean>()

    val status : LiveData<Boolean>
        get()= _status

    fun getStatus(): MutableLiveData<Boolean>{
        return _status
    }

    fun retrieveOnlineStaffFromDB(status: String){

        val query: Query = FirebaseDatabase.getInstance().getReference("Staff")
                .orderByChild("status")
                .equalTo(status)

        query.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    staffList.clear()

                    for(i in snapshot.children){
                        val staff = i.getValue(Staff::class.java)

                        // add the item and pass to observer for the adapter
                        staffList.add(staff!!)
                    }
                }else{
                    staffList.clear()
                }

                _status.value = staffList.size > 0

                _staff.value = staffList
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }
}