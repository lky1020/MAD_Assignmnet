package com.example.mad_assignment.Class

data class Staff(
    val name: String = "",
    val id: String = "",
    val email: String = "",
    val password: String = "",
    val phoneNum: String = "",
    val img: String = "",
    val role: String = "",
    val status: String = "Offline",
    val accessRoom: Boolean = false,
    val accessServicesFacilities: Boolean = false,
    val accessHousekeeping: Boolean = false,
    val accessCheckInOut: Boolean = false
)