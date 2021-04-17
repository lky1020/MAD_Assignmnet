package com.example.mad_assignment.Customer.Booking.Adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mad_assignment.Customer.Booking.Class.ReservationDetail
import com.example.mad_assignment.Customer.Booking.Class.RoomType
import com.example.mad_assignment.Customer.Booking.Main.BookRoomCart
import com.example.mad_assignment.R
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import java.util.*

class BookRoomAdapter(
        private var roomMenuList: ArrayList<RoomType>,
        private var mContext: Context
): RecyclerView.Adapter<BookRoomAdapter.BookRoomViewHolder>() {

    class BookRoomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val ivRoom: ImageView = itemView.findViewById(R.id.iv_bmr_item)
        val tvRoomType: TextView = itemView.findViewById(R.id.tv_brm_item_room_type)
        val tvRoomPrice: TextView = itemView.findViewById(R.id.tv_brm_item_price)
        val tvRoomBeds: TextView = itemView.findViewById(R.id.tv_brm_item_beds)
        val tvRoomOccupancy: TextView = itemView.findViewById(R.id.tv_brm_item_occupancy)
        val btn_bmr_book: Button = itemView.findViewById(R.id.btn_brm_item_book)
    }

    override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
    ): BookRoomViewHolder {
        val itemView = LayoutInflater.from(mContext).inflate(R.layout.book_room_menu_item, parent, false)

        return BookRoomViewHolder(itemView)
    }

    @SuppressLint("SetTextI18n", "SimpleDateFormat")
    override fun onBindViewHolder(holder: BookRoomViewHolder, position: Int) {
        val currentItem = roomMenuList[position]

        // Process date
        Picasso.get().load(currentItem.img).into(holder.ivRoom)
        Picasso.get().isLoggingEnabled = true

        holder.tvRoomType.text = currentItem.roomType
        holder.tvRoomPrice.text = "RM ${currentItem.price?.format(2)} / per night"
        holder.tvRoomBeds.text =  "${currentItem.beds} beds"
        holder.tvRoomOccupancy.text = "${currentItem.occupancy} guest"

        //Book button
        holder.btn_bmr_book.setOnClickListener {

            //pass selected room to next activity
            val gson = Gson()
            val intent = Intent(mContext, BookRoomCart::class.java)

            intent.putExtra("selectedRoom", gson.toJson(ReservationDetail(currentItem, 1, currentItem.price)))
            intent.putExtra("roomTypeList", gson.toJson(roomMenuList))

            mContext.startActivity(intent)
        }
    }


    override fun getItemCount(): Int {
        return roomMenuList.size
    }

    fun Double.format(digits: Int) = "%.${digits}f".format(this)

}