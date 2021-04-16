package com.example.mad_assignment.Customer.Booking.Class

data class ReservationDetail(
    var roomType: RoomType? = null,
    var qty: Int = 0,
    var subtotal: Double? = 0.0
)