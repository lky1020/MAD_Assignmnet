package com.example.mad_assignment.Customer.Customer_Fragments.cust_trans_history.payment

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import com.example.mad_assignment.R
import kotlinx.android.synthetic.main.activity_cust_payment_method.*
import kotlinx.android.synthetic.main.activity_cust_payment_online_banking.*

class cust_payment_online_banking : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cust_payment_online_banking)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }


}