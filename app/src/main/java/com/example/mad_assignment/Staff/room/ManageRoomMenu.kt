package com.example.mad_assignment.Staff.room

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mad_assignment.Staff.room.Class.RoomMenu
import com.example.mad_assignment.R
import com.example.mad_assignment.Staff.room.Adapter.ManageRoomMenuAdapter


class ManageRoomMenu : AppCompatActivity() {
    private var recyclerView: RecyclerView? = null
    private var recyclerDataArrayList: ArrayList<RoomMenu>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.manage_room_menu)

        //for toolbar
        val toolbar: Toolbar = findViewById(R.id.room_menu_toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = null;

        //for room menu recycle view item
        recyclerView = findViewById(R.id.rvManageRoomMenu)

        recyclerDataArrayList = ArrayList()

        //added data to array list
        recyclerDataArrayList!!.add(RoomMenu("Standard Single Room", R.drawable.bg1))
        recyclerDataArrayList!!.add(RoomMenu("Standard Double Room", R.drawable.bg1))
        recyclerDataArrayList!!.add(RoomMenu("Deluxe Double Room", R.drawable.bg1))
        recyclerDataArrayList!!.add(RoomMenu("Standard Twin Room", R.drawable.bg1))
        recyclerDataArrayList!!.add(RoomMenu("Superor Queen Room", R.drawable.bg1))
        recyclerDataArrayList!!.add(RoomMenu("Family Room", R.drawable.bg1))
        recyclerDataArrayList!!.add(RoomMenu("Deluxe Studio", R.drawable.bg1))
        recyclerDataArrayList!!.add(RoomMenu("Triple Room", R.drawable.bg1))

        //added data from arraylist to adapter class.
        val adapter = ManageRoomMenuAdapter(recyclerDataArrayList!!, this)


        //setting grid layout manager to implement grid view.
        //'2' represents number of columns to be displayed in grid view.
        val layoutManager = GridLayoutManager(this, 2)

        //set adapter to recycler view.
        recyclerView?.also{
            recyclerView!!.setLayoutManager(layoutManager)
            recyclerView!!.setAdapter(adapter)
        }



    }
}