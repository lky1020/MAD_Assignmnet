package com.example.mad_assignment.Staff.Staff_Fragments.staff_housekeeping.Main.Adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.mad_assignment.Customer.Customer_Fragments.cust_housekeeping.Class.Housekeeping
import com.example.mad_assignment.Customer.Customer_Fragments.cust_housekeeping.Services.Item.CustHousekeepingAvailableItemActivity
import com.example.mad_assignment.Customer.Customer_Fragments.cust_housekeeping.Services.Service.CustHousekeepingAvailableServicesActivity
import com.example.mad_assignment.R
import com.example.mad_assignment.Staff.Staff_Fragments.staff_housekeeping.Main.Main.StaffHousekeepingServiceActivity
import com.squareup.picasso.Picasso

class StaffHousekeepingServicesAdapter(private var housekeepingList: ArrayList<Housekeeping>, private var mContext: FragmentActivity): RecyclerView.Adapter<StaffHousekeepingServicesAdapter.HousekeepingViewHolder>() {

    class HousekeepingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val cvHousekeeping: CardView = itemView.findViewById(R.id.cv_housekeeping)
        val tvTitle: TextView = itemView.findViewById(R.id.housekeeping_id)
        val ivHousekeeping: ImageView = itemView.findViewById(R.id.housekeeping_img)
    }

    override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
    ): HousekeepingViewHolder {
        val itemView = LayoutInflater.from(mContext)
            .inflate(R.layout.cardview_item_housekeeping, parent, false)

        return HousekeepingViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: HousekeepingViewHolder, position: Int) {
        val currentItem = housekeepingList[position]

        holder.tvTitle.text = currentItem.title
        Picasso.get().load(currentItem.img).into(holder.ivHousekeeping);

        //Set onclicklisterner
        holder.cvHousekeeping.setOnClickListener {
            var intent = Intent()

            intent = Intent(mContext, StaffHousekeepingServiceActivity::class.java).apply {
                putExtra("Title", currentItem.title)
            }

            mContext.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return housekeepingList.size
    }
}