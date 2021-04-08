package com.example.mad_assignment.Customer.Customer_Fragments.cust_housekeeping.Adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.core.view.marginTop
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.mad_assignment.Customer.Customer_Fragments.cust_housekeeping.Class.RoomCleaningService
import com.example.mad_assignment.R


class AvailableRoomCleaningServicesAdadpter(private var availableRoomCleaningServicesList: ArrayList<RoomCleaningService>, private var mContext: FragmentActivity): RecyclerView.Adapter<AvailableRoomCleaningServicesAdadpter.AvailableHousekeepingServicesViewHolder>(){

    class AvailableHousekeepingServicesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val tvTime: TextView = itemView.findViewById(R.id.tv_services_time)
        val tvRemark: TextView = itemView.findViewById(R.id.tv_services_remark)
        val tvStatus: TextView = itemView.findViewById(R.id.tv_services_status)
        val viewServiceStatus: View = itemView.findViewById(R.id.view_services_status)
        val btnServiceBook: View = itemView.findViewById(R.id.btn_service_book)
    }

    override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
    ): AvailableHousekeepingServicesViewHolder {
        val itemView = LayoutInflater.from(mContext)
            .inflate(R.layout.cardview_item_available_housekeeping_services, parent, false)

        return AvailableHousekeepingServicesViewHolder(itemView)
    }

    @SuppressLint("SetTextI18n", "ResourceAsColor")
    override fun onBindViewHolder(holder: AvailableHousekeepingServicesViewHolder, position: Int) {
        val currentItem = availableRoomCleaningServicesList[position]

        holder.tvRemark.visibility = View.GONE

        val params: RelativeLayout.LayoutParams = RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT)
        params.addRule(RelativeLayout.END_OF, R.id.view_services_status);
        params.setMargins(0, 35, 0, 0)
        holder.tvTime.layoutParams = params

        holder.tvTime.text = "Time: " + currentItem.timeFrom + " - " + currentItem.timeTo
        holder.tvStatus.text = "Status: " + currentItem.status

        if(currentItem.status == "Not Available"){
            holder.viewServiceStatus.backgroundTintList = ContextCompat.getColorStateList(mContext, R.color.red);

            holder.btnServiceBook.alpha = 0.25f
            holder.btnServiceBook.isClickable = false
            holder.btnServiceBook.isEnabled = false

        }else{
            holder.viewServiceStatus.backgroundTintList = ContextCompat.getColorStateList(mContext, R.color.green);

            holder.btnServiceBook.alpha = 1.0f
            holder.btnServiceBook.isClickable = true
            holder.btnServiceBook.isEnabled = true
        }

        //Set onclicklisterner
        holder.btnServiceBook.setOnClickListener {
            Toast.makeText(mContext, "Hihi", Toast.LENGTH_SHORT).show()
        }
    }

    override fun getItemCount(): Int {
        return availableRoomCleaningServicesList.size
    }
}