package com.example.mad_assignment.Staff_Fragments.manager_report

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.example.mad_assignment.R
import com.example.mad_assignment.Staff.Staff_Fragments.manager_report.HousekeepingReportFragment
import com.example.mad_assignment.Staff.Staff_Fragments.manager_report.ReservationReportFragment
import com.example.mad_assignment.Staff.Staff_Fragments.manager_report.UserReportFragment

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


        //redirect to user report page
        cv_user_report.setOnClickListener {
            val ft: FragmentTransaction = activity?.supportFragmentManager!!.beginTransaction()
            ft.replace(R.id.nav_host_fragment_staff, UserReportFragment())
            ft.commit()
        }

        //redirect to reservation report page
        cv_reservation_report.setOnClickListener {
            val ft: FragmentTransaction = activity?.supportFragmentManager!!.beginTransaction()
            ft.replace(R.id.nav_host_fragment_staff, ReservationReportFragment())
            ft.commit()
        }

        //redirect to housekeeping report page
        cv_housekeeping_report.setOnClickListener {
            val ft: FragmentTransaction = activity?.supportFragmentManager!!.beginTransaction()
            ft.replace(R.id.nav_host_fragment_staff, HousekeepingReportFragment())
            ft.commit()
        }


    }
}