@file:Suppress("DEPRECATION")

package com.example.mad_assignment

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.mad_assignment.Cust_Staff_Shared.Cust_Staff_Fragments.Login
import com.example.mad_assignment.Cust_Staff_Shared.Cust_Staff_Fragments.Register


class MainActivity : AppCompatActivity() {

    private lateinit var decorView: View

    override fun onCreate(savedInstanceState: Bundle?) {
        //For status & navigation bar
        decorView = window.decorView
        decorView.systemUiVisibility = hideSystemBars()

        //the apps will first run the first page of the app (hotelapp_firstpage.xml)
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.login)
        setContentView(R.layout.hotelapp_firstpage)

        /** btn click**/

        //click 'Login' button
        val btnStaffLogin: Button = findViewById(R.id.btnto_loginpage)
        btnStaffLogin.setOnClickListener(){
            val intent = Intent(this@MainActivity, CustomerMain::class.java)
            startActivity(intent)
        }

        //click 'Register' button
        val btnCustLogin: Button = findViewById(R.id.btn_register)
        btnCustLogin.setOnClickListener(){
            val intent = Intent(this@MainActivity, Register::class.java)
            startActivity(intent)
        }

    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)

        if(hasFocus){
            //Hide the status & navigation bar
            decorView.systemUiVisibility = hideSystemBars()
        }
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