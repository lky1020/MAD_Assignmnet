package com.example.mad_assignment.Customer.Customer_Fragments.cust_trans_history.payment

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.mad_assignment.Customer.Booking.Class.Reservation
import com.example.mad_assignment.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_cust_payment_method.*


class cust_payment_method : AppCompatActivity() {

    companion object {
        var currentReserved: Reservation? = null
        var totalPrice: String? = ""
    }
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cust_payment_method)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        getCurrentReservationData()
    }

    private fun callFunctionDirect(){
        ll_layout_bar_cust_payment_method.setOnClickListener{
            val intent = Intent(this, cust_payment_online_banking::class.java)
            intent.putExtra("reservedID",currentReserved!!.reservationID)

            startActivity(intent)
        }


        ll_layout_bar_cust_payment_method_ewallet.setOnClickListener{
            val intent = Intent(this, cust_payment_EWallet::class.java)
            intent.putExtra("reservedID",currentReserved!!.reservationID)
            startActivity(intent)
        }

        ll_layout_bar_cust_payment_method_creditCard.setOnClickListener{
            val intent = Intent(this, cust_payment_credit_card::class.java)
            intent.putExtra("reservedID",currentReserved!!.reservationID)
            startActivity(intent)
        }
    }

    private fun assignDataIntoText() {

        val passText = "RM $totalPrice"

        tvSubtotalValue.text = passText
        tvTotalAmountValue.text = passText
    }

    private fun getCurrentReservationData(){

        val currentReservation: String? = intent.getStringExtra("currentReservationID")
        Log.d("ReservationID", "$currentReservation")
        val uid = FirebaseAuth.getInstance().uid
        val ref = FirebaseDatabase.getInstance().getReference("/Reservation/$uid/$currentReservation")

        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                currentReserved = snapshot.getValue(Reservation::class.java)

                totalPrice = String.format("%.2f", currentReserved?.totalPrice)
                assignDataIntoText()
                Log.d("PaymentReserve", "Current Data $totalPrice")

                callFunctionDirect()
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }

}