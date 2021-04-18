package com.example.mad_assignment.Customer.Booking.Class

import android.os.Build
import androidx.annotation.RequiresApi
import java.util.*

@RequiresApi(Build.VERSION_CODES.O)
data class Reservation
constructor(
        var reservationID: String? = null,
        var uid: String? = null,
        var guest:Int? = 0,
        var checkInDate:String? = null,
        var checkOutDate:String? = null,
        var nights:Int? = 0,
        var breakfast:Int? = 0,
        var reservationDetail: ArrayList<ReservationDetail>? = null,
        var totalPrice: Double? = 0.0,
        var status: String? = null,
        var dateReserved:String? = null
)