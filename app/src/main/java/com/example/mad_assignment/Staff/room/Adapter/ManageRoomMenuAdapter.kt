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
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            mContext.startActivity(intent)

        }
    }

    override fun getItemCount(): Int {
        // this method returns the size of recyclerview
        return roomMenuArrList.size
    }

}