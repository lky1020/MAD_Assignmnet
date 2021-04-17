package com.example.mad_assignment.Staff.room.Main

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.widget.*
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mad_assignment.Customer.Booking.Adapter.BookRoomAdapter
import com.example.mad_assignment.Customer.Booking.Class.RoomType
import com.example.mad_assignment.Customer.Booking.Model.BookRoomTypeMenuModel
import com.example.mad_assignment.R
import com.example.mad_assignment.Staff.room.Adapter.RoomsAdapter
import com.example.mad_assignment.Staff.room.Class.Room
import com.example.mad_assignment.Staff.room.Class.RoomMenu
import com.example.mad_assignment.Staff.room.Model.RoomsViewModel
import com.google.firebase.database.DatabaseReference
import com.google.gson.Gson
import kotlinx.android.synthetic.main.manage_room.*

class ManageRoom : AppCompatActivity() {

    private var roomList: ArrayList<Room>? = null

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.manage_room)

        val context = this


        //------------------------------------------------------
        //------- retrieve data from previous page -------------
        //------------------------------------------------------

        val gson = Gson()
        val roomType = gson.fromJson<RoomType>(intent.getStringExtra("RoomType"), RoomType::class.java)

        //------------------------------------------------------
        //---------------------- Toolbar -----------------------
        //------------------------------------------------------

        //set toolbar as support action bar
        val toolbar: Toolbar = findViewById(R.id.appbar_manage_room)
        setSupportActionBar(toolbar)
        supportActionBar?.title = null

        //set title
        val title = findViewById<TextView>(R.id.manage_room_title)
        title.text = roomType.roomType

        //back button
        val backButton = findViewById<ImageView>(R.id.manage_room_back_icon)
        backButton.setOnClickListener{
            finish()
        }

        //------------------------------------------------------
        //------------- Room Type Description ------------------
        //------------------------------------------------------

        mr_total_room.text = roomList?.size.toString()
        mr_occupancy.text = roomType.occupancy.toString()
        mr_price.text = roomType.price.toString()
        mr_size.text = roomType.size.toString() + " m" + Html.fromHtml("&#178")
        mr_beds.text = "Beds: $roomType.beds"


        //------------------------------------------------------
        //------------- Edit Description Button ----------------
        //------------------------------------------------------

        btn_mr_edi_description.setOnClickListener {

        }

        //------------------------------------------------------
        //-------------------- Add button ----------------------
        //------------------------------------------------------

        btn_mr_add_room.setOnClickListener {
            val intent = Intent(context, AddRoom::class.java)

            //pass room type data to next page
           // intent.putExtra("RoomType", roomType)

            context.startActivity(intent)
        }
        //addBtn.isAllCaps = false

        //------------------------------------------------------
        //------------------ Recycle view ----------------------
        //------------------------------------------------------
        val recyclerView: RecyclerView = findViewById(R.id.rv_room_select_item)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = BookRoomAdapter(ArrayList<RoomType>(), this) //Initialize adapter
        recyclerView.setHasFixedSize(true)

        //Get the view model for room type menu
        //bookRoomTypeMenuModel = ViewModelProvider(this).get(BookRoomTypeMenuModel::class.java)

        //Retrieve data from db
       // bookRoomTypeMenuModel.fetchBookRoomType()

        //Observe the room type menu list and set it
       // bookRoomTypeMenuModel.getBookRoomTypeList().observe(this, Observer {
      //      recyclerView.adapter = BookRoomAdapter(it, this)
      //  })

       // bookRoomTypeMenuModel.getStatus().observe(this, Observer {
        //    if (it == false) {
        //        Toast.makeText(this, "No data found", Toast.LENGTH_SHORT).show()
       //     }
      //  })



    }
/*
    fun observeData(){
        viewModel.fetchRoomData().observe(this, Observer{
            adapter.setListData(it)
            adapter.notifyDataSetChanged()
        })
    }*/
}


