package com.example.mad_assignment.Cust_Staff_Shared.Cust_Staff_Fragments

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.mad_assignment.CustomerMain
import com.example.mad_assignment.R
import com.example.mad_assignment.StaffMain

class Login: AppCompatActivity() {
    //temperary variable -- need REMOVE after use firebase
    val role:String? = "manager"

    override fun onCreate(savedInstanceState: Bundle?) {
        //the apps will first run the first page of the app (hotelapp_firstpage.xml)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login)

        /** temporary LOGIN detection **/

        //click 'btnCustLogin' will proceed to CustomerMain.kt (will enter customer menu navigation)
        val btnCustLogin: Button = findViewById(R.id.btnCustLogin)
        btnCustLogin.setOnClickListener() {
            val intent = Intent(this, CustomerMain::class.java)
            startActivity(intent)
        }

        //click 'btnStafftLogin' will proceed to StaffMain.kt (will enter staff menu navigation)
        val btnStaffLogin: Button = findViewById(R.id.btnStaffLogin)
        btnStaffLogin.setOnClickListener() {
            val intent = Intent(this, StaffMain::class.java)
            intent.putExtra("StaffPosition", role)
            startActivity(intent)
        }


    }
}