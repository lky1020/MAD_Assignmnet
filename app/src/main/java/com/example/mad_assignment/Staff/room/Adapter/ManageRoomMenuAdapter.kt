package com.example.mad_assignment.Staff.room.Adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mad_assignment.Customer.Booking.Class.RoomType
import com.example.mad_assignment.R
import com.example.mad_assignment.Staff.room.Main.ManageRoom
import com.google.gson.Gson
import com.squareup.picasso.Picasso

//This adapter is use to handle menu item in room activity
class ManageRoomMenuAdapter(
        private val roomMenuArrList: ArrayList<RoomType>,
        private val mContext: Context
):
    RecyclerView.Adapter<ManageRoomMenuAdapter.RecyclerViewHolder>() {
    // View Holder Class to handle Recycler View.
    inner class RecyclerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val roomType: TextView = itemView.findViewById(R.id.idRoomType)
        val roomImg: ImageView = itemView.findViewById(R.id.idRoomTypeIcon)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
        // Inflate Layout
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.manage_room_menu_item, parent, false)
        return RecyclerViewHolder(view)
    }
    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {

        val recyclerData = roomMenuArrList[position]

        // Set the room type title
        holder.roomType.text = recyclerData.roomType

        // Set the image
        Picasso.get().load(recyclerData.img).into(holder.roomImg)
        Picasso.get().isLoggingEnabled = true



        //Set on click listener (navigate to room activity)
        holder.itemView.setOnClickListener {

            //pass selected room to next activity
            val gson = Gson()
            val intent = Intent(mContext, ManageRoom::class.java)

            //pass room type data to next page
            intent.putExtra("RoomType", gson.toJson(recyclerData))
            mContext.startActivity(intent)

        }
    }

    override fun getItemCount(): Int {
        // this method returns the size of recyclerview
        return roomMenuArrList.size
    }

   /* fun setRoom(room: RoomType, roomNo: String){

        if(recyclerData.roomID.equals("SS")){
            setRoom(recyclerData, "SS01")
            setRoom(recyclerData, "SS02")
            setRoom(recyclerData, "SS03")
            setRoom(recyclerData, "SS04")
            setRoom(recyclerData, "SS05")
        }else if (recyclerData.roomID.equals("SD")){
            setRoom(recyclerData, "SD01")
            setRoom(recyclerData, "SD02")
            setRoom(recyclerData, "SD03")
            setRoom(recyclerData, "SD04")
            setRoom(recyclerData, "SD05")
        }else if (recyclerData.roomID.equals("DD")){
            setRoom(recyclerData, "DD01")
            setRoom(recyclerData, "DD02")
            setRoom(recyclerData, "DD03")
            setRoom(recyclerData, "DD04")
            setRoom(recyclerData, "DD05")
        }else if (recyclerData.roomID.equals("ST")){
            setRoom(recyclerData, "ST01")
            setRoom(recyclerData, "ST02")
            setRoom(recyclerData, "ST03")
            setRoom(recyclerData, "ST04")
            setRoom(recyclerData, "ST05")
        }else if (recyclerData.roomID.equals("SQ")){
            setRoom(recyclerData, "SQ01")
            setRoom(recyclerData, "SQ02")
            setRoom(recyclerData, "SQ03")
            setRoom(recyclerData, "SQ04")
            setRoom(recyclerData, "SQ05")
        }else if (recyclerData.roomID.equals("FR")){
            setRoom(recyclerData, "FR01")
            setRoom(recyclerData, "FR02")
            setRoom(recyclerData, "FR03")
            setRoom(recyclerData, "FR04")
            setRoom(recyclerData, "FR05")
        }else if (recyclerData.roomID.equals("DS")){
            setRoom(recyclerData, "DS01")
            setRoom(recyclerData, "DS02")
            setRoom(recyclerData, "DS03")
            setRoom(recyclerData, "DS04")
            setRoom(recyclerData, "DS05")
        }else{
            setRoom(recyclerData, "TR01")
            setRoom(recyclerData, "TR02")
            setRoom(recyclerData, "TR03")
            setRoom(recyclerData, "TR04")
            setRoom(recyclerData, "TR05")
        }


        val ref = FirebaseDatabase.getInstance().getReference("Rooms/${room.roomID}/$roomNo")

        ref.child("roomNo").setValue(roomNo)
        ref.child("roomType").setValue(room)
        ref.child("status").setValue("available")
    } */


}