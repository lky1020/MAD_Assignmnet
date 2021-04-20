package com.example.mad_assignment.Staff.Staff_Fragments.manager_report

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.mad_assignment.Class.User
import com.example.mad_assignment.Customer.Booking.Class.Reservation
import com.example.mad_assignment.R
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.reservation_report.*
import java.time.LocalDate
import java.util.*

class ReservationReportFragment : Fragment() {

    var totalSS = 0
    var totalSD = 0
    var totalDD = 0
    var totalST = 0
    var totalSQ = 0
    var totalFR = 0
    var totalDS = 0
    var totalTR = 0

    @SuppressLint("SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.reservation_report, container, false)

        var reportName:TextView = root.findViewById(R.id.tv_report_name)
        var reportDate:TextView = root.findViewById(R.id.tv_date_gen)

        //get report name
        val year:String = LocalDate.now().year.toString()
        val month:String = LocalDate.now().month.toString()
        reportName.text = "$month $year RESERVATION REPORT"

        //get report gen date
        reportDate.text = LocalDate.now().toString()

        fetchData()


        return root
    }

    fun fetchData(){
        //get current month
        val c = Calendar.getInstance()
        val mon = c.get(Calendar.MONTH) +1
        Log.d("Reservation Resport", "Current month: $mon")

        val ref = FirebaseDatabase.getInstance().getReference("/User")
        ref.addValueEventListener(object : ValueEventListener {
            @RequiresApi(Build.VERSION_CODES.O)
            override fun onDataChange(snapshot: DataSnapshot) {
                //reportList.clear()
                //to get multiple data
                for (postSnapshot in snapshot.children) {
                    val allUser: User? = postSnapshot.getValue(User::class.java)
                    Log.d("User Report", "Display all user data: ${allUser!!.name}")

                    if(allUser.role == "Member"){
                        val refReservation = FirebaseDatabase.getInstance().getReference("/Reservation/${allUser.uid}")
                        refReservation.addValueEventListener(object : ValueEventListener {
                            @RequiresApi(Build.VERSION_CODES.O)
                            override fun onDataChange(snapshot: DataSnapshot) {

                                //to get multiple data
                                for (postSnapshot1 in snapshot.children) {
                                    val reservation: Reservation? = postSnapshot1.getValue(
                                        Reservation::class.java)
                                    Log.d("Reservation Report", "Display all reservation data: ${reservation!!.reservationID} - ${reservation.dateReserved}")
                                    Log.d("Reservation Report", "Display all reservation data: ${getMonth(reservation.dateReserved)}")
                                    if(getMonth(reservation.dateReserved) == mon){
                                        for(reservationDetails in reservation.reservationDetail!!){
                                            when (reservationDetails.roomType!!.roomID) {
                                                "SS" -> totalSS ++
                                                "SD" -> totalSD ++
                                                "DD" -> totalDD ++
                                                "ST" -> totalST ++
                                                "SQ" -> totalSQ ++
                                                "FR" -> totalFR ++
                                                "DS" -> totalDS ++
                                                else -> totalTR ++
                                            }

                                        }

                                    }
                                }
                                tv_ss_no.text = totalSS.toString()
                                tv_sd_no.text = totalSD.toString()
                                tv_dd_no.text = totalDD.toString()
                                tv_st_no.text = totalST.toString()
                                tv_sq_no.text = totalSQ.toString()
                                tv_fr_no.text = totalFR.toString()
                                tv_ds_no.text = totalDS.toString()
                                tv_tr_no.text = totalTR.toString()
                                tv_total.text = (totalSS+totalSD+totalDD+totalST+totalSQ+totalFR+totalDS+totalTR).toString()

                            }

                            override fun onCancelled(error: DatabaseError) {

                            }
                        })
                    }
                }


            }
            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

    @SuppressLint("SimpleDateFormat")
    @RequiresApi(Build.VERSION_CODES.O)
    fun getMonth(date: String?): Int {
        return date!!.substring(3, 5).toInt()
    }


}

