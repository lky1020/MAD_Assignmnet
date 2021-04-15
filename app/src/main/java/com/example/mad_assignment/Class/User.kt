package com.example.mad_assignment.Class

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class User (val name:String, val uid:String, val password:String, val phoneNum:String,val email:String, val role:String, val img:String): Parcelable {
    constructor() : this("","","","","","", "")
}