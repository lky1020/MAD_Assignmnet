package com.example.mad_assignment.Staff.facility.Class

import java.util.*

data class Facility(
    var facilityID: String? = null,
    var facilityName: String? = null,
    var operationHrStart: Date? = null,
    var operationHrEnd: Date? = null,
    var description: String? = null,
    var location: String? = null,
    var img: String? = null,
)
