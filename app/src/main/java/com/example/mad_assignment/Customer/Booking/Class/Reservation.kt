package com.example.mad_assignment.Customer.Booking.Class

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
class Reservation(var reservationID: String?,
                  var uid: String?,
                  var custName: String?,
                  var custImg: String?,
                  var guest:Int?,
                  var checkInDate:String?,
                  var checkOutDate:String?,
                  var nights:Int?,
                  var breakfast:Int?,
                  var reservationDetail: ArrayList<ReservationDetail>?,
                  var totalPrice: Double?,
                  var status: String?,
                  var dateReserved: String?): Parcelable{

    constructor() : this(null,null,null,null,0,null, null, 0,
            0, null, 0.0, null, null)
}