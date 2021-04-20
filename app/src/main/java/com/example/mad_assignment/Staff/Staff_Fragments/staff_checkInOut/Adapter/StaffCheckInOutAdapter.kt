package com.example.mad_assignment.Staff.Staff_Fragments.staff_checkInOut.Adapter

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.mad_assignment.Customer.Booking.Class.Reservation
import com.example.mad_assignment.R
import com.example.mad_assignment.Staff.Staff_Fragments.staff_checkInOut.Main.StaffManageCheckInOutActivity
import com.google.android.material.imageview.ShapeableImageView
import com.squareup.picasso.Picasso
import java.util.*

class StaffCheckInOutAdapter(private var checkInOutList: ArrayList<Reservation>, private var mContext: FragmentActivity): RecyclerView.Adapter<StaffCheckInOutAdapter.StaffCheckInOutViewHolder>() {

    class StaffCheckInOutViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        val ivCustImg: ShapeableImageView = itemView.findViewById(R.id.iv_customer_img)
        val tvCustName: TextView = itemView.findViewById(R.id.tv_customer_name)
        val tvGuest: TextView = itemView.findViewById(R.id.tv_guest)
        val tvCheckDate: TextView = itemView.findViewById(R.id.tv_check_date)
        val tvReservedDate: TextView = itemView.findViewById(R.id.tv_reservedDate)
        val btnCheckInOut: Button = itemView.findViewById(R.id.btn_check_in_out)
    }

    override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
    ): StaffCheckInOutViewHolder {
        val itemView = LayoutInflater.from(mContext).inflate(R.layout.cardview_staff_check_in_out, parent, false)

        return StaffCheckInOutViewHolder(itemView)
    }

    @SuppressLint("SetTextI18n", "SimpleDateFormat")
    override fun onBindViewHolder(holder: StaffCheckInOutViewHolder, position: Int) {
        val currentItem = checkInOutList[position]

        Picasso.get().load(currentItem.custImg).into(holder.ivCustImg)
        holder.tvCustName.text = currentItem.custName
        holder.tvGuest.text = "Guest: " + currentItem.guest

        if(currentItem.status == "paid"){
            holder.tvCheckDate.text = currentItem.checkInDate

        }else if(currentItem.status == "check in"){
            holder.tvCheckDate.text = currentItem.checkOutDate

        }

        holder.tvReservedDate.text = "Reserved at " + currentItem.dateReserved

        if(currentItem.status == "paid"){
            holder.btnCheckInOut.text = "Check In"

        }else if(currentItem.status == "check in"){
            holder.btnCheckInOut.text = "Check Out"
        }

        //Set onclicklisterner
        holder.btnCheckInOut.setOnClickListener {

            var intent = Intent()

            intent = Intent(mContext, StaffManageCheckInOutActivity::class.java).apply {
                putExtra("Reservation", currentItem)
                putExtra("type", holder.btnCheckInOut.text)
            }

            mContext.startActivity(intent)

        }
    }

    override fun getItemCount(): Int {
        return checkInOutList.size
    }

}