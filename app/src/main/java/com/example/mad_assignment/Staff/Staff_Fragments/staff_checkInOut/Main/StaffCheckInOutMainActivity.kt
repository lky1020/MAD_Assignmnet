package com.example.mad_assignment.Staff.Staff_Fragments.staff_checkInOut.Main

import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.FragmentTransaction
import androidx.navigation.ui.AppBarConfiguration
import com.example.mad_assignment.R


class StaffCheckInOutMainActivity : AppCompatActivity() {
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var decorView: View

    override fun onCreate(savedInstanceState: Bundle?) {
        //For status & navigation bar
        decorView = window.decorView
        decorView.systemUiVisibility = hideSystemBars()
        //Remove status bar shadow in Navigation View
        window.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_staff_check_in_out_main)

        val type: String? = intent.getStringExtra("type")

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Manage Check In/Out"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        //Set Fragment
        val fragment = StaffCheckInOutMainFragment()
        val arguments = Bundle()
        arguments.putString("type", type)
        fragment.arguments = arguments
        val ft: FragmentTransaction = supportFragmentManager.beginTransaction()
        ft.replace(R.id.framel_staff_checkInOut, fragment, "Check In Out")
        ft.commit()
    }

    private fun hideSystemBars(): Int{
        return View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                View.SYSTEM_UI_FLAG_FULLSCREEN or
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
    }
}