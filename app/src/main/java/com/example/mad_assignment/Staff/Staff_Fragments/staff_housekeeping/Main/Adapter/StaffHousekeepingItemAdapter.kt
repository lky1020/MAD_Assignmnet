package com.example.mad_assignment.Staff.Staff_Fragments.staff_housekeeping.Main.Adapter

import android.annotation.SuppressLint
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.mad_assignment.Class.User
import com.example.mad_assignment.Customer.Customer_Fragments.cust_housekeeping.Adapter.HousekeepingItemOrderedAdapter
import com.example.mad_assignment.Customer.Customer_Fragments.cust_housekeeping.Class.HousekeepingItem
import com.example.mad_assignment.Customer.Customer_Fragments.cust_housekeeping.Class.HousekeepingOrderedItem
import com.example.mad_assignment.R
import com.google.android.material.imageview.ShapeableImageView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import kotlin.collections.ArrayList

class StaffHousekeepingItemAdapter(private var housekeepingItemOrderedList: ArrayList<HousekeepingItem>, private var mContext: FragmentActivity): RecyclerView.Adapter<StaffHousekeepingItemAdapter.StaffHousekeepingItemViewHolder>() {

    class StaffHousekeepingItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val ivItemOrdered: ShapeableImageView = itemView.findViewById(R.id.iv_item_ordered)
        val tvOrderedTitle: TextView = itemView.findViewById(R.id.tv_ordered_title)
        val tvOrderedQuantity: TextView = itemView.findViewById(R.id.tv_ordered_quantity)
        val btnItemEdit: ImageView = itemView.findViewById(R.id.btn_item_edit)
        val btnItemDelete: ImageView = itemView.findViewById(R.id.btn_item_delete)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): StaffHousekeepingItemViewHolder {
        val itemView = LayoutInflater.from(mContext).inflate(R.layout.cardview_staff_housekeeping_item, parent, false)

        return StaffHousekeepingItemViewHolder(itemView)
    }

    @SuppressLint("SetTextI18n", "SimpleDateFormat")
    override fun onBindViewHolder(holder: StaffHousekeepingItemViewHolder, position: Int) {
        val currentItem = housekeepingItemOrderedList[position]

        Picasso.get().load(currentItem.img).into(holder.ivItemOrdered);
        holder.tvOrderedTitle.text = currentItem.title
        holder.tvOrderedQuantity.text = "Quantity: " + currentItem.quantity.toString()

        holder.btnItemEdit.setOnClickListener {
            //TODO
        }

        holder.btnItemDelete.setOnClickListener {
            //TODO
        }
    }

    override fun getItemCount(): Int {
        return housekeepingItemOrderedList.size
    }
}