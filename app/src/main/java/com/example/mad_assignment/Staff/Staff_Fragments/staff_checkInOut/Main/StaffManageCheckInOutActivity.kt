package com.example.mad_assignment.Staff.Staff_Fragments.staff_checkInOut.Main

import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.os.Bundle
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.mad_assignment.Customer.Booking.Class.Reservation
import com.example.mad_assignment.R
import com.example.mad_assignment.Staff.Staff_Fragments.StaffHomeFragment.Companion.PREFS_NUM_CHK
import com.google.android.material.imageview.ShapeableImageView
import com.google.firebase.database.*
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
                if(reservation != null){
                    checkInUser(reservation)

                    //********************************
                    //store today's chk in number of cust into the PREFS_NUM_CHK
                    var shareChkPref: SharedPreferences = getSharedPreferences(PREFS_NUM_CHK, MODE_PRIVATE)

                    val chkInNum: Int = shareChkPref.getInt("pref_chkIn", 0) + 1

                    var editor: SharedPreferences.Editor = shareChkPref.edit()
                    editor.putInt("pref_chkIn", chkInNum)
                    editor.apply()


                    //go back to manage activity
                    onBackPressed()

                    Toast.makeText(this, "Check In Success", Toast.LENGTH_SHORT).show()
                }else{
                    Toast.makeText(this, "Error Occured! Please Refresh App", Toast.LENGTH_SHORT).show()
                }

            }else{
                if(reservation != null){
                    checkOutUser(reservation)

                    //********************************
                    //store today's chk in number of cust into the PREFS_NUM_CHK
                    var shareChkPref: SharedPreferences = getSharedPreferences(PREFS_NUM_CHK, MODE_PRIVATE)

                    val chkOutNum: Int? = shareChkPref.getInt("pref_chkOut", 0)+ 1

                    var editor: SharedPreferences.Editor = shareChkPref.edit()
                    editor.putInt("pref_chkOut", chkOutNum!!)
                    editor.apply()



                    //go back to manage activity
                    onBackPressed()

                    Toast.makeText(this, "Check Out Success", Toast.LENGTH_SHORT).show()

                }else{
                    Toast.makeText(this, "Error Occured! Please Refresh App", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun checkInUser(reservation: Reservation){
        val query: Query = FirebaseDatabase.getInstance().getReference("Reservation")
            .child(reservation.uid.toString())
            .orderByChild("reservationID")
            .equalTo(reservation.reservationID)

        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                if(snapshot.exists()) {

                    val updateReservation = Reservation(reservation.reservationID, reservation.uid, reservation.custName, reservation.custImg,
                        reservation.guest, reservation. checkInDate, reservation.checkOutDate, reservation.nights, reservation.breakfast, reservation.reservationDetail,
                        reservation.totalPrice, "check in", reservation.dateReserved)

                    snapshot.ref.child(reservation.reservationID.toString()).setValue(updateReservation)
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

    private fun checkOutUser(reservation: Reservation){
        val query: Query = FirebaseDatabase.getInstance().getReference("Reservation")
            .child(reservation.uid.toString())
            .orderByChild("reservationID")
            .equalTo(reservation.reservationID)

        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                if(snapshot.exists()) {

                    val updateReservation = Reservation(reservation.reservationID, reservation.uid, reservation.custName, reservation.custImg,
                        reservation.guest, reservation. checkInDate, reservation.checkOutDate, reservation.nights, reservation.breakfast, reservation.reservationDetail,
                        reservation.totalPrice, "check out", reservation.dateReserved)

                    snapshot.ref.child(reservation.reservationID.toString()).setValue(updateReservation)
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }
}