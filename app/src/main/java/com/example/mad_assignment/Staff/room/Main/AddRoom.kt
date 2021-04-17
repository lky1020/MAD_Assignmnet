package com.example.mad_assignment.Staff.room.Main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import com.example.mad_assignment.R

class AddRoom : AppCompatActivity() {

    lateinit var mRoomNo: EditText
    lateinit var mRoomOccupancy: EditText
    lateinit var mRoomSize: EditText
    lateinit var mRoomPrice: EditText
    //lateinit var mRoomAmenities: EditText
    //lateinit var mRoomImages: EditText
    lateinit var btnAddRoom: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_room)

        val context = this

        //get data from previous page
        var roomType= intent.getStringExtra("RoomType")

        //set toolbar as support action bar
        val toolbar: Toolbar = findViewById(R.id.add_room_toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = null

        //back button
        val backButton = findViewById<ImageView>(R.id.add_room_back_icon)
        backButton.setOnClickListener{
            finish()
        }

        //set subtitle
        val subtitle = findViewById<TextView>(R.id.tv_addroom_subtitle)
        subtitle.setText(roomType)

        //assign value from input
        mRoomNo = findViewById(R.id.input_room_no)
        mRoomOccupancy = findViewById(R.id.input_room_occupancy)
        mRoomSize = findViewById(R.id.input_room_size)
        mRoomPrice = findViewById(R.id.input_room_price)

        //add new room
        btnAddRoom = findViewById(R.id.btn_add_room)
        btnAddRoom.setOnClickListener {
            val roomNo = mRoomNo.text.toString().trim()
            val roomOccupancy = mRoomOccupancy.text
            val roomSize = mRoomSize.text.toString()
            val roomPrice = mRoomPrice.text.toString()


        }
    }
}