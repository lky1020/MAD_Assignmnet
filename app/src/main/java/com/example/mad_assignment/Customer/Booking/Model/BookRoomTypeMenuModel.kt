package com.example.mad_assignment.Customer.Booking.Model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mad_assignment.Customer.Booking.Class.RoomType
import com.google.firebase.database.*

class BookRoomTypeMenuModel : ViewModel()  {

    private val _bookRoomType: MutableLiveData<ArrayList<RoomType>> = MutableLiveData<ArrayList<RoomType>>()
    private val bookRoomTypeList = ArrayList<RoomType>()

    val roomType : LiveData<ArrayList<RoomType>>
        get()= _bookRoomType

    fun getBookRoomTypeList(): MutableLiveData<ArrayList<RoomType>> {
        return _bookRoomType
    }

    //Status
    private var _status = MutableLiveData<Boolean>()

    val status : LiveData<Boolean>
        get()= _status

    fun getStatus(): MutableLiveData<Boolean> {
        return _status
    }

    fun fetchBookRoomType(){

        val ref: DatabaseReference = FirebaseDatabase.getInstance().getReference("Room")

        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    // Clear bookRoomTypeList to prevent duplicate item appear
                    bookRoomTypeList.clear()

                    for(i in snapshot.children){
                        // get the item from firebase
                        val roomType: RoomType? = i.getValue(RoomType::class.java)

                        // add the item and pass to observer for the adapter
                        bookRoomTypeList.add(roomType!!)
                        _bookRoomType.value = bookRoomTypeList
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }
}