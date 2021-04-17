package com.example.mad_assignment.Customer.Booking.Main

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mad_assignment.Class.User
import com.example.mad_assignment.Customer.Booking.Adapter.ConfirmBookingAdapter
import com.example.mad_assignment.Customer.Booking.Class.Reservation
import com.example.mad_assignment.Customer.Booking.Class.ReservationDetail
import com.example.mad_assignment.Customer.Customer_Fragments.cust_trans_history.payment.cust_payment_method
import com.example.mad_assignment.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.confirm_booking.*
import java.lang.reflect.Type
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle


class ConfirmBooking : AppCompatActivity() {

    private var selectedRoomList: ArrayList<ReservationDetail>? = ArrayList<ReservationDetail>()
    private var subtotal:Double = 0.0
    private var breakfastRate: Double = 15.0
    private var totalPrice:Double = 0.0
    private var reservationID: String? = null

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.confirm_booking)

        //get from shared preferences
        val sharedPreferences = getSharedPreferences("myKey", MODE_PRIVATE)
        val startDate = convertLongToDate(sharedPreferences.getLong("startDate", 0))
        val endDate = convertLongToDate(sharedPreferences.getLong("endDate", 0))
        val guest = sharedPreferences.getInt("guest", 1)
        val nights = sharedPreferences.getLong("night", 0).toInt()

        //set date & guest
        confirm_book_check_in.text = startDate

        if(nights == 1)
            confirm_book_check_out.text = "$endDate ($nights night)"
        else
            confirm_book_check_out.text = "$endDate ($nights nights)"

        confirm_book_guest.text = "$guest guest"

        //set toolbar as support action bar
        setSupportActionBar(confirm_book_toolbar)
        supportActionBar?.title = null

        //back button
        confirm_book_back_icon.setOnClickListener{
            finish()
        }


        //retrieve data from previous page
        val gson = Gson()
        val selectedRoom = gson.fromJson<ReservationDetail>(intent.getStringExtra("selectedRoom"), ReservationDetail::class.java)
        val groupListType: Type = object : TypeToken<ArrayList<ReservationDetail>>() {}.type
        selectedRoomList = gson.fromJson(intent.getStringExtra("selectedRoomList"), groupListType)


        //recycle view
        val recyclerView: RecyclerView = findViewById(R.id.rv_confirm_book_price_details)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter =
            selectedRoomList?.let { ConfirmBookingAdapter(this.selectedRoomList!!, this) } //Initialize adapter
        recyclerView.setHasFixedSize(true)

        //calculate subtotal
        for(detail in selectedRoomList!!){
            subtotal += detail.subtotal!!
        }
        confirm_book_subtotal.text = "RM ${subtotal.format(2)}"

        //Guest control
        tv_cb_guest.text = guest.toString()
        iv_cb_plus.setOnClickListener { increaseInteger() }
        iv_cb_minus.setOnClickListener { decreaseInteger() }

        //calculate total price
        totalPrice = subtotal + (breakfastRate * tv_cb_guest.text.toString().toDouble())
        confirm_book_total.text = "RM ${totalPrice.format(2)}"


        //------------------------------------------------------------------
        //---------------------Confirm and pay button-----------------------
        //------------------------------------------------------------------

        btn_confirm_pay.setOnClickListener {
            val uid = FirebaseAuth.getInstance().uid


            val query: Query = FirebaseDatabase.getInstance().getReference("User")
                    .orderByChild("uid")
                    .equalTo(uid)

            query.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if(snapshot.exists()){

                        for(i in snapshot.children){
                            val user = i.getValue(User::class.java)

                            val ref = FirebaseDatabase.getInstance().getReference("Reservation/$uid/${reservationID}")

                            val reservation:Reservation = Reservation(
                                    reservationID,
                                    uid,
                                    user!!.name,
                                    user.img,
                                    guest,
                                    convertLongToDate1(sharedPreferences.getLong("startDate", 0)),
                                    convertLongToDate1(sharedPreferences.getLong("endDate", 0)),
                                    nights,
                                    tv_cb_guest.text.toString().toInt(),
                                    selectedRoomList,
                                    totalPrice,
                                    "pending",
                                    todayDate(),
                            )

                            ref.setValue(reservation)
                                    .addOnSuccessListener {
                                        Log.d("confirm book", "Successfully reserve")
                                    }
                                    .addOnFailureListener{
                                        Log.d("confirm book", "Fail to reserve")
                                    }
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {

                }
            })

            val intent = Intent(this, cust_payment_method::class.java)
            startActivity(intent)

        }

        assignReservationID()




    }

    fun assignReservationID(){
        val uid = FirebaseAuth.getInstance().uid
        val ref = FirebaseDatabase.getInstance().getReference("Reservation/$uid")

        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val count = snapshot.childrenCount + 1
                reservationID = count.toString()
            }

            override fun onCancelled(error: DatabaseError) {}
        })



    }

    @SuppressLint("SimpleDateFormat")
    private fun convertLongToDate(date: Long?): String {
        val format: SimpleDateFormat = SimpleDateFormat("dd MMM yyyy")
        return format.format(date)
    }

    @SuppressLint("SimpleDateFormat")
    private fun convertLongToDate1(date: Long?): String {
        val format: SimpleDateFormat = SimpleDateFormat("dd/MM/yyyy")
        return format.format(date)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("SimpleDateFormat")
    private fun todayDate(): String {
        val today: LocalDate = LocalDate.now()
        val formattedDate = today.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT));
        return formattedDate
    }

    private fun Double.format(digits: Int) = "%.${digits}f".format(this)

    private fun increaseInteger() {
        display(tv_cb_guest.text.toString().toInt() + 1)
    }

    private fun decreaseInteger() {
        //if greater than 1, decrease
        if(tv_cb_guest.text.toString().toInt() > 0)
            display(tv_cb_guest.text.toString().toInt() - 1)
    }

    private fun display(number: Int) {
        tv_cb_guest.text = "$number"

        //calculate total price
        totalPrice = subtotal + (breakfastRate * tv_cb_guest.text.toString().toDouble())
        confirm_book_total.text = "RM ${totalPrice.format(2)}"
    }
}