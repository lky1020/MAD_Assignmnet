package com.example.mad_assignment.Staff.Staff_Fragments.staff_manager.Main.Permission

import android.os.Bundle
import android.view.WindowManager
import android.widget.Button
import android.widget.CheckBox
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.mad_assignment.Class.Staff
import com.example.mad_assignment.R
import com.google.android.material.imageview.ShapeableImageView
import com.google.firebase.database.FirebaseDatabase
import com.squareup.picasso.Picasso

class StaffPermissionActivity : AppCompatActivity() {

    private lateinit var cbManageRoom: CheckBox
    private lateinit var cbManageServicesFacilities: CheckBox
    private lateinit var cbManageHousekeeping: CheckBox
    private lateinit var cbManageCheckInOut: CheckBox

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_staff_permission)

        //For status & navigation bar
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Manage Permission"

        //Initialize layout
        val ivStaffImg: ShapeableImageView = findViewById(R.id.iv_staff_img)
        val tvStaffName: TextView = findViewById(R.id.tv_staff_name)
        val tvStaffID: TextView = findViewById(R.id.tv_staff_id)
        cbManageRoom = findViewById(R.id.cb_manage_room)
        cbManageServicesFacilities = findViewById(R.id.cb_manage_services_facilities)
        cbManageHousekeeping = findViewById(R.id.cb_manage_housekeeping)
        cbManageCheckInOut = findViewById(R.id.cb_manage_check_in_out)
        val btnUpdateStaff: Button = findViewById(R.id.btn_update_permission)

        //set layout
        val staff = intent.getParcelableExtra<Staff>("Staff")
        if (staff != null) {
            Picasso.get().load(staff.img).into(ivStaffImg)
            tvStaffName.text = staff.name
            tvStaffID.text = staff.id
            cbManageRoom.isChecked = staff.accessRoom
            cbManageServicesFacilities.isChecked = staff.accessServicesFacilities
            cbManageHousekeeping.isChecked = staff.accessHousekeeping
            cbManageCheckInOut.isChecked = staff.accessCheckInOut
        }

        btnUpdateStaff.setOnClickListener {
            if(cbManageRoom.isChecked || cbManageServicesFacilities.isChecked ||
                cbManageHousekeeping.isChecked || cbManageCheckInOut.isChecked){

                if (staff != null) {
                    updateStaffInformation(staff)

                    //go back to manage activity
                    onBackPressed()

                    Toast.makeText(this, "Staff Permission Updated", Toast.LENGTH_SHORT).show()

                }else{
                    Toast.makeText(this, "Error Occured! Please Refresh App", Toast.LENGTH_SHORT).show()
                }

            }else{
                Toast.makeText(this, "Permisison Required", Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun updateStaffInformation(staff: Staff){
        val staffRef = FirebaseDatabase.getInstance().getReference("/Staff/")

        val updateStaff = Staff(staff.name, staff.id, staff.email, staff.password, staff.phoneNum, staff.img, staff.role,
            staff.status, cbManageRoom.isChecked, cbManageServicesFacilities.isChecked,
            cbManageHousekeeping.isChecked, cbManageCheckInOut.isChecked, staff.uid)

        staffRef.child("${staff.name} - ${staff.uid}").setValue(updateStaff)
    }
}