package com.example.mad_assignment.Staff.room.Model

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mad_assignment.Staff.room.Class.Room
import com.google.firebase.database.*

/*class RoomsViewModel: ViewModel() {
    val repo = RoomRepo()

    fun fetchRoomData(): LiveData<MutableList<Room>> {
        val mutableData = MutableLiveData<MutableList<Room>>()
        repo.getRoomData().observeForever { roomList ->
            mutableData.value = roomList
        }

        return mutableData
    }
}

class RoomRepo {

    fun getRoomData(): LiveData<MutableList<Room>> {
        val mutableData = MutableLiveData<MutableList<Room>>()

        FirebaseFirestore.getInstance().collection("Rooms").get().addOnSuccessListener { result ->
            val listData: MutableList<Room> = mutableListOf<Room>()
            for(document: QueryDocumentSnapshot in result){
                val roomNo: String? = document.getString("roomNo")
                val status: String? = document.getString("status")
                val room = Room(roomNo!!, status!!)
                listData.add(room)
            }
            mutableData.value = listData
        }
        return mutableData
    }
}*/

class RoomsViewModel() : ViewModel()  {

    private val mutableData = MutableLiveData<MutableList<Room>>()

    private val _room = MutableLiveData<ArrayList<Room>>()
    private val roomList = ArrayList<Room>()

    val room : LiveData<ArrayList<Room>>
        get()= _room

    fun getRoomList(): MutableLiveData<ArrayList<Room>> {
        return _room
    }

    //Status
    private var _status = MutableLiveData<Boolean>()

    val status : LiveData<Boolean>
        get()= _status

    fun getStatus(): MutableLiveData<Boolean> {
        return _status
    }

    fun fetchRoomData(roomType: String){

        val ref: DatabaseReference = FirebaseDatabase.getInstance().getReference("Room").child(roomType).child("Rooms")

        ref.addValueEventListener(object : ValueEventListener {
            @RequiresApi(Build.VERSION_CODES.O)
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    // prevent duplicate
                    roomList.clear()

                    for(i in snapshot.children){
                        // get the item from firebase
                        val room = i.getValue(Room::class.java)

                        // add the item and pass to observer for the adapter
                        roomList.add(room!!)
                        _room.value = roomList
                    }
                }
            }

            /*for(document: QueryDocumentSnapshot in result){
                val roomNo: String? = document.getString("roomNo")
                val status: String? = document.getString("status")
                val room = Room(roomNo!!, status!!)
                listData.add(room)
            }
            mutableData.value = listData*/

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }
}