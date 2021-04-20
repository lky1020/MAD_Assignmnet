package com.example.mad_assignment.Staff.Staff_Fragments.staff_trans_history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.RecyclerView
import com.example.mad_assignment.Class.User
import com.example.mad_assignment.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.staff_fragment_trans_history_row.view.*


class staff_trans_his(
    private val context: FragmentActivity,
    private val user: ArrayList<User>,
    private val view: View
): RecyclerView.Adapter<staff_trans_his.ViewHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.staff_fragment_trans_history_row,
                parent,
                false
            )
        )
    }



    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val items = user[position]
        holder.bind(items)

        holder.eachItem.setOnClickListener {
            val fragment: Fragment = staff_fragment_trans_his_record()
            val arguments = Bundle()
            arguments.putString("currentUserUID", items.uid)
            fragment.arguments = arguments
            val ft: FragmentTransaction = context.supportFragmentManager.beginTransaction()
            ft.replace(R.id.nav_host_fragment_staff, fragment)
            ft.commit()
        }

    }



    inner class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView) {

        val eachItem: ConstraintLayout = itemView.findViewById(R.id.staff_trans_history_recyclerView_row)
        fun bind(viewHolder: User) {
          itemView.tvusername.text = viewHolder.name
          Picasso.get().load(viewHolder.img).into(itemView.imgVTransStaffFrag)
        }


    }


    override fun getItemCount() = user.size

}


