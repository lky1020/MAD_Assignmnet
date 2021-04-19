package com.example.mad_assignment.Customer.Customer_Fragments.cust_facilities.Adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mad_assignment.Customer.Customer_Fragments.cust_facilities.Main.CustFacilityDetails
import com.example.mad_assignment.R
import com.example.mad_assignment.Staff.facility.Class.Facility
import com.google.gson.Gson
import com.squareup.picasso.Picasso

class CustFacilitiesAdapter (
        private val facilityList: ArrayList<Facility>,
        private val mContext: Context
):
RecyclerView.Adapter<CustFacilitiesAdapter.RecyclerViewHolder>() {
    // View Holder Class to handle Recycler View.
    inner class RecyclerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val facilityName: TextView = itemView.findViewById(R.id.tv_facility_name)
        val facilitImg: ImageView = itemView.findViewById(R.id.iv_facility_img)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
        // Inflate Layout
        val view: View = LayoutInflater.from(parent.context)
                .inflate(R.layout.manage_facility_menu_item, parent, false)
        return RecyclerViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {

        val recyclerData = facilityList[position]

        // Set the facility title
        holder.facilityName.text = recyclerData.facilityName

        // Set the image
        Picasso.get().load(recyclerData.img).into(holder.facilitImg)
        Picasso.get().isLoggingEnabled = true

        //Set on click listener (navigate to facility details activity)
        holder.itemView.setOnClickListener {

            //pass selected facility to next activity
            val gson = Gson()
            val intent = Intent(mContext, CustFacilityDetails::class.java)
            intent.putExtra("facility", gson.toJson(recyclerData))
            mContext.startActivity(intent)

        }
    }

    override fun getItemCount(): Int {
        // this method returns the size of recyclerview
        return facilityList.size
    }
}