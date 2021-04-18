package com.example.mad_assignment.Customer.Booking.Class

import java.util.ArrayList

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class RoomType(
    var roomID: String?,
    var roomType: String?,
    var occupancy: Int?,
    var size: Int?,
    var price: Double?,
    var beds: String?,
    var img: String?,

): Parcelable {

    constructor() : this(null, null, 0, 0, 0.0, null, null)
}
