package com.example.mad_assignment.Customer.Customer_Fragments.cust_trans_history.payment.views

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mad_assignment.Customer.Booking.Class.ReservationDetail
import com.example.mad_assignment.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_cust_payment_transaction_details_row.view.*


class cust_transaction_details_record(
        private var transList: ArrayList<ReservationDetail>,
        private var mContext: Context
): RecyclerView.Adapter<cust_transaction_details_record.TransDetailsViewHolder>() {

    inner class TransDetailsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        fun bindData(transList: ReservationDetail){

            itemView.tvRoomName.text = transList.roomType?.roomType.toString()
            val qty = "x ${transList.qty}"
            itemView.tvRoomQty.text = qty
            val priceText = "RM ${transList.subtotal?.format(2)}"
            itemView.tvusername.text = priceText
            Picasso.get().load(transList.roomType?.img).into(itemView.imgVTransStaffFrag)

        }


    }

    override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
    ): TransDetailsViewHolder {
        return TransDetailsViewHolder(LayoutInflater.from(mContext).inflate(R.layout.activity_cust_payment_transaction_details_row, parent, false))





    }

    //@SuppressLint("SetTextI18n", "SimpleDateFormat")
    override fun onBindViewHolder(holder: TransDetailsViewHolder, position: Int) {
        holder.bindData(transList[position])

    }


    override fun getItemCount(): Int {
        Log.d("CheckPosition", "I am in card recycler view")
        return transList.size

    }

    fun Double.format(digits: Int) = "%.${digits}f".format(this)

}