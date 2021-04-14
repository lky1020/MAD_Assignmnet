package com.example.mad_assignment.Staff.Staff_Fragments

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.example.mad_assignment.Customer.Cust_Staff_Fragments.logout.LogoutFragment
import com.example.mad_assignment.R
import com.example.mad_assignment.Staff.Staff_Fragments.staff_housekeeping.Main.Main.StaffHousekeepingMainFragment


//belongs to staff_fragment_home.xml
class StaffHomeFragment : Fragment() {

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        //temp variable  -- need retrieve total cust chk in & out
        var numIn = 20
        var numOut = 20
        val user_role = activity?.intent?.getStringExtra("Role")


        val root = inflater.inflate(R.layout.staff_fragment_home, container, false)

        var tv_Num_ChkIn: TextView = root.findViewById(R.id.tv_ChkIn_totalNum)
        var tv_Num_ChkOut: TextView = root.findViewById(R.id.tv_ChkOut_totalNum)

        //card views
        val cv_chkIn: CardView = root.findViewById(R.id.cv_chkIn)
        val cv_chkOut: CardView = root.findViewById(R.id.cv_chkOut)
        val cv_houseKeeper: CardView = root.findViewById(R.id.cv_manage_housekeeper)
        val cv_room: CardView = root.findViewById(R.id.cv_manage_room)
        val cv_facility: CardView = root.findViewById(R.id.cv_manage_facilities)
        val cv_staff: CardView = root.findViewById(R.id.cv_manage_staff)

        //  staffHomeViewModel.text.observe(viewLifecycleOwner, Observer {
        tv_Num_ChkIn.text = numIn.toString()//it
        tv_Num_ChkOut.text = numOut.toString()//it

        if (user_role == "Manager") {
            cv_staff.visibility = View.VISIBLE
        } else {
            cv_staff.visibility = View.INVISIBLE
        }

        //*************redirect to chk in page
        cv_chkIn.setOnClickListener() {
            //the class.java file is TEMPORARY
            /*  val ft: FragmentTransaction = activity?.supportFragmentManager!!.beginTransaction()
            ft.replace(R.id.nav_host_fragment_staff, StaffHousekeepingMainFragment())

            ft.commit()*/
            /*
            //val intent = Intent (this@StaffHomeFragment.context, LogoutFragment::class.java)
            //startActivity(intent)
             */
        }

        //*************redirect to chk out page
        cv_chkOut.setOnClickListener() {
        //the class.java file is TEMPORARY
                    /*  val ft: FragmentTransaction = activity?.supportFragmentManager!!.beginTransaction()
                    ft.replace(R.id.nav_host_fragment_staff, StaffHousekeepingMainFragment())

                    ft.commit()*/
                    /*
            //val intent = Intent (this@StaffHomeFragment.context, LogoutFragment::class.java)
            //startActivity(intent)
             */
        }

        //*************redirect to manage house keeper page
        cv_houseKeeper.setOnClickListener() {
            val ft: FragmentTransaction = activity?.supportFragmentManager!!.beginTransaction()
            ft.replace(R.id.nav_host_fragment_staff, StaffHousekeepingMainFragment())
            ft.commit()
        }

        //*************redirect to manage room page
        cv_room.setOnClickListener() {
        //the class.java file is TEMPORARY
        /*  val ft: FragmentTransaction = activity?.supportFragmentManager!!.beginTransaction()
                    ft.replace(R.id.nav_host_fragment_staff, StaffHousekeepingMainFragment())

                    ft.commit()*/
                    /*
            //val intent = Intent (this@StaffHomeFragment.context, LogoutFragment::class.java)
            //startActivity(intent)
             */
        }

        //*************redirect to manage facility page
        cv_facility.setOnClickListener() {
        //the class.java file is TEMPORARY
        /*  val ft: FragmentTransaction = activity?.supportFragmentManager!!.beginTransaction()
                    ft.replace(R.id.nav_host_fragment_staff, StaffHousekeepingMainFragment())

                    ft.commit()*/
                    /*
            //val intent = Intent (this@StaffHomeFragment.context, LogoutFragment::class.java)
            //startActivity(intent)
             */

        }

        //*************redirect to manage staff page
        cv_staff.setOnClickListener() {
        //the class.java file is TEMPORARY
        /*  val ft: FragmentTransaction = activity?.supportFragmentManager!!.beginTransaction()
                    ft.replace(R.id.nav_host_fragment_staff, StaffHousekeepingMainFragment())

                    ft.commit()*/
                    /*
            //val intent = Intent (this@StaffHomeFragment.context, LogoutFragment::class.java)
            //startActivity(intent)
             */

        }


        return root
    }
}