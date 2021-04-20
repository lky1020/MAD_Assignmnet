package com.example.mad_assignment.Staff.room.Class

import com.example.mad_assignment.Customer.Booking.Class.RoomType

data class Room (
         var roomNo: String? = null,
         var roomType: RoomType? = RoomType(),
         var status: String? = null
)