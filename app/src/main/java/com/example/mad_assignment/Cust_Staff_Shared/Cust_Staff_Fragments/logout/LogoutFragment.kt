package com.example.mad_assignment.Customer.Cust_Staff_Fragments.logout

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.mad_assignment.Cust_Staff_Shared.Cust_Staff_Fragments.Profile.ProfileFragment
import com.example.mad_assignment.CustomerMain
import com.example.mad_assignment.MainActivity
import com.example.mad_assignment.R
import com.example.mad_assignment.Staff.Staff_Fragments.staff_housekeeping.Main.Main.StaffHousekeepingMainFragment

//belongs to fragment_logout.xml
class LogoutFragment : Fragment() {

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        //prompt to ask confirmation message
        val confirmBox = AlertDialog.Builder(context)
        confirmBox.setTitle("LOGOUT")
        confirmBox.setMessage("Please click 'Confirm' to logout.")
        confirmBox.setPositiveButton("Confirm") { dialogInterface: DialogInterface, i: Int ->
            Toast.makeText(context, "Logout Successfully!", Toast.LENGTH_SHORT).show()

            val intent = Intent(context, MainActivity::class.java)
            startActivity(intent)
        }
        confirmBox.setNegativeButton("Cancel") { dialogInterface: DialogInterface, i: Int ->
            Toast.makeText(context, "Cancel Logout!", Toast.LENGTH_SHORT).show()

            val ft: FragmentTransaction = activity?.supportFragmentManager!!.beginTransaction()
            ft.replace(R.id.nav_host_fragment, ProfileFragment())
            ft.commit()

        }
        confirmBox.show()

        return null
    }
}