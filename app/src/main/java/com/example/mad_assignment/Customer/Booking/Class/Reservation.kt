package com.example.mad_assignment.Customer.Booking.Class

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDateTime
import java.util.*

data class Reservation @RequiresApi(Build.VERSION_CODES.O) constructor(
    var reservationID: String? = null,
    var uid: String? = null,
    var guest:Int? = 0,
    var checkInDate:Long? = 0,
    var checkOutDate:Long? = 0,
    var nights:Int? = 0,
    var breakfast:Int? = 0,
    var reservationDetail: ArrayList<ReservationDetail>? = null,
    var totalPrice: Double? = 0.0,
    var status: String? = null,
    var dateReserved: LocalDateTime? = null
)