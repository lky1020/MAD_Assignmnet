package com.example.mad_assignment.Staff.Staff_Fragments.staff_manager.Main.Staff

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import android.widget.ImageView
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.FragmentTransaction
import com.example.mad_assignment.R

class StaffManagerActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_staff_manager)

        //For status & navigation bar
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = null
        supportActionBar?.title = null

        //back button
        val backButton = findViewById<ImageView>(R.id.manage_staff_back_icon)
        backButton.setOnClickListener{
            finish()
        }

        //Set Fragment
        val ft: FragmentTransaction = supportFragmentManager.beginTransaction()
        ft.replace(R.id.framel_staff_Manager, StaffManagerFragment())
        ft.commit()
    }
}