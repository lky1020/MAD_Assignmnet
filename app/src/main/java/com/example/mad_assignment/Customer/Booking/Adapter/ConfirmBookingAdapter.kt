package com.example.mad_assignment.Customer.Booking.Adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mad_assignment.Customer.Booking.Class.Reservation
import com.example.mad_assignment.Customer.Booking.Class.ReservationDetail
import com.example.mad_assignment.R
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.*


class ConfirmBookingAdapter(
    private var reservationDetailList: ArrayList<ReservationDetail>,
    private var mContext: Context
): RecyclerView.Adapter<ConfirmBookingAdapter.ReservationDetailViewHolder>() {

    class ReservationDetailViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val description: TextView = itemView.findViewById(R.id.cb_item_description)
        val price: TextView = itemView.findViewById(R.id.cb_item_subtotal)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ReservationDetailViewHolder {
        val itemView = LayoutInflater.from(mContext).inflate(
            R.layout.confirm_booking_item,
            parent,
            false
        )

        return ReservationDetailViewHolder(itemView)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ReservationDetailViewHolder, position: Int) {
        val currentItem = reservationDetailList[position]

        holder.description.text = "${currentItem.qty}x ${currentItem.roomType?.roomType}"
        holder.price.text = "RM ${currentItem.subtotal?.format(2)}"

    }

    override fun getItemCount(): Int {
        return reservationDetailList.size
    }


    fun Double.format(digits: Int) = "%.${digits}f".format(this)

    /*   var database = FirebaseDatabase.getInstance().getReference("Room")
       val dd:String = "https://firebasestorage.googleapis.com/v0/b/quadcorehms-5b4ed.appspot.com/o/room%2Fdeluxe%20double%20room%2FDD.png?alt=media&token=e21081dc-f6b0-4fd1-87d3-9b94133fa278"
       val ss:String = "https://firebasestorage.googleapis.com/v0/b/quadcorehms-5b4ed.appspot.com/o/room%2Fstandard%20single%20room%2Froom3.jpg?alt=media&token=41ce0122-3ade-4daf-b2e7-bd8cd3725576"
       val sd:String = "https://firebasestorage.googleapis.com/v0/b/quadcorehms-5b4ed.appspot.com/o/room%2Fstandard%20double%20room%2FSD.jfif?alt=media&token=bad505ca-5700-4ca2-aff9-e5387d4fb739"
       val st:String = "https://firebasestorage.googleapis.com/v0/b/quadcorehms-5b4ed.appspot.com/o/room%2Fstandard%20twin%20room%2FST.jpg?alt=media&token=0c0ec80b-e63d-4b47-9dcd-06d045a5270e"
       val sq:String = "https://firebasestorage.googleapis.com/v0/b/quadcorehms-5b4ed.appspot.com/o/room%2Fsuperior%20queen%20room%2FSQ.jpg?alt=media&token=712e2dda-2862-4249-ac2c-5418d7400b1f"
       val fr:String = "https://firebasestorage.googleapis.com/v0/b/quadcorehms-5b4ed.appspot.com/o/room%2Ffamily%20room%2FFR.jpg?alt=media&token=aedcf822-9787-428b-9c37-b22a8c08e8c3"
       val ds:String = "https://firebasestorage.googleapis.com/v0/b/quadcorehms-5b4ed.appspot.com/o/room%2Fdeluxe%20studio%2FDS.jpg?alt=media&token=e3e412d8-48f5-484b-9ef6-e9ed09bed2b5"
       val tr:String = "https://firebasestorage.googleapis.com/v0/b/quadcorehms-5b4ed.appspot.com/o/room%2Ftriple%20room%2FTR.jpg?alt=media&token=9b8b40f5-befa-4064-be8f-c933358f8a78"
       // val beds:String = "1 single bed, 1 large bed"

       database.child("DD").child("img").setValue(dd)
       database.child("SS").child("img").setValue(ss)
       database.child("SD").child("img").setValue(sd)
       database.child("ST").child("img").setValue(st)
       database.child("SQ").child("img").setValue(sq)
       database.child("FR").child("img").setValue(fr)
       database.child("DS").child("img").setValue(ds)
       database.child("TR").child("img").setValue(tr)*/
}