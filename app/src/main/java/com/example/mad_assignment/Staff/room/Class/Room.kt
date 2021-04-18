package com.example.mad_assignment.Staff.room.Class

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModel
import com.example.mad_assignment.Customer.Booking.Class.RoomType
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

 data class Room (
         var roomNo: String? = null,
         var roomType: RoomType? = RoomType(),
         var status: String? = null
)