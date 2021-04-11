package com.example.mad_assignment.Customer.Customer_Fragments.cust_housekeeping.Adapter

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
import com.example.mad_assignment.Customer.Customer_Fragments.cust_housekeeping.Class.HousekeepingOrderedItem
import com.example.mad_assignment.R
import com.google.android.material.imageview.ShapeableImageView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import kotlin.collections.ArrayList

class HousekeepingItemOrderedAdapter(private var housekeepingItemOrderedList: ArrayList<HousekeepingOrderedItem>, private var mContext: FragmentActivity): RecyclerView.Adapter<HousekeepingItemOrderedAdapter.HousekeepingItemOrderedViewHolder>() {

    class HousekeepingItemOrderedViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val ivItemOrdered: ShapeableImageView = itemView.findViewById(R.id.iv_item_ordered)
        val tvOrderedTitle: TextView = itemView.findViewById(R.id.tv_ordered_title)
        val tvOrderedQuantity: TextView = itemView.findViewById(R.id.tv_ordered_quantity)
        val tvOrderedEstimateTime: TextView = itemView.findViewById(R.id.tv_ordered_estimate_time)
        val btnCancelItemOrdered: ImageView = itemView.findViewById(R.id.btn_cancel_item_ordered)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): HousekeepingItemOrderedViewHolder {
        val itemView = LayoutInflater.from(mContext).inflate(R.layout.cardview_item_housekeeping_services_ordered, parent, false)

        return HousekeepingItemOrderedViewHolder(itemView)
    }

    @SuppressLint("SetTextI18n", "SimpleDateFormat")
    override fun onBindViewHolder(holder: HousekeepingItemOrderedViewHolder, position: Int) {
        val currentItem = housekeepingItemOrderedList[position]

        Picasso.get().load(currentItem.img).into(holder.ivItemOrdered);
        holder.tvOrderedTitle.text = currentItem.title
        holder.tvOrderedQuantity.text = "Quantity: " + currentItem.quantity.toString()
        holder.tvOrderedEstimateTime.text = "Estimate Receive Time: " + currentItem.receiveTime

        holder.btnCancelItemOrdered.setOnClickListener {
            deleteItemOrdered(currentItem, position)
        }
    }

    override fun getItemCount(): Int {
        return housekeepingItemOrderedList.size
    }

    private fun deleteItemOrdered(currentItem: HousekeepingOrderedItem, position: Int){
        val uid = FirebaseAuth.getInstance().uid
        val ref = FirebaseDatabase.getInstance().getReference("/User/$uid")

        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            @RequiresApi(Build.VERSION_CODES.O)
            override fun onDataChange(snapshot: DataSnapshot) {
                val currentUser = snapshot.getValue(User::class.java)!!

                // Order item for User
                val myRef = FirebaseDatabase.getInstance().getReference("Housekeeping").child(currentItem.serviceType).child("ItemOrdered").child(currentUser.name + " - " + currentItem.bookedTime)
                myRef.removeValue()

                housekeepingItemOrderedList.remove(currentItem);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, housekeepingItemOrderedList.size);

                Toast.makeText(mContext, "Item Ordered Been Removed", Toast.LENGTH_SHORT).show()
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }
}