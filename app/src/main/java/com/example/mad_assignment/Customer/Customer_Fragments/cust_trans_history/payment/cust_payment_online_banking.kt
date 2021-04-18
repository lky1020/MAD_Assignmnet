package com.example.mad_assignment.Customer.Customer_Fragments.cust_trans_history.payment

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.widget.Toolbar
import com.example.mad_assignment.Customer.Booking.Class.Reservation
import com.example.mad_assignment.Customer.Booking.Class.ReservationDetail
import com.example.mad_assignment.Customer.Booking.Class.RoomType
import com.example.mad_assignment.R
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_cust_payment_method.*
import kotlinx.android.synthetic.main.activity_cust_payment_online_banking.*
import java.lang.reflect.Type

class cust_payment_online_banking : AppCompatActivity() {

    lateinit var selectedReservationDataList: ArrayList<Reservation>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cust_payment_online_banking)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        fetchCurrentReservationData()
    }

    private fun fetchCurrentReservationData() {
        //retrieve data from previous page
//        val gson = Gson()
//        val groupListType: Type = object : TypeToken<ArrayList<Reservation?>?>() {}.type
//        selectedReservationDataList = gson.fromJson(intent.getStringExtra("reservedDataList"), groupListType)
        
        val reserve = intent.getStringExtra("reservedDataList")


//        Log.d("online Banking", " Hello${selectedReservationDataList!![0].reservationID}")




    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }


}

