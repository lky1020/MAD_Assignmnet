package com.example.mad_assignment.Customer.Customer_Fragments.cust_housekeeping.Services.Service

import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.FragmentTransaction
import com.example.mad_assignment.R

class CustHousekeepingAvailableServicesActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cust_housekeeping_available_services)

        //For status & navigation bar
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)

        //Receive data
        val title: String = intent.getStringExtra("Title").toString()
        val imageUrl: String = intent.getStringExtra("ImageUrl").toString()

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = title
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        //Set Fragment
        val ft: FragmentTransaction = supportFragmentManager.beginTransaction()
        ft.replace(R.id.framel_Housekeeping_Services, CustHousekeepingAvailableServicesFragment(title, imageUrl))
        ft.commit()
    }
}