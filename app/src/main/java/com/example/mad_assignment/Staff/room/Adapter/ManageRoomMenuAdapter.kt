package com.example.mad_assignment.Staff.room.Adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mad_assignment.Staff.room.Class.RoomMenu
import com.example.mad_assignment.R
import com.example.mad_assignment.Staff.room.ManageRoom
import java.util.*

//This adapter is use to handle menu item in room activity
class ManageRoomMenuAdapter(
    private val roomMenuArrList: ArrayList<RoomMenu>,
    private val mcontext: Context
):
    RecyclerView.Adapter<ManageRoomMenuAdapter.RecyclerViewHolder>() {
    // View Holder Class to handle Recycler View.
    inner class RecyclerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val roomType: TextView
        val roomImg: ImageView

        init {
            roomType = itemView.findViewById(R.id.idRoomType)
            roomImg = itemView.findViewById(R.id.idRoomTypeIcon)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
        // Inflate Layout
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.manage_room_menu_item, parent, false)
        return RecyclerViewHolder(view)
    }
    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {
        // Set the data to textview and imageview.
        val recyclerData = roomMenuArrList[position]
        holder.roomType.text = recyclerData.name
        holder.roomImg.setImageResource(recyclerData.img)



        //Set on click listener (navigate to room activity)
        holder.itemView.setOnClickListener {
            val intent = Intent(mcontext, ManageRoom::class.java)

            //pass room type data to next page
            intent.putExtra("RoomType", holder.roomType.text)

            mcontext.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        // this method returns the size of recyclerview
        return roomMenuArrList.size
    }


}