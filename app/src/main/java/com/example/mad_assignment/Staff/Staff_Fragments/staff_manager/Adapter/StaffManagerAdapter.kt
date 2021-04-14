package com.example.mad_assignment.Staff.Staff_Fragments.staff_manager.Adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.mad_assignment.Class.Staff
import com.example.mad_assignment.R
import com.example.mad_assignment.Staff.Staff_Fragments.staff_housekeeping.Main.Main.StaffHousekeepingServiceActivity
import com.example.mad_assignment.Staff.Staff_Fragments.staff_manager.Main.Permission.StaffPermissionActivity
import com.google.android.material.imageview.ShapeableImageView
import com.squareup.picasso.Picasso

class StaffManagerAdapter(private var staffList: ArrayList<Staff>, private var mContext: FragmentActivity): RecyclerView.Adapter<StaffManagerAdapter.StaffManagerViewHolder>() {

    class StaffManagerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val ivStaffImg: ShapeableImageView = itemView.findViewById(R.id.iv_staff_img)
        val tvName: TextView = itemView.findViewById(R.id.tv_staff_name)
        val tvID: TextView = itemView.findViewById(R.id.tv_staff_id)
        val btnManage: Button = itemView.findViewById(R.id.btn_manage_staff)
    }

    override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
    ): StaffManagerViewHolder {
        val itemView = LayoutInflater.from(mContext)
                .inflate(R.layout.cardview_staff_manager, parent, false)

        return StaffManagerViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: StaffManagerViewHolder, position: Int) {
        val currentItem = staffList[position]

        holder.tvName.text = currentItem.name
        holder.tvID.text = currentItem.id
        Picasso.get().load(currentItem.img).into(holder.ivStaffImg);

        //Set onclicklisterner
        holder.btnManage.setOnClickListener {
            var intent = Intent()

            intent = Intent(mContext, StaffPermissionActivity::class.java).apply {
                putExtra("Staff", currentItem)
            }

            mContext.startActivity(intent)
        }

    }

    override fun getItemCount(): Int {
        return staffList.size
    }
}