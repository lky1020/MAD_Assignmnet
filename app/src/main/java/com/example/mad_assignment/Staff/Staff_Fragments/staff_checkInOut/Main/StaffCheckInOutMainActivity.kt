package com.example.mad_assignment.Staff.Staff_Fragments.staff_checkInOut.Main

import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.FragmentTransaction
import com.example.mad_assignment.R


class StaffCheckInOutMainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_staff_check_in_out_main)

        //For status & navigation bar
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)

        val type: String? = intent.getStringExtra("type")

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Manage Check In/Out"

        //Set Fragment
        val fragment = StaffCheckInOutMainFragment()
        val arguments = Bundle()
        arguments.putString("type", type)
        fragment.arguments = arguments
        val ft: FragmentTransaction = supportFragmentManager.beginTransaction()
        ft.replace(R.id.framel_staff_checkInOut, fragment, "Check In Out")
        ft.commit()
    }
}