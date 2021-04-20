package com.example.mad_assignment.Staff.room.Main

import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mad_assignment.Customer.Booking.Class.RoomType
import com.example.mad_assignment.Customer.Booking.Model.BookRoomTypeMenuModel
import com.example.mad_assignment.R
import com.example.mad_assignment.Staff.room.Adapter.ManageRoomMenuAdapter


class ManageRoomMenu : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.manage_room_menu)

        //------------------------------------------------------
        //---------------------- Toolbar -----------------------
        //------------------------------------------------------

        //for toolbar
        val toolbar: Toolbar = findViewById(R.id.room_menu_toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = null

        //back button
        val backButton = findViewById<ImageView>(R.id.manage_room_menu_back_icon)
        backButton.setOnClickListener{
            finish()
        }

        //------------------------------------------------------
        //------------------ Recycle view ----------------------
        //------------------------------------------------------

        val recyclerView: RecyclerView = findViewById(R.id.rvManageRoomMenu)
        recyclerView.layoutManager = GridLayoutManager(this, 2)

        //Initialize adapter
        recyclerView.adapter = ManageRoomMenuAdapter(ArrayList<RoomType>(), this)
        recyclerView.setHasFixedSize(true)

        //Get the view model for room type menu
        var roomTypeMenuModel = ViewModelProvider(this).get(BookRoomTypeMenuModel::class.java)

        //Retrieve data from db
        roomTypeMenuModel.fetchBookRoomType()

        //Observe the room type menu list and set it
        roomTypeMenuModel.getBookRoomTypeList().observe(this, Observer {
            recyclerView.adapter = ManageRoomMenuAdapter(it, this)
        })

        roomTypeMenuModel.getStatus().observe(this, Observer {
            if (it == false) {
                Toast.makeText(this, "No data found", Toast.LENGTH_SHORT).show()
            }
        })



    }
}