package com.example.mad_assignment.Staff.facility.Main

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.mad_assignment.R
import com.example.mad_assignment.Staff.facility.Class.Facility
import com.google.firebase.database.FirebaseDatabase
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.manage_facility_details.*
import java.text.SimpleDateFormat

class ManageFacilityDetails : AppCompatActivity() {
    @SuppressLint("SetTextI18n", "SimpleDateFormat")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.manage_facility_details)

        //------------------------------------------------------
        //---------------------- Toolbar -----------------------
        //------------------------------------------------------

        //for toolbar
        setSupportActionBar(facility_toolbar)
        supportActionBar?.title = null

        //back button
        manage_facility_details_back_icon.setOnClickListener{
            val intent = Intent(this, ManageFacilityMenu::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
        }

        //------------------------------------------------------
        //------- retrieve data from previous page -------------
        //------------------------------------------------------

        val gson = Gson()
        val facility = gson.fromJson(intent.getStringExtra("facility"), Facility::class.java)

        Picasso.get().load(facility.img).into(iv_facility_img)
        Picasso.get().isLoggingEnabled = true

        tv_facility_name.text = "${facility.facilityName}"
        tv_facility_desc.text = "${facility.description}"
        tv_facility_location.text = "Location: ${facility.location}"

        val from = SimpleDateFormat("hh:mm aa").format(facility.operationHrStart)
        val to = SimpleDateFormat("hh:mm aa").format(facility.operationHrEnd)

        tv_facility_operation.text = "Operation Hours: $from to $to"

        //------------------------------------------------------
        //-------------------- delete button -------------------
        //------------------------------------------------------

        btn_delete.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setMessage("Are you sure you want to Delete?")
                .setCancelable(false)
                .setPositiveButton("Yes") { _, _ ->
                    deleteFacility(facility)

                    finish()
                }
                .setNegativeButton("No") { dialog, id ->
                    // Dismiss the dialog
                    dialog.dismiss()
                }
            val alert = builder.create()
            alert.show()

        }

        //------------------------------------------------------
        //-------------------- edit button -------------------
        //------------------------------------------------------

        btn_edit.setOnClickListener {
            val gson = Gson()
            val intent = Intent(this, EditFacility::class.java)
            intent.putExtra("facility", gson.toJson(facility))
            startActivity(intent)
        }

    }

    private fun deleteFacility(currentItem: Facility){

        val myRef = FirebaseDatabase.getInstance().getReference("Facility")

            myRef.child(currentItem.facilityID!!).removeValue()
                .addOnSuccessListener {
                    Log.d("Facility", "Successfully delete room")
                    Toast.makeText(this, "Delete Success", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener{
                    Log.d("Facility", "Fail to delete")
                    Toast.makeText(this, "Fail to delete", Toast.LENGTH_SHORT).show()
                }

    }
}