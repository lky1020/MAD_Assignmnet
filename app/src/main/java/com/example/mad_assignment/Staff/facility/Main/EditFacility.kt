package com.example.mad_assignment.Staff.facility.Main

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.mad_assignment.R
import com.example.mad_assignment.Staff.facility.Class.Facility
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.edit_facility.*
import java.text.SimpleDateFormat
import java.util.*

class EditFacility : AppCompatActivity() {

    var selectPhotoUri: Uri? = null
    var fromTime: Date? = null
    var toTime: Date? = null

    @SuppressLint("SimpleDateFormat")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.edit_facility)

        //------------------------------------------------------
        //------- retrieve data from previous page -------------
        //------------------------------------------------------

        val gson = Gson()
        val facility = gson.fromJson(intent.getStringExtra("facility"), Facility::class.java)

        Picasso.get().load(facility.img).into(iv_facility_img)
        Picasso.get().isLoggingEnabled = true

        et_edit_facility_name.setText(facility.facilityName)
        et_edit_desc.setText(facility.description)
        et_edit_location.setText(facility.location)
        tv_from.text = SimpleDateFormat("hh:mm aa").format(facility.operationHrStart!!)
        tv_to.text = SimpleDateFormat("hh:mm aa").format(facility.operationHrEnd!!)


        //------------------------------------------------------
        //---------------------- Toolbar -----------------------
        //------------------------------------------------------

        //for toolbar
        setSupportActionBar(facility_toolbar)
        supportActionBar?.title = null;

        //back button
        manage_facility_details_back_icon.setOnClickListener{
            //pass back facility
            val gson = Gson()
            val intent = Intent(this, ManageFacilityDetails::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            intent.putExtra("facility", gson.toJson(facility))
            startActivity(intent)
        }

        //------------------------------------------------------
        //--------------------- Edit Button ---------------------
        //------------------------------------------------------


        btn_edit_room.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setMessage("Are you sure want to Edit?")
                .setCancelable(false)
                .setPositiveButton("Yes") { _, _ ->
                    //validation
                    val name = et_edit_facility_name.text.toString()
                    val desc = et_edit_desc.text.toString()
                    val location = et_edit_location.text.toString()


                    if(name == ""){
                        Toast.makeText(this, "Facility Name cannot be null", Toast.LENGTH_LONG).show()
                    }else if(desc == ""){
                        Toast.makeText(this, "Description cannot be null", Toast.LENGTH_LONG).show()
                    }else if(location == ""){
                        Toast.makeText(this, "Location cannot be null", Toast.LENGTH_LONG).show()
                    }else{
                        //update db
                        facility.facilityName = name
                        facility.description = desc
                        facility.location = location

                        if(fromTime != null){
                            facility.operationHrStart = fromTime
                        }

                        if(toTime != null){
                            facility.operationHrEnd = toTime
                        }


                        if(selectPhotoUri == null){
                            addFacilityToDB(facility, this)
                        }else{
                            facility.img = selectPhotoUri.toString()
                            updateImageDB(selectPhotoUri!!, facility)
                        }


                        //pass back facility
                        val gson = Gson()
                        val intent = Intent(this, ManageFacilityDetails::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                        intent.putExtra("facility", gson.toJson(facility))
                        startActivity(intent)
                    }

                }
                .setNegativeButton("No") { dialog, id ->
                    // Dismiss the dialog
                    dialog.dismiss()
                }
            val alert = builder.create()
            alert.show()
        }

        //------------------------------------------------------
        //--------------------- Img Button ---------------------
        //------------------------------------------------------

        ib_rooms_edit.setOnClickListener {
            val intentAction = Intent(Intent.ACTION_PICK)
            intentAction.type = "image/*"
            startActivityForResult(intentAction, 0)
        }

        //------------------------------------------------------
        //-------------------- Time picker ---------------------
        //------------------------------------------------------
        layout_from.setOnClickListener {
            val cal = Calendar.getInstance()
            val timeSetListener = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
                cal.set(Calendar.HOUR_OF_DAY, hour)
                cal.set(Calendar.MINUTE, minute)
                fromTime = cal.time
                tv_from.text = SimpleDateFormat("hh:mm aa").format(cal.time)
            }
            TimePickerDialog(
                this,
                timeSetListener,
                cal.get(Calendar.HOUR_OF_DAY),
                cal.get(Calendar.MINUTE),
                false
            ).show()
        }

        layout_to.setOnClickListener {
            val cal = Calendar.getInstance()
            val timeSetListener = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
                cal.set(Calendar.HOUR_OF_DAY, hour)
                cal.set(Calendar.MINUTE, minute)
                toTime = cal.time
                tv_to.text = SimpleDateFormat("hh:mm aa").format(cal.time)
            }
            TimePickerDialog(
                this,
                timeSetListener,
                cal.get(Calendar.HOUR_OF_DAY),
                cal.get(Calendar.MINUTE),
                false
            ).show()
        }

    }

    //select photo in phone
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        //proceed and check what the selected image was...
        if (requestCode == 0 && resultCode == Activity.RESULT_OK && data != null) {
            Log.d("Edit Facility", "image is selected")
            selectPhotoUri = data.data
            Picasso.get().load(selectPhotoUri).into(iv_facility_img)
        }
    }


    //upload img to firebase storage and firebase
    private fun updateImageDB(imguri: Uri, facility: Facility) {
        if(imguri != null) {
            val fileName = UUID.randomUUID().toString() +".jpg"
            val ref = FirebaseStorage.getInstance().getReference("facility/$fileName")

            ref.putFile(imguri)
                .addOnSuccessListener {
                    Log.d("Facility", "Successfully uploaded image: ${it.metadata?.path}")

                    ref.downloadUrl.addOnSuccessListener {
                        Log.d("Facility", "File Location: $it")
                        facility.img = it.toString()
                        addFacilityToDB(facility, this)

                    }
                }
                .addOnFailureListener {
                    Log.d("Facility", "Failed to upload image to storage: ${it.message}")
                    Toast.makeText(this, "Your photo is fail to upload", Toast.LENGTH_LONG)
                        .show()
                }
        }
    }


    private fun addFacilityToDB(currentItem: Facility, context: Context){
        val myRef = FirebaseDatabase.getInstance().getReference("Facility/${currentItem.facilityID}")

        myRef.addListenerForSingleValueEvent(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {

                if (snapshot.exists()) {

                    myRef.setValue(currentItem)
                        .addOnSuccessListener {

                            Log.d("Facility", "Successfully edit facility")
                            Toast.makeText(context, "Edit Success", Toast.LENGTH_SHORT).show()
                        }
                        .addOnFailureListener {

                            Log.d("Facility", "Fail to edit facility")
                            Toast.makeText(context, "Fail to Edit", Toast.LENGTH_SHORT).show()
                        }

                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }
}