package com.example.mad_assignment.Staff.room

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mad_assignment.R
import com.example.mad_assignment.Staff.room.Adapter.RoomsAdapter
import com.example.mad_assignment.Staff.room.Class.Room
import com.example.mad_assignment.Staff.room.Model.RoomsViewModel
import com.google.android.gms.common.api.internal.ActivityLifecycleObserver.of
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class ManageRoom : AppCompatActivity() {

    lateinit var mSearchText: EditText
    lateinit var mRecyclerView: RecyclerView

    lateinit var mDatabase: DatabaseReference

    private lateinit var adapter: RoomsAdapter
    private lateinit var roomViewModel: RoomsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.manage_room)

        //get data from previous page
        var roomType = intent.getStringExtra("RoomType")

        val context = this

        //set toolbar as support action bar
        val toolbar: Toolbar = findViewById(R.id.room_toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = null

        //set title
        val title = findViewById<TextView>(R.id.manage_room_title)
        title.text = roomType

        //back button
        val backButton = findViewById<ImageView>(R.id.staff_room_back_icon)
        backButton.setOnClickListener{
            finish()
        }

        //add button
        val addBtn = findViewById<Button>(R.id.add_room)
        addBtn.setOnClickListener {
            val intent = Intent(context, AddRoom::class.java)

            //pass room type data to next page
            intent.putExtra("RoomType", roomType)

            context.startActivity(intent)
        }
        //addBtn.isAllCaps = false


        //Recycle view
        mSearchText = findViewById(R.id.et_room_search)
        mRecyclerView = findViewById(R.id.rv_rooms)

        mRecyclerView.layoutManager = LinearLayoutManager(this)
        mRecyclerView.adapter = RoomsAdapter(this)
        //mRecyclerView.setHasFixedSize(true)
       // observeData()

        //Get the viewmodel for housekeeping requested
        roomViewModel = ViewModelProvider(this).get(RoomsViewModel::class.java)

        //Fetch data from db
        roomViewModel.fetchRoomData(roomType.toString())

        //Observe the housekeeping list and set it
     //   roomViewModel.getRoomList().observe(viewLifecycleOwner, Observer {
     //       mRecyclerView.adapter = RoomsAdapter(this)
     //   })

     //   roomViewModel.getStatus().observe(viewLifecycleOwner, Observer {
     //       if (it == false) {
     //           Toast.makeText(context, "No Services Requested!", Toast.LENGTH_SHORT).show()
     //       }
     //   })

      /*  val roomList: MutableList<Room> = mutableListOf<Room>()
        roomList.add(Room("SS001", "Available"))

        adapter.setListData(roomList)
        adapter.notifyDataSetChanged()*/


        //firebase
       // mDatabase = FirebaseDatabase.getInstance().getReference("Room").child(roomType.toString())



    }
/*
    fun observeData(){
        viewModel.fetchRoomData().observe(this, Observer{
            adapter.setListData(it)
            adapter.notifyDataSetChanged()
        })
    }*/
}


