package com.example.mad_assignment.Staff.Staff_Fragments.staff_housekeeping.Main.Main

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.WindowManager
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.FragmentTransaction
import com.example.mad_assignment.R
import com.example.mad_assignment.Staff.Staff_Fragments.staff_housekeeping.Main.Main.Item.StaffHousekeepingItemFragment
import com.example.mad_assignment.Staff.Staff_Fragments.staff_housekeeping.Main.Main.Services.StaffHousekeepingServicesFragment


class StaffHousekeepingServiceActivity : AppCompatActivity() {
    @SuppressLint("ResourceType", "InflateParams", "SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_staff_housekeeping_service_fragment)

        //For status & navigation bar
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)

        //Receive data
        val title: String = intent.getStringExtra("Title").toString()

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Manage $title"
        
        val btnAddService: Button = findViewById(R.id.btn_add_service)
        btnAddService.text = "Add New $title"

        if(title == "Room Cleaning" || title == "Laundry Service"){
            //Set Fragment
            val ft: FragmentTransaction = supportFragmentManager.beginTransaction()
            ft.replace(R.id.framel_staff_Housekeeping_manage, StaffHousekeepingServicesFragment(title))
            ft.commit()

        }else{
            //Set Fragment
            val ft: FragmentTransaction = supportFragmentManager.beginTransaction()
            ft.replace(R.id.framel_staff_Housekeeping_manage, StaffHousekeepingItemFragment(title))
            ft.commit()
        }

    }

}