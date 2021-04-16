package com.example.mad_assignment.Staff.Staff_Fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.example.mad_assignment.Class.Staff
import com.example.mad_assignment.Class.User
import com.example.mad_assignment.R
import com.example.mad_assignment.Staff.Staff_Fragments.staff_housekeeping.Main.Main.StaffHousekeepingMainFragment

import com.example.mad_assignment.Staff.Staff_Fragments.staff_manager.Main.Staff.StaffManagerFragment
import com.google.firebase.database.*

//belongs to staff_fragment_home.xml
class StaffHomeFragment : Fragment() {

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        //allow app bar
        (requireActivity() as AppCompatActivity).supportActionBar?.show()

        return inflater.inflate(R.layout.staff_fragment_home, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //temp variable  -- need retrieve total cust chk in & out
        var numIn = 20
        var numOut = 20
        val user_role = activity?.intent?.getStringExtra("Role")

        var tv_Num_ChkIn: TextView = view.findViewById(R.id.tv_ChkIn_totalNum)
        var tv_Num_ChkOut: TextView = view.findViewById(R.id.tv_ChkOut_totalNum)

        //card views
        val cv_chkIn: CardView = view.findViewById(R.id.cv_chkIn)
        val cv_chkOut: CardView = view.findViewById(R.id.cv_chkOut)
        val cv_houseKeeper: CardView = view.findViewById(R.id.cv_manage_housekeeper)
        val cv_room: CardView = view.findViewById(R.id.cv_manage_room)
        val cv_facility: CardView = view.findViewById(R.id.cv_manage_facilities)
        val cv_staff: CardView = view.findViewById(R.id.cv_manage_staff)

        //  staffHomeViewModel.text.observe(viewLifecycleOwner, Observer {
        tv_Num_ChkIn.text = numIn.toString()//it
        tv_Num_ChkOut.text = numOut.toString()//it

        if (user_role == "Manager") {
            cv_staff.visibility = View.VISIBLE
        } else {
            cv_staff.visibility = View.INVISIBLE
        }

        if(user_role == "Staff"){
            //Will check permission
            val currentUser: User = activity?.intent?.getParcelableExtra("user")!!
            setStaffView(currentUser, cv_chkIn, cv_chkOut, cv_houseKeeper, cv_room, cv_facility, cv_staff)

        }else{

            //Manager (will not check permission)
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

            //redirect to manage house keeper page
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

            //redirect to manage staff page
            cv_staff.setOnClickListener() {
                val ft: FragmentTransaction = activity?.supportFragmentManager!!.beginTransaction()
                ft.replace(R.id.nav_host_fragment_staff, StaffManagerFragment())
                ft.commit()

            }

        }
    }

    private fun setStaffView(currentUser: User, cv_chkIn: CardView, cv_chkOut: CardView, cv_houseKeeper: CardView, cv_room: CardView, cv_facility: CardView, cv_staff: CardView) {

        val query: Query = FirebaseDatabase.getInstance().getReference("Staff")
                .orderByChild("uid")
                .equalTo(currentUser.uid)

        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {

                    for (i in snapshot.children) {
                        val staffDetail = i.getValue(Staff::class.java)

                        if (staffDetail != null) {
                            if (staffDetail.uid == currentUser.uid) {


                                    //*************redirect to chk in page
                                    cv_chkIn.setOnClickListener() {

                                    if (staffDetail.accessCheckInOut) {
                                        //the class.java file is TEMPORARY
                                        /*  val ft: FragmentTransaction = activity?.supportFragmentManager!!.beginTransaction()
                                        ft.replace(R.id.nav_host_fragment_staff, StaffHousekeepingMainFragment())

                                        ft.commit()*/
                                        /*
                                        //val intent = Intent (this@StaffHomeFragment.context, LogoutFragment::class.java)
                                        //startActivity(intent)
                                         */
                                    }else {
                                        Toast.makeText(requireActivity(), "Permission Denied!", Toast.LENGTH_SHORT).show()
                                    }




                                    //*************redirect to chk out page
                                    cv_chkOut.setOnClickListener() {
                                        if (staffDetail.accessCheckInOut) {
                                            //the class.java file is TEMPORARY
                                            /*  val ft: FragmentTransaction = activity?.supportFragmentManager!!.beginTransaction()
                                            ft.replace(R.id.nav_host_fragment_staff, StaffHousekeepingMainFragment())

                                            ft.commit()*/
                                            /*
                                            //val intent = Intent (this@StaffHomeFragment.context, LogoutFragment::class.java)
                                            //startActivity(intent)
                                             */
                                        }else {
                                            Toast.makeText(requireActivity(), "Permission Denied!", Toast.LENGTH_SHORT).show()
                                        }

                                    }
                                }


                                //redirect to manage house keeper page
                                cv_houseKeeper.setOnClickListener() {

                                    if (staffDetail.accessHousekeeping) {
                                        val ft: FragmentTransaction = activity?.supportFragmentManager!!.beginTransaction()
                                        ft.replace(R.id.nav_host_fragment_staff, StaffHousekeepingMainFragment())
                                        ft.commit()
                                    }else{
                                        Toast.makeText(requireActivity(), "Permission Denied!", Toast.LENGTH_SHORT).show()
                                    }
                                }


                                //*************redirect to manage room page
                                cv_room.setOnClickListener() {
                                    if (staffDetail.accessRoom) {
                                        //the class.java file is TEMPORARY
                                        /*  val ft: FragmentTransaction = activity?.supportFragmentManager!!.beginTransaction()
                                                    ft.replace(R.id.nav_host_fragment_staff, StaffHousekeepingMainFragment())

                                                    ft.commit()*/
                                        /*
                                        //val intent = Intent (this@StaffHomeFragment.context, LogoutFragment::class.java)
                                        //startActivity(intent)
                                         */
                                    }else{
                                        Toast.makeText(requireActivity(), "Permission Denied!", Toast.LENGTH_SHORT).show()
                                    }
                                }


                                //*************redirect to manage facility page
                                cv_facility.setOnClickListener() {
                                    if (staffDetail.accessServicesFacilities) {
                                        //the class.java file is TEMPORARY
                                        /*  val ft: FragmentTransaction = activity?.supportFragmentManager!!.beginTransaction()
                                                    ft.replace(R.id.nav_host_fragment_staff, StaffHousekeepingMainFragment())

                                                    ft.commit()*/
                                        /*
                                        //val intent = Intent (this@StaffHomeFragment.context, LogoutFragment::class.java)
                                        //startActivity(intent)
                                         */
                                    }else{
                                        Toast.makeText(requireActivity(), "Permission Denied!", Toast.LENGTH_SHORT).show()
                                    }
                                }
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