package com.example.mad_assignment.Customer.Customer_Fragments.cust_trans_history.payment.views

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mad_assignment.Customer.Booking.Class.Reservation
import com.example.mad_assignment.Customer.Booking.Class.ReservationDetail
import com.example.mad_assignment.Customer.Booking.Main.BookRoomCart
import com.example.mad_assignment.R
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import kotlin.collections.ArrayList


class cust_transaction_details_record(
        private var roomMenuList: ArrayList<Reservation>,
        private var mContext: Context
): RecyclerView.Adapter<cust_transaction_details_record.BookRoomViewHolder>() {

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
        Log.d("CheckPosition", "I am in card recycler view")
        return BookRoomViewHolder(itemView)
    }

    @SuppressLint("SetTextI18n", "SimpleDateFormat")
    override fun onBindViewHolder(holder: BookRoomViewHolder, position: Int) {
        Log.d("CheckPosition", "I am in card recycler view")
    }


    override fun getItemCount(): Int {
        Log.d("CheckPosition", "I am in card recycler view")
        return roomMenuList.size

    }

    fun Double.format(digits: Int) = "%.${digits}f".format(this)

}