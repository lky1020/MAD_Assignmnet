package com.example.mad_assignment.Staff.Staff_Fragments.staff_checkInOut.Main

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import com.example.mad_assignment.Class.Staff
import com.example.mad_assignment.Customer.Booking.Class.Reservation
import com.example.mad_assignment.R
import com.google.android.material.imageview.ShapeableImageView
import com.squareup.picasso.Picasso

class StaffManageCheckInOutActivity : AppCompatActivity() {
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_staff_manage_check_in_out)

        val reservation = intent.getParcelableExtra<Reservation>("Reservation")
        val type = intent.getStringExtra("type")

        //Initialize layout
        val ivCustImg: ShapeableImageView = findViewById(R.id.iv_customer_img)
        val tvCustName: TextView = findViewById(R.id.tv_customer_name)
        val tvGuest: TextView = findViewById(R.id.tv_guest)
        val tvCheckDate: TextView = findViewById(R.id.tv_check_date)
        val tvReservedDate: TextView = findViewById(R.id.tv_reservedDate)
        val tvCheckInDate: TextView = findViewById(R.id.tv_check_in_detail)
        val tvCheckOutDate: TextView = findViewById(R.id.tv_check_out_detail)
        val tvGuestDetail: TextView = findViewById(R.id.tv_guest_detail)
        val tvRoomDetail: TextView = findViewById(R.id.tv_room_detail)
        val btnCheckInOut: Button = findViewById(R.id.btn_check_in_out)

        if (reservation != null) {
            Picasso.get().load(reservation.custImg).into(ivCustImg)
            tvCustName.text = reservation.custName
            tvGuest.text = reservation.guest.toString() + " Guest"
            tvCheckDate.text = reservation.checkInDate
            tvReservedDate.text = "Reserved at " + reservation.dateReserved
            tvCheckInDate.text = reservation.checkInDate
            tvCheckOutDate.text = reservation.checkOutDate
            tvGuestDetail.text = reservation.guest.toString() + " guest(s)"
            tvRoomDetail.text = reservation.reservationDetail?.get(0)?.qty.toString() + "x "  + reservation.reservationDetail?.get(0)?.roomType?.roomType.toString()

            if(type == "Check In"){
                btnCheckInOut.text = "Check In"
            }else{
                btnCheckInOut.text = "Check Out"
            }
        }

        //For status & navigation bar
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        if(type == "Check In"){
            supportActionBar?.title = "Manage Check In"
        }else{
            supportActionBar?.title = "Manage Check Out"
        }

        btnCheckInOut.setOnClickListener {
            if(type == "Check In"){
                Toast.makeText(this, "Check In Success", Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(this, "Check Out Success", Toast.LENGTH_SHORT).show()
            }
        }

    }
}