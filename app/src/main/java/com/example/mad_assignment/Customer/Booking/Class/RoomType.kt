package com.example.mad_assignment.Customer.Booking.Class

import java.util.ArrayList

data class RoomType(
    var roomID: String? = null,
    var roomType: String? = null,
    var occupancy: Int? = 0,
    var size: Int? = 0,
    var price: Double? = 0.0,
    var beds: String? = null,
    var img: String? = null,
)
