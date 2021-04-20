package com.example.mad_assignment.Staff.Staff_Fragments

import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import com.example.mad_assignment.Class.Staff
import com.example.mad_assignment.Class.User
import com.example.mad_assignment.Cust_Staff_Shared.Cust_Staff_Fragments.Login.Companion.currentUser
import com.example.mad_assignment.R
import com.example.mad_assignment.Staff.Staff_Fragments.staff_checkInOut.Main.StaffCheckInOutMainActivity
import com.example.mad_assignment.Staff.Staff_Fragments.staff_housekeeping.Main.Main.StaffHousekeepingMainActivity
import com.example.mad_assignment.Staff.Staff_Fragments.staff_manager.Main.Staff.StaffManagerActivity
import com.example.mad_assignment.Staff.facility.Main.ManageFacilityMenu
import com.example.mad_assignment.Staff.room.Main.ManageRoomMenu
import com.google.firebase.database.*
import java.time.LocalDate

//belongs to staff_fragment_home.xml
class StaffHomeFragment : Fragment() {

    companion object {
        //for static purpose - to store no. of chk in chk out
        var PREFS_NUM_CHK: String? = "PrefsFile"

    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        //allow app bar
        (requireActivity() as AppCompatActivity).supportActionBar?.show()

        return inflater.inflate(R.layout.staff_fragment_home, container, false)

    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // variable
        var numIn: Int = 0
        var numOut: Int = 0
        val user_role = currentUser!!.role

        var tv_Num_ChkIn: TextView = view.findViewById(R.id.tv_ChkIn_totalNum)
        var tv_Num_ChkOut: TextView = view.findViewById(R.id.tv_ChkOut_totalNum)

        //card views
        val cv_chkIn: CardView = view.findViewById(R.id.cv_chkIn)
        val cv_chkOut: CardView = view.findViewById(R.id.cv_chkOut)
        val cv_houseKeeper: CardView = view.findViewById(R.id.cv_manage_housekeeper)
        val cv_room: CardView = view.findViewById(R.id.cv_manage_room)
        val cv_facility: CardView = view.findViewById(R.id.cv_manage_facilities)
        val cv_staff: CardView = view.findViewById(R.id.cv_manage_staff)

        //get share preference data
        var preferencesChk: SharedPreferences? = this.activity?.getSharedPreferences(PREFS_NUM_CHK , MODE_PRIVATE)

        //get today date of preference
        val chkTodayDate:String? = preferencesChk!!.getString("pref_chkDate", "null")

        //cmp today date of preference
        if(chkTodayDate == "null"){
            var editor: SharedPreferences.Editor = preferencesChk!!.edit()
            editor.putString("pref_chkDate", LocalDate.now().toString())
            editor.apply()
        }else if(chkTodayDate!! < LocalDate.now().toString()){ //if today date of preference is not current date, then will reset
            var editor: SharedPreferences.Editor = preferencesChk!!.edit()
            editor.putInt("pref_chkIn", 0)
            editor.putInt("pref_chkOut", 0)
            editor.putString("pref_chkDate", LocalDate.now().toString())
            editor.apply()
        }

        //get pref_chkIn/Out
        numIn = preferencesChk!!.getInt("pref_chkIn", 0)
        numOut = preferencesChk!!.getInt("pref_chkOut", 0)

        Log.d("Staff Homepage", "Number of check in: $numIn")


        tv_Num_ChkIn.text = numIn.toString( )
        tv_Num_ChkOut.text = numOut.toString()

        if (user_role == "Manager") {
            cv_staff.visibility = View.VISIBLE
        } else {
            cv_staff.visibility = View.INVISIBLE
        }

        if(user_role == "Staff"){
            //Will check permission
            val currentUser: User = currentUser!! //activity?.intent?.getParcelableExtra("user")!!
            setStaffView(currentUser, cv_chkIn, cv_chkOut, cv_houseKeeper, cv_room, cv_facility, cv_staff)

        }else{

            //Manager (will not check permission)
            //redirect to chk in page
            cv_chkIn.setOnClickListener {
                activity?.let{
                    val intent = Intent (it, StaffCheckInOutMainActivity::class.java)
                    intent.putExtra("type", "check in")
                    it.startActivity(intent)
                }
            }

            //redirect to chk out page
            cv_chkOut.setOnClickListener {
                activity?.let{
                    val intent = Intent (it, StaffCheckInOutMainActivity::class.java)
                    intent.putExtra("type", "check out")
                    it.startActivity(intent)
                }
            }

            //redirect to manage house keeper page
            cv_houseKeeper.setOnClickListener {
                activity?.let{
                    val intent = Intent (it, StaffHousekeepingMainActivity::class.java)
                    it.startActivity(intent)
                }
            }

            //redirect to manage room page
            cv_room.setOnClickListener {
                activity?.let{
                    val intent = Intent (it, ManageRoomMenu::class.java)
                    it.startActivity(intent)
                }
            }

            //redirect to manage facility page
            cv_facility.setOnClickListener {
                activity?.let{
                    val intent = Intent (it, ManageFacilityMenu::class.java)
                    it.startActivity(intent)
                }

            }

            //redirect to manage staff page
            cv_staff.setOnClickListener {
                activity?.let{
                    val intent = Intent (it, StaffManagerActivity::class.java)
                    it.startActivity(intent)
                }
            }

        }
    }

    private fun setStaffView(currentUser: User, cv_chkIn: CardView, cv_chkOut: CardView, cv_houseKeeper: CardView, cv_room: CardView, cv_facility: CardView, cv_staff: CardView) {

        val query: Query = FirebaseDatabase.getInstance().getReference("Staff")
                .orderByChild("uid")
                .equalTo(currentUser.uid)

        query.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {

                    for (i in snapshot.children) {
                        val staffDetail = i.getValue(Staff::class.java)

                        if (staffDetail != null) {
                            if (staffDetail.uid == currentUser.uid) {

                                    //redirect to chk in page
                                    cv_chkIn.setOnClickListener {
                                        if (staffDetail.accessCheckInOut) {
                                            activity?.let {
                                                val intent = Intent(it, StaffCheckInOutMainActivity::class.java)
                                                intent.putExtra("type", "check in")
                                                it.startActivity(intent)
                                            }

                                        } else {
                                            Toast.makeText(requireActivity(), "Permission Denied!", Toast.LENGTH_SHORT).show()
                                        }
                                    }


                                    //redirect to chk out page
                                    cv_chkOut.setOnClickListener {
                                        if (staffDetail.accessCheckInOut) {
                                            activity?.let{
                                                val intent = Intent (it, StaffCheckInOutMainActivity::class.java)
                                                intent.putExtra("type", "check out")
                                                it.startActivity(intent)
                                            }

                                        }else {
                                            Toast.makeText(requireActivity(), "Permission Denied!", Toast.LENGTH_SHORT).show()
                                        }

                                    }

                                //redirect to manage house keeper page
                                cv_houseKeeper.setOnClickListener {
                                    if (staffDetail.accessHousekeeping) {
                                        activity?.let{
                                            val intent = Intent (it, StaffHousekeepingMainActivity::class.java)
                                            it.startActivity(intent)
                                        }
                                    }else{
                                        Toast.makeText(requireActivity(), "Permission Denied!", Toast.LENGTH_SHORT).show()
                                    }
                                }

                                //redirect to manage room page
                                cv_room.setOnClickListener {
                                    if (staffDetail.accessRoom) {
                                        activity?.let{
                                            val intent = Intent (it, ManageRoomMenu::class.java)
                                            it.startActivity(intent)
                                        }
                                    }else{
                                        Toast.makeText(requireActivity(), "Permission Denied!", Toast.LENGTH_SHORT).show()
                                    }
                                }


                                //*************redirect to manage facility page
                                cv_facility.setOnClickListener {
                                    if (staffDetail.accessServicesFacilities) {
                                        activity?.let {
                                            val intent = Intent(it, ManageFacilityMenu::class.java)
                                            it.startActivity(intent)
                                        }
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