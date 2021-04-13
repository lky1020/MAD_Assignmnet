package com.example.mad_assignment.Staff.Staff_Fragments.staff_housekeeping.Main.Main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.FragmentTransaction
import com.example.mad_assignment.Customer.Customer_Fragments.cust_housekeeping.Services.Service.CustHousekeepingAvailableServicesFragment
import com.example.mad_assignment.R

class StaffHousekeepingMainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_staff_housekeeping_main)

        //For status & navigation bar
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Manage Housekeeping"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        //Set Fragment
        val ft: FragmentTransaction = supportFragmentManager.beginTransaction()
        ft.replace(R.id.framel_staff_housekeeping, StaffHousekeepingMainFragment())
        ft.commit()
    }
}