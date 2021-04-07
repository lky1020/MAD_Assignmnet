package com.example.mad_assignment.Customer.Customer_Fragments.cust_housekeeping

import android.annotation.SuppressLint
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mad_assignment.Customer.Customer_Fragments.cust_housekeeping.Class.HousekeepingService
import com.google.firebase.database.*
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class CustAvailableHousekeepingServicesModel() : ViewModel()  {

    private val _housekeepingServices = MutableLiveData<ArrayList<HousekeepingService>>()
    private val housekeepingServicesList = ArrayList<HousekeepingService>()

    val housekeepingServices : LiveData<ArrayList<HousekeepingService>>
        get()= _housekeepingServices

    fun getHousekeepingServicesList(): MutableLiveData<ArrayList<HousekeepingService>> {
        return _housekeepingServices
    }

    private var _status = MutableLiveData<Boolean>()

    val status : LiveData<Boolean>
        get()= _status

    fun getStatus(): MutableLiveData<Boolean>{
        return _status
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private val dtf: DateTimeFormatter = DateTimeFormatter.ofPattern("hh:mm a")
    private lateinit var from: LocalTime
    private lateinit var to: LocalTime
    private lateinit var dbFrom: LocalTime
    private lateinit var dbTo: LocalTime

    @RequiresApi(Build.VERSION_CODES.O)
    fun retrieveHousekeepingServicesFromDB(retrieveDate: String, timeFromHour: Int, timeFromMinute: Int, timeToHour: Int, timeToMinute: Int){

        val timeFromSeason = ""
        val timeToSeason = ""

        val timeFromList = mutableListOf<Any>(timeFromHour, timeFromMinute, timeFromSeason)
        val timeToList = mutableListOf<Any>(timeToHour, timeToMinute, timeToSeason)

        initTime(timeFromList, timeToList)

        val query: Query = FirebaseDatabase.getInstance().getReference("Housekeeping")
            .child("Bed Textiles").child("ServicesAvailable").child(retrieveDate)
            .orderByChild("date")
            .equalTo(retrieveDate)

        query.addValueEventListener(object : ValueEventListener {
            @SuppressLint("SimpleDateFormat")
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    // Clear housekeepingList to prevent duplicate item appear
                    housekeepingServicesList.clear()

                    for (i in snapshot.children) {
                        // get the item from firebase
                        val housekeepingServices = i.getValue(HousekeepingService::class.java)

                        //sort the time
                        if (housekeepingServices != null) {
                            if(housekeepingServices.timeFrom.substring(6, 8) == "PM"){
                                if(housekeepingServices.timeFrom.substring(0, 2).toInt() == 12){
                                    dbFrom = LocalTime.of(0,
                                            housekeepingServices.timeFrom.substring(3, 5).toInt())
                                }
                                else{
                                    dbFrom = LocalTime.of(housekeepingServices.timeFrom.substring(0, 2).toInt() + 12,
                                            housekeepingServices.timeFrom.substring(3, 5).toInt())
                                }
                            }else{
                                dbFrom = LocalTime.of(housekeepingServices.timeFrom.substring(0, 2).toInt(),
                                        housekeepingServices.timeFrom.substring(3, 5).toInt())
                            }

                            if(housekeepingServices.timeTo.substring(6, 8) == "PM"){
                                if(housekeepingServices.timeTo.substring(0, 2).toInt() == 12){
                                    dbTo = LocalTime.of(housekeepingServices.timeTo.substring(0, 2).toInt(),
                                            housekeepingServices.timeTo.substring(3, 5).toInt())
                                }else{
                                    dbTo = LocalTime.of(housekeepingServices.timeTo.substring(0, 2).toInt() + 12,
                                            housekeepingServices.timeTo.substring(3, 5).toInt())
                                }

                            }else{
                                dbTo = LocalTime.of(housekeepingServices.timeTo.substring(0, 2).toInt(),
                                        housekeepingServices.timeTo.substring(3, 5).toInt())
                            }

                            if(dbFrom == from || dbFrom.isAfter(from)){
                                if(dbTo == to || dbTo.isBefore(to)){
                                    //add the item and pass to observer for the adapter
                                    housekeepingServicesList.add(housekeepingServices)
                                }
                            }
                        }
                    }

                    _status.value = housekeepingServicesList.size > 0

                    _housekeepingServices.value = housekeepingServicesList
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("SimpleDateFormat")
    private fun initTime(timeFrom: MutableList<Any>, timeTo: MutableList<Any>){

        // Change hour to 12 hour
        if(timeFrom[0].toString().toInt() >= 12){
            timeFrom[2] = "PM"
        }
        else{
            timeFrom[2] = "AM"
        }

        if(timeFrom[1].toString().toInt() in 1..30){
            timeFrom[1] = 30
        }
        else if(timeFrom[1].toString().toInt() == 0){
            timeFrom[1] = 0
        }
        else{
            timeFrom[0] = timeFrom[0].toString().toInt() + 1
            timeFrom[1] = 0
        }

        // Change hour to 12 hour
        if(timeTo[0].toString().toInt() >= 12){
            timeTo[2] = "PM"
        }
        else{
            timeTo[2] = "AM"
        }

        if(timeTo[1].toString().toInt() in 1..30){
            timeTo[1] = 30
        }
        else if(timeTo[1].toString().toInt() == 0){
            timeTo[1] = 0
        }
        else{
            timeTo[0] = timeTo[0].toString().toInt() + 1
            timeTo[1] = 0
        }

        from = LocalTime.of(timeFrom[0].toString().toInt(), timeFrom[1].toString().toInt())
        Log.d("Testing Formatter", from.format(dtf))

        to = LocalTime.of(timeTo[0].toString().toInt(), timeTo[1].toString().toInt())
        Log.d("Testing Formatter", to.format(dtf))

    }
}