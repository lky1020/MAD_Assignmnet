package com.example.mad_assignment.Customer.Customer_Fragments.cust_housekeeping.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.mad_assignment.Customer.Customer_Fragments.cust_housekeeping.Class.HousekeepingBottomSheetFragment
import com.example.mad_assignment.Customer.Customer_Fragments.cust_housekeeping.Class.HousekeepingItem
import com.example.mad_assignment.R
import com.squareup.picasso.Picasso

class HousekeepingItemAdapter(private var housekeepingItemList: ArrayList<HousekeepingItem>, private var mContext: FragmentActivity, private val servicesType: String): RecyclerView.Adapter<HousekeepingItemAdapter.HousekeepingItemViewHolder>() {

    /*
    The cardview will be same as the housekeeping mainpage cardview
     */

    class HousekeepingItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val cvHousekeepingItem: CardView = itemView.findViewById(R.id.cv_housekeeping)
        val tvTitle: TextView = itemView.findViewById(R.id.housekeeping_id)
        val ivHousekeepingItem: ImageView = itemView.findViewById(R.id.housekeeping_img)
    }

    override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
    ): HousekeepingItemViewHolder {
        val itemView = LayoutInflater.from(mContext).inflate(R.layout.cardview_item_housekeeping, parent, false)

        return HousekeepingItemViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: HousekeepingItemViewHolder, position: Int) {
        val currentItem = housekeepingItemList[position]
        val bottomSheetFragment = HousekeepingBottomSheetFragment(currentItem, servicesType, currentItem.img)

        holder.tvTitle.text = currentItem.title
        Picasso.get().load(currentItem.img).into(holder.ivHousekeepingItem)

        //Set onclicklisterner
        holder.cvHousekeepingItem.setOnClickListener {

            bottomSheetFragment.show(mContext.supportFragmentManager, "BottomSheetDialog")

        }

    }

    override fun getItemCount(): Int {
        return housekeepingItemList.size
    }
}