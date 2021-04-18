package com.example.mad_assignment.Staff_Fragments.manager_report

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.mad_assignment.Class.User
import com.example.mad_assignment.Cust_Staff_Shared.Cust_Staff_Fragments.Login
import com.example.mad_assignment.R
import com.example.mad_assignment.Staff.Staff_Fragments.StaffHomeFragment
import com.example.mad_assignment.Staff.Staff_Fragments.manager_report.UserReportFragment
import com.example.mad_assignment.Staff.Staff_Fragments.staff_checkInOut.Main.StaffCheckInOutMainActivity
import com.example.mad_assignment.Staff.Staff_Fragments.staff_housekeeping.Main.Main.StaffHousekeepingMainActivity
import com.example.mad_assignment.Staff.Staff_Fragments.staff_housekeeping.Main.Main.StaffHousekeepingMainFragment
import com.example.mad_assignment.Staff.Staff_Fragments.staff_manager.Main.Staff.StaffManagerActivity
import com.example.mad_assignment.Staff.room.Main.ManageRoomMenu
import java.time.LocalDate

//belongs to staff_fragment_home.xml
class ManagerReportFragment : Fragment() {

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        val root = inflater.inflate(R.layout.staff_fragment_report, container, false)

        return root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //card views
        val cv_user_report: CardView = view.findViewById(R.id.cv_user_report)
        val cv_reservation_report: CardView = view.findViewById(R.id.cv_reservation_report)
        val cv_housekeeping_report: CardView = view.findViewById(R.id.cv_housekeeping_report)
        val cv_facility_report: CardView = view.findViewById(R.id.cv_facility_report)


        //redirect to user report page
        cv_user_report.setOnClickListener() {
            val ft: FragmentTransaction = activity?.supportFragmentManager!!.beginTransaction()
            ft.replace(R.id.nav_host_fragment_staff, UserReportFragment())
            ft.commit()
        }

        //*************redirect to reservation report page
        cv_reservation_report.setOnClickListener() {
            /*
            activity?.let{
                val intent = Intent (it, StaffCheckInOutMainActivity::class.java)
                intent.putExtra("type", "check out")
                it.startActivity(intent)
            }

             */
        }

        //redirect to housekeeping report page
        cv_housekeeping_report.setOnClickListener() {
            /*
            activity?.let{
                val intent = Intent (it, StaffHousekeepingMainActivity::class.java)
                it.startActivity(intent)
            }

             */
        }

        //*************redirect to facility report page
        cv_facility_report.setOnClickListener() {
            /*
            activity?.let{
                val intent = Intent (it, ManageRoomMenu::class.java)
                it.startActivity(intent)
            }

             */
        }

    }
}