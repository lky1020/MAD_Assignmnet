package com.example.mad_assignment.Staff.Staff_Fragments.manager_report

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.example.mad_assignment.Class.User
import com.example.mad_assignment.Cust_Staff_Shared.Cust_Staff_Fragments.ProfileFragment
import com.example.mad_assignment.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import java.time.LocalDate

class UserReportFragment : Fragment() {

    lateinit var totalNum_cust:TextView
    lateinit var totalNum_staff:TextView

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.user_report, container, false)

        var report_name:TextView = root.findViewById(R.id.tv_report_name)
        var date_gen_report:TextView = root.findViewById(R.id.tv_date_gen)
        totalNum_cust = root.findViewById(R.id.tv_totalNum_cust)
        totalNum_staff = root.findViewById(R.id.tv_totalNum_staff)

        //get report name
        val year:String = LocalDate.now().year.toString()
        val month:String = LocalDate.now().month.toString()
        report_name.text = "$month $year USER REPORT"

        //get report gen date
        date_gen_report.text = LocalDate.now().toString()

        //********get total cust in the system

        fetchCurrentUser()

        //********get total staff in the system


        return root
    }

    //get info from firebase
    private fun fetchCurrentUser(){
        var totalNumStaff = 0
        var totalNumCust = 0

        val ref = FirebaseDatabase.getInstance().getReference("/User")
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                //to get multiple data
                for (postSnapshot in snapshot.children) {
                    val allUser: User? = postSnapshot.getValue(User::class.java)
                    Log.d("User Report", "Display all user data: ${allUser!!.name}")

                    if(allUser.role == "Staff"){
                        totalNumStaff += 1
                    }else if(allUser.role == "Member"){
                        totalNumCust += 1
                    }
                }

                totalNum_cust.text = totalNumCust.toString()
                totalNum_staff.text = totalNumStaff.toString()



            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }
}