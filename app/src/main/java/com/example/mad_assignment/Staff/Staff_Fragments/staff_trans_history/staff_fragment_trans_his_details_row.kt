package com.example.mad_assignment.Staff.Staff_Fragments.staff_trans_history

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.mad_assignment.Customer.Booking.Class.ReservationDetail
import com.example.mad_assignment.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_staff_trans_his_details_row.view.*

class staff_fragment_trans_his_details_row (private val context: FragmentActivity, private val reservation: List<ReservationDetail>): RecyclerView.Adapter<staff_fragment_trans_his_details_row.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.fragment_staff_trans_his_details_row, parent, false))
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val items = reservation[position]
        holder.bind(items)

    }


    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        fun bind(viewHolder: ReservationDetail) {

            // Set the image
            Picasso.get().load(viewHolder.roomType!!.img).into(itemView.imgVTransStaffFrag)
            Picasso.get().isLoggingEnabled = true

            itemView.tvRoomNameStaff.text = viewHolder.roomType!!.roomType
            itemView.tvRoomQtyStaff.text = viewHolder.qty.toString()
            val price = viewHolder.subtotal?.format(2)
            itemView.tvNameStaff.text = "RM $price"




        }


    }


    override fun getItemCount() = reservation.size

}
fun Double.format(digits: Int) = "%.${digits}f".format(this)
