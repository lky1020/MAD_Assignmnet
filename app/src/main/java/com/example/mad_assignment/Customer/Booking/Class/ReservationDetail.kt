package com.example.mad_assignment.Customer.Booking.Class
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class ReservationDetail(
    var roomType: RoomType?,
    var qty: Int,
    var subtotal: Double?
): Parcelable {

    constructor() : this(null, 0, 0.0)
}