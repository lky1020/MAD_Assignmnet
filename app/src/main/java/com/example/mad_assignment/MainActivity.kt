package com.example.mad_assignment

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.mad_assignment.Cust_Staff_Shared.Cust_Staff_Fragments.Login
import com.example.mad_assignment.Cust_Staff_Shared.Cust_Staff_Fragments.Register


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        //the apps will first run the first page of the app (hotelapp_firstpage.xml)
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.login)
        setContentView(R.layout.hotelapp_firstpage)

        /** btn click**/

        //click 'Login' button
        val btnStaffLogin: Button = findViewById(R.id.btnto_loginpage)
        btnStaffLogin.setOnClickListener(){
            val intent = Intent(this@MainActivity, Login::class.java)
            startActivity(intent)
        }

        //click 'Register' button
        val btnCustLogin: Button = findViewById(R.id.btn_register)
        btnCustLogin.setOnClickListener(){
            val intent = Intent(this@MainActivity, Register::class.java)
            startActivity(intent)
        }

    }

}