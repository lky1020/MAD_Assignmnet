package com.example.mad_assignment.Staff.Staff_Fragments.staff_trans_history

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.mad_assignment.Class.User
import com.example.mad_assignment.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.staff_fragment_trans_history_row.view.*

class staff_trans_his(private val context: FragmentActivity, private val user: List<User>): RecyclerView.Adapter<staff_trans_his.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        Log.d("position", "I am in staff_trans_his")
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.staff_fragment_trans_history_row, parent, false))
    }



    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        Log.d("position", "I am in staff_trans_his")
        val items = user[position]
        holder.bind(items)
    }



    inner class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView) {


        fun bind(viewHolder: User) {
            Log.d("position", "I am in staff_trans_his")
          itemView.tvusername.text = viewHolder.name
          Picasso.get().load(viewHolder.img).into(itemView.imgVTransStaffFrag)


        }


    }


    override fun getItemCount() = user.size

}


