package com.example.mad_assignment.Staff.Staff_Fragments.staff_trans_history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.RecyclerView
import com.example.mad_assignment.Customer.Customer_Fragments.cust_trans_history.payment.model.Payment
import com.example.mad_assignment.R
import kotlinx.android.synthetic.main.cust_payment_trans_his_row.view.*

class staff_trans_his_record(private val context: FragmentActivity, private val payment: List<Payment>): RecyclerView.Adapter<staff_trans_his_record.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.cust_payment_trans_his_row, parent, false))
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val items = payment[position]
        holder.bind(items)

        holder.eachItem.setOnClickListener {
            val fragment: Fragment = staff_fragment_trans_his_details()
            val arguments = Bundle()
            arguments.putString("currentUserUID", items.uid)
            arguments.putString("currentReservedID", items.reserveID)
            fragment.arguments = arguments
            val ft: FragmentTransaction = context.supportFragmentManager.beginTransaction()
            ft.replace(R.id.nav_host_fragment_staff, fragment)
            ft.commit()
        }

    }


    inner class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView) {
        val eachItem: ConstraintLayout = itemView.findViewById(R.id.purchaserow)
        fun bind(viewHolder: Payment) {

            val price = "RM ${viewHolder.totalPayment}0"
            itemView.tv_purchase_date.text = viewHolder.paidDateTime
            itemView.tv_purchase_cost.text = price
            itemView.tv_purchase_status.text = viewHolder.status
            itemView.tv_purchase_text.text = "Purchase"

            val status = viewHolder.status
            val target = itemView.imgView_purchase_icon_trans_his
            if (status == "Success") {

                target.setImageResource(R.drawable.receiptsuccessful)
            } else {

                target.setImageResource(R.drawable.receiptfail)
            }




        }


    }


    override fun getItemCount() = payment.size
    fun Double.format(digits: Int) = "%.${digits}f".format(this)
}


