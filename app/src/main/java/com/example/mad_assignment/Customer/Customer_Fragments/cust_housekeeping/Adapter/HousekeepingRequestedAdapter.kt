package com.example.mad_assignment.Customer.Customer_Fragments.cust_housekeeping.Adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.mad_assignment.Customer.Customer_Fragments.cust_housekeeping.Class.BookedHousekeepingService
import com.example.mad_assignment.R
import com.google.android.material.imageview.ShapeableImageView
import com.squareup.picasso.Picasso
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class HousekeepingRequestedAdapter(private var housekeepingRequestedList: ArrayList<BookedHousekeepingService>, private var mContext: FragmentActivity): RecyclerView.Adapter<HousekeepingRequestedAdapter.HousekeepingRequestedViewHolder>() {

    class HousekeepingRequestedViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val ivService: ShapeableImageView = itemView.findViewById(R.id.iv_service_requested)
        val tvServiceTitle: TextView = itemView.findViewById(R.id.tv_service_title)
        val tvServiceDate: TextView = itemView.findViewById(R.id.tv_service_date)
        val tvServiceTime: TextView = itemView.findViewById(R.id.tv_service_time)
        val tvBookedTime: TextView = itemView.findViewById(R.id.tv_service_booked_time)
        val btnCancelRequest: ImageView = itemView.findViewById(R.id.btn_cancel_request)
    }

    override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
    ): HousekeepingRequestedViewHolder {
        val itemView = LayoutInflater.from(mContext)
                .inflate(R.layout.cardview_item_housekeeping_services_requested, parent, false)

        return HousekeepingRequestedViewHolder(itemView)
    }

    @SuppressLint("SetTextI18n", "SimpleDateFormat")
    override fun onBindViewHolder(holder: HousekeepingRequestedViewHolder, position: Int) {
        val currentItem = housekeepingRequestedList[position]

        // Process date
        val retrieveDate = currentItem.date
        val dayOfMonth = retrieveDate.substring(4, 6).toInt()
        val month = convertMonth(retrieveDate.substring(0, 3))
        val year =retrieveDate.substring(7).toInt()

        val simpleDateFormat = SimpleDateFormat("EEEE")
        val date = Date(year, month, dayOfMonth)
        val dayString = simpleDateFormat.format(date).substring(0, 3)

        Picasso.get().load(currentItem.serviceImg).into(holder.ivService);
        holder.tvServiceTitle.text = currentItem.serviceType
        holder.tvServiceDate.text = "Date: $dayString, $month $dayOfMonth "
        holder.tvServiceTime.text = "Time: " + currentItem.timeFrom + " - " + currentItem.timeTo
        holder.tvBookedTime.text = "Book at " + currentItem.bookedTime

        holder.btnCancelRequest.setOnClickListener {

        }


    }

    override fun getItemCount(): Int {
        return housekeepingRequestedList.size
    }

    private fun convertMonth(month: String): Int{
        when (month){
            "Jan" -> return 0
            "Feb" -> return 1
            "Mar" -> return 2
            "Apr" -> return 3
            "May"-> return 4
            "Jun" -> return 5
            "Jul" -> return 6
            "Aug" -> return 7
            "Sep" -> return 8
            "Oct" -> return 9
            "Nov" -> return 10
            "Dec" -> return 11
        }

        return 12
    }
}