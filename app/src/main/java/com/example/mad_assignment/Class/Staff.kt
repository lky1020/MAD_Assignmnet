package com.example.mad_assignment.Class

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class Staff (val name: String, val id: String, val email: String, val password: String, val phoneNum: String, val img: String,
    val role: String, val status: String, val accessRoom: Boolean, val accessServicesFacilities: Boolean, val accessHousekeeping: Boolean,
    val accessCheckInOut: Boolean, val uid: String): Parcelable {

    constructor() : this("","","","","","", "", "",
        false, false, false, false, "")
}