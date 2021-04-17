package com.example.mad_assignment.Customer.Customer_Fragments.cust_trans_history.payment

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.add
import androidx.fragment.app.commit
import com.example.mad_assignment.Customer.Chat.messages.LatestMessages
import com.example.mad_assignment.R
import kotlinx.android.synthetic.main.activity_cust_payment_method.*


class cust_payment_method : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cust_payment_method)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        ll_layout_bar_cust_payment_method.setOnClickListener{
            val intent = Intent(this, cust_payment_online_banking::class.java)
            startActivity(intent)
        }


        ll_layout_bar_cust_payment_method_ewallet.setOnClickListener{
            val intent = Intent(this, cust_payment_EWallet::class.java)
            startActivity(intent)
        }

        ll_layout_bar_cust_payment_method_creditCard.setOnClickListener{
            val intent = Intent(this, cust_payment_credit_card::class.java)
            startActivity(intent)
        }
    }
}