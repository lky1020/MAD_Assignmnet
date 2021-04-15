package com.example.mad_assignment.Customer.Customer_Fragments.cust_home

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.Constraints
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.RecyclerView
import com.example.mad_assignment.R
import com.example.mad_assignment.Staff.Staff_Fragments.staff_housekeeping.Main.Main.StaffHousekeepingMainFragment
import kotlinx.android.synthetic.main.customer_fragment_home_item.view.*

class CustHomeAdapter(private val context: FragmentActivity, private val item: List<Cust_Home_Item>)
    : RecyclerView.Adapter<CustHomeAdapter.ViewHolder>(){

    //involves inflating a layout from XML & returning the holder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.customer_fragment_home_item, parent, false))
    }

    inner class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView) {
        val book_now: TextView = itemView.findViewById(R.id.tv_book_now)
        val eachItem: LinearLayout = itemView.findViewById(R.id.cust_home_item)

        fun bind(items: Cust_Home_Item) {
            itemView.tvTitle_Room.text = items.title
            items.img?.let { itemView.iv_room_banner.setImageResource(it) }
            itemView.tv_des_home_item.text = items.des
            itemView.tv_book_now.text = items.link
        }

    }

    //involves populating data into the item through holder
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val items = item[position]
        holder.bind(items)

        holder.eachItem.setOnClickListener(){
            //proceed to Specific Pages
            if(holder.book_now.text.contains("Services")){
                //proceed to service page
                val ft: FragmentTransaction = context?.supportFragmentManager!!.beginTransaction()
                ft.replace(R.id.nav_host_fragment, StaffHousekeepingMainFragment())
                ft.commit()

            }else if(holder.book_now.text.contains("Faci")){
                //proceed to facility page


            }else{
                //proceed to room page

            }
        }
    }

    //return total items in the list
    override fun getItemCount() = item.size

}



