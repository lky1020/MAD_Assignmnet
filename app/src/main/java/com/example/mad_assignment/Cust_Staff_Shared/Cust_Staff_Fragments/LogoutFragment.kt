package com.example.mad_assignment.Cust_Staff_Shared.Cust_Staff_Fragments

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.mad_assignment.Class.Staff
import com.example.mad_assignment.Class.User
import com.example.mad_assignment.Cust_Staff_Shared.Cust_Staff_Fragments.Login.Companion.currentUser
import com.example.mad_assignment.CustomerMain
import com.example.mad_assignment.MainActivity
import com.example.mad_assignment.StaffMain
import com.google.firebase.database.*

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

            if(currentUser?.role == "Staff"){
                updateStaffStatus(currentUser!!)
            }

            //logout and remove the rmb me check box
            var preferences: SharedPreferences? = this.activity?.getSharedPreferences("chkBox", AppCompatActivity.MODE_PRIVATE)
            var editor:SharedPreferences.Editor = preferences!!.edit()
            editor.putString("chkRmbMe", "false")
            editor.apply()


            Toast.makeText(context, "Logout Successfully!", Toast.LENGTH_SHORT).show()

            val intent = Intent(context, MainActivity::class.java)
            startActivity(intent)
        }
        confirmBox.setNegativeButton("Cancel") { dialogInterface: DialogInterface, i: Int ->
            Toast.makeText(context, "Cancel Logout!", Toast.LENGTH_SHORT).show()

            //val ft: FragmentTransaction = activity?.supportFragmentManager!!.beginTransaction()

            if(currentUser?.role == "Staff"){
                val intentStaffMain = Intent(context, StaffMain::class.java)
                intentStaffMain.putExtra("Role", "Staff")
                startActivity(intentStaffMain)
                //ft.replace(R.id.nav_host_fragment_staff, StaffHomeFragment())

            }else if(currentUser?.role == "Manager"){
                val intentStaffMain = Intent(context, StaffMain::class.java)
                intentStaffMain.putExtra("Role", "Manager")
                startActivity(intentStaffMain)
            }
            else{
                val intentCustMain = Intent(context, CustomerMain::class.java)
                startActivity(intentCustMain)
                //ft.replace(R.id.nav_host_fragment, CustHomeFragment())
            }

            //ft.commit()
        }
        confirmBox.show()

        return null
    }


    // update staff status to firebase
    private fun updateStaffStatus(currentUser: User){

        val query: Query = FirebaseDatabase.getInstance().getReference("Staff")
                .orderByChild("uid")
                .equalTo(currentUser.uid)

        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){

                    for(i in snapshot.children){
                        val staff = i.getValue(Staff::class.java)

                        if (staff != null) {
                            if(staff.uid == currentUser.uid){
                                val updateStaffStatus = Staff(currentUser.name, staff.id, staff.email, staff.password, staff.phoneNum, staff.img, staff.role,
                                        "Offline", staff.accessRoom, staff.accessServicesFacilities,
                                        staff.accessHousekeeping, staff.accessCheckInOut, staff.uid)

                                snapshot.ref.child("${staff.name} - ${staff.uid}").setValue(updateStaffStatus)
                            }
                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }
}