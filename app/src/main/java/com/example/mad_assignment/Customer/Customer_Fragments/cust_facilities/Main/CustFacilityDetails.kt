package com.example.mad_assignment.Customer.Customer_Fragments.cust_facilities.Main

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.mad_assignment.R
import com.example.mad_assignment.Staff.facility.Class.Facility
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.cust_facility_details.*
import java.text.SimpleDateFormat

class CustFacilityDetails : AppCompatActivity() {
    @SuppressLint("SimpleDateFormat", "SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.cust_facility_details)

        //back button
        back_icon.setOnClickListener{
            finish()
        }

        //------------------------------------------------------
        //------- retrieve data from previous page -------------
        //------------------------------------------------------

        val gson = Gson()
        val facility = gson.fromJson<Facility>(intent.getStringExtra("facility"), Facility::class.java)

        Picasso.get().load(facility.img).into(iv_facility_img)
        Picasso.get().isLoggingEnabled = true

        tv_name.text = "${facility.facilityName}"
        tv_desc.text = "${facility.description}"
        tv_location.text = "Location: ${facility.location}"

        val from = SimpleDateFormat("hh:mm aa").format(facility.operationHrStart!!)
        val to = SimpleDateFormat("hh:mm aa").format(facility.operationHrEnd!!)

        tv_operation.text = "$from to $to"

    }
}