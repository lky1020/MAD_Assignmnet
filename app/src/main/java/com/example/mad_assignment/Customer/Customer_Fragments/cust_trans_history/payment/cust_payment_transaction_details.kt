package com.example.mad_assignment.Customer.Customer_Fragments.cust_trans_history.payment

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import com.example.mad_assignment.Class.User
import com.example.mad_assignment.Customer.Booking.Class.Reservation
import com.example.mad_assignment.Customer.Customer_Fragments.cust_trans_history.payment.views.cust_transaction_details_record
import com.example.mad_assignment.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_cust_payment_transaction_details.*
import java.text.SimpleDateFormat

class cust_payment_transaction_details : AppCompatActivity() {

    companion object {
        const val PERMISSION_REQUEST_CODE: Int = 200
        var currentUser: User? = null
        var invoiceID: String = ""
        var pageHeight = 1120
        var pagewidth = 792
        var bmp: Bitmap? = null
        var scaledbmp: Bitmap? = null
        var elementCount: Int = 0
        var currentReserved: Reservation? = null
        var totalPrice: String? = ""
        val adapter = GroupAdapter<ViewHolder>()
        private var reservation_List = ArrayList<Reservation>()
    }

    val mcontext = this
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cust_payment_transaction_details)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        fetchCurrentUser()
        getCurrentReservationData()
        Log.d("checking", "in payment trasaction details line 53")

        Log.d("checking", "in payment trasaction details line 55")


    }

    private fun fetchCurrentUser(){
        val uid = FirebaseAuth.getInstance().uid
        val ref = FirebaseDatabase.getInstance().getReference("/User/$uid")
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                currentUser = snapshot.getValue(User::class.java)

            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }


    private fun assignDataIntoText() {
        val passText = "RM $totalPrice"

        tvTotalAmountValueDetails.text = passText
        val currentPaymentMethod: String? = intent.getStringExtra("paymentMethod")
        if(currentReserved!!.status == "paid"){
            Log.d("status", "${currentReserved!!.status}")
            imgViewPaymentStatus.setImageResource(R.drawable.success)
            tvPurchaseStatus.text = getString(R.string.purchasesuccesStr)
        }
        else{
            Log.d("status", "${currentReserved!!.status}")
            Log.d("status", getString(R.string.purchasefailStr))
            imgViewPaymentStatus.setImageResource(R.drawable.fail)
            tvPurchaseStatus.setTextColor(ContextCompat.getColor(this,R.color.pink))
            tvPurchaseStatus.text = getString(R.string.purchasefailStr)
        }

        when (currentPaymentMethod) {
            "TNG Wallet" -> {
                tvpaymentMethod.text = getString(R.string.ewallet)
                tvPaymentCompany.text = getString(R.string.tngStr)
                imgVPaymentMethodLogo.setImageResource(R.drawable.tng)
            }
            "Grab Pay" -> {
                tvpaymentMethod.text = getString(R.string.ewallet)
                tvPaymentCompany.text = getString(R.string.grabpayStr)
                imgVPaymentMethodLogo.setImageResource(R.drawable.gp)
            }
            "Boost" -> {
                tvpaymentMethod.text = getString(R.string.ewallet)
                tvPaymentCompany.text = getString(R.string.boostStr)
                imgVPaymentMethodLogo.setImageResource(R.drawable.boost)
            }
            "Fave" -> {
                tvpaymentMethod.text = getString(R.string.ewallet)
                tvPaymentCompany.text = getString(R.string.faveStr)
                imgVPaymentMethodLogo.setImageResource(R.drawable.fave)
            }
            "Public Bank" ->{
                tvpaymentMethod.text = getString(R.string.onlineBanking)
                tvPaymentCompany.text = getString(R.string.pbeStr)
                imgVPaymentMethodLogo.setImageResource(R.drawable.pbe)
            }
            "Maybank" -> {
                tvpaymentMethod.text = getString(R.string.onlineBanking)
                tvPaymentCompany.text = getString(R.string.maybankStr)
                imgVPaymentMethodLogo.setImageResource(R.drawable.maybank)
            }
            "CIMB Bank" -> {
                tvpaymentMethod.text = getString(R.string.onlineBanking)
                tvPaymentCompany.text = getString(R.string.CIMBStr)
                imgVPaymentMethodLogo.setImageResource(R.drawable.cimb)
            }
            "Other Bank" -> {
                tvpaymentMethod.text = getString(R.string.onlineBanking)
                tvPaymentCompany.text = getString(R.string.otherBankStr)
                imgVPaymentMethodLogo.setImageResource(R.drawable.otherbank)
            }
            else -> {
                tvpaymentMethod.text = ""
                tvPaymentCompany.text = ""
                imgVPaymentMethodLogo.setImageResource(R.drawable.tng)
            }
        }


    }


    private fun getCurrentReservationData(){

        val currentReservation: String? = intent.getStringExtra("reservedID")
        Log.d("ReservationID", "$currentReservation")
        val uid = FirebaseAuth.getInstance().uid
        val ref = FirebaseDatabase.getInstance().getReference("/Reservation/$uid/$currentReservation")
        val sharedPreferences = getSharedPreferences("myKey", MODE_PRIVATE)

        ref.addValueEventListener(object : ValueEventListener {
            @RequiresApi(Build.VERSION_CODES.O)
            override fun onDataChange(snapshot: DataSnapshot) {
                currentReserved = snapshot.getValue(Reservation::class.java)

                totalPrice = String.format("%.2f", currentReserved?.totalPrice)
                assignDataIntoText()
                Log.d("PaymentReserve", "Current Data $totalPrice")

                snapshot.children.forEach {
                    Log.d("NewMessage", it.toString())


                    reservation_List.add(Reservation(
                            currentReserved!!.reservationID,
                            uid,
                            currentReserved!!.custName,
                            currentReserved!!.custImg,
                            currentReserved!!.guest,
                            convertLongToDate1(sharedPreferences.getLong("startDate", 0)),
                            convertLongToDate1(sharedPreferences.getLong("endDate", 0)),
                            currentReserved!!.nights,
                            currentReserved!!.breakfast,
                            currentReserved!!.reservationDetail,
                            currentReserved!!.totalPrice,
                            currentReserved!!.status,
                            currentReserved!!.dateReserved,
                    ))


                }
//                recyclerViewTransactionDataForPayment.adapter =
//                        reservation_List?.let { cust_transaction_details_record(reservation_List, mcontext) } //Initialize adapter


            }

            override fun onCancelled(error: DatabaseError) {
            }

        })
    }

    @SuppressLint("SimpleDateFormat")
    private fun convertLongToDate1(date: Long?): String {
        val format: SimpleDateFormat = SimpleDateFormat("dd/MM/yyyy")
        return format.format(date)
    }

}