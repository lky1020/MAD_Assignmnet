package com.example.mad_assignment.Staff.room.Main

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.Html
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mad_assignment.Customer.Booking.Class.RoomType
import com.example.mad_assignment.R
import com.example.mad_assignment.Staff.room.Adapter.RoomsAdapter
import com.example.mad_assignment.Staff.room.Class.Room
import com.example.mad_assignment.Staff.room.Model.RoomsViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.alert_add_room.view.*
import kotlinx.android.synthetic.main.manage_room.*


class ManageRoom : AppCompatActivity() {

    private var roomList = java.util.ArrayList<Room>()
    private lateinit var etSearch: EditText
    private lateinit var recyclerView: RecyclerView
    private lateinit var roomsViewModel: RoomsViewModel

    @SuppressLint("SetTextI18n", "UseSwitchCompatOrMaterialCode", "ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.manage_room)

        val context = this

        //------------------------------------------------------
        //------- retrieve data from previous page -------------
        //------------------------------------------------------

        val gson = Gson()
        val roomType = gson.fromJson(intent.getStringExtra("RoomType"), RoomType::class.java)

        //------------------------------------------------------
        //---------------------- Toolbar -----------------------
        //------------------------------------------------------

        //set toolbar as support action bar
        val toolbar: Toolbar = findViewById(R.id.room_menu_toolbar)
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

        Picasso.get().load(roomType.img).into(iv_roomType)
        Picasso.get().isLoggingEnabled = true

        tv_room_occupancy.text = "Occupancy: ${roomType.occupancy}"
        tv_room_price.text = "Price: RM${roomType.price?.format(2)}"
        tv_room_size.text = "Size: "+roomType.size.toString() + " m" + Html.fromHtml("&#178")
        tv_room_beds.text = "Beds: ${roomType.beds}"


        //------------------------------------------------------
        //------------------ Recycle view ----------------------
        //------------------------------------------------------

        //recycle view
        recyclerView = findViewById(R.id.rv_rooms)
        recyclerView.layoutManager = LinearLayoutManager(this)

        recyclerView.adapter = RoomsAdapter(ArrayList<Room>(), this) //Initialize adapter
        recyclerView.setHasFixedSize(true)

        //Get the view model for room type menu
        roomsViewModel = ViewModelProvider(this).get(RoomsViewModel::class.java)

        //Retrieve data from db
        roomType.roomID?.let { roomsViewModel.fetchRoomData(it) }

        //Observe the room type menu list and set it
        roomsViewModel.getRoomList().observe(this, Observer {
            recyclerView.adapter = RoomsAdapter(it, this)
            roomList = it
        })

        roomsViewModel.getStatus().observe(this, Observer {
            if (it == false) {
                Toast.makeText(this, "No room found", Toast.LENGTH_SHORT).show()
            }
        })

        //------------------------------------------------------
        //--------------------- Search -------------------------
        //------------------------------------------------------

        etSearch = findViewById(R.id.rooms_search)



        etSearch.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {
                if(!etSearch.text.equals("")){
                    filter(s.toString())
                }else{
                    recyclerView.adapter = RoomsAdapter(roomList, context)
                }
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

            }
        })

        //------------------------------------------------------
        //------------- Edit Description Button ----------------
        //------------------------------------------------------

        btn_edit_room_desc.setOnClickListener {

            //pass data to next activity
            val gson = Gson()
            val intent = Intent(this, EditRoomDesc::class.java)
            intent.putExtra("roomType", gson.toJson(roomType))
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)

        }

        //------------------------------------------------------
        //-------------------- Add button ----------------------
        //------------------------------------------------------

        btn_add_rooms.setOnClickListener {
            val mDialogView = LayoutInflater.from(context).inflate(R.layout.alert_add_room, null)
            val dialog = androidx.appcompat.app.AlertDialog.Builder(context)
            mDialogView.et_room_no.hint = "${roomType.roomID}01"

            dialog.setCancelable(false)
                    .setTitle("Add New Room")
                    .setPositiveButton("Add") { dialog, _ ->

                        val roomNo = mDialogView.et_room_no.text.toString()

                        val status:String = if(mDialogView.switch_add_room_status.isChecked)
                            "available"
                        else
                            "not available"

                        //validation

                        if(roomNo.equals(null)){
                            Toast.makeText(context, "Room No cannot be null", Toast.LENGTH_SHORT).show()
                        }else if(!(roomNo.take(2).equals(roomType.roomID))){
                            Toast.makeText(context, "First two character must be ${roomType.roomID}", Toast.LENGTH_SHORT).show()
                        }else{
                                //add new room in database
                                val room:Room = Room(roomNo, roomType, status)
                                addRoom(room, this)
                        }

                        //dismiss dialog
                        dialog.dismiss()


                    }
                    .setNegativeButton("Cancel") { dialog, _ ->
                        //dismiss dialog
                        dialog.dismiss()
                    }
                    .setView(mDialogView)
            dialog.create().show()


        }



    }

    private fun addRoom(currentItem: Room, context: Context){
        val myRef = FirebaseDatabase.getInstance().getReference("Rooms")
                .child(currentItem.roomType?.roomID.toString())
                .child(currentItem.roomNo.toString())


        myRef.addListenerForSingleValueEvent(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                if (!snapshot.exists()) {
                    myRef.setValue(currentItem)
                            .addOnSuccessListener {
                                Log.d("Manage Room", "Successfully add room")
                                Toast.makeText(context, "Add Success", Toast.LENGTH_SHORT).show()
                            }
                            .addOnFailureListener{
                                Log.d("Manage Room", "Fail to change room status")
                                Toast.makeText(context, "Fail to Add", Toast.LENGTH_SHORT).show()
                            }
                }else{
                    Toast.makeText(context, "Exist Room ID", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

    @SuppressLint("DefaultLocale")
    private fun filter(text: String){
        val filteredList = java.util.ArrayList<Room>()

        for (i in roomList) {
            if (i.roomNo?.toLowerCase()?.contains(text.toLowerCase()) == true) {
                filteredList.add(i)
            }
        }

        recyclerView.adapter = RoomsAdapter(filteredList, this)
    }


    fun Double.format(digits: Int) = "%.${digits}f".format(this)
}


