package com.example.mad_assignment.Customer.Customer_Fragments.cust_trans_history.payment.views


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.mad_assignment.Customer.Customer_Fragments.cust_trans_history.payment.model.Payment
import com.example.mad_assignment.R
import kotlinx.android.synthetic.main.cust_payment_trans_his_row.view.*

class cust_transaction_history(private val context: FragmentActivity, private val payment: List<Payment>): RecyclerView.Adapter<cust_transaction_history.ViewHolder>() {

    var paymentInfo: Payment? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.cust_payment_trans_his_row, parent, false))
    }




    override fun onBindViewHolder(holder: cust_transaction_history.ViewHolder, position: Int) {
        val items = payment[position]
        holder.bind(items)

    }



    inner class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView) {

        fun bind(viewHolder: Payment) {

            itemView.tv_purchase_date.text = viewHolder.paidDateTime
            itemView.tv_purchase_cost.text = viewHolder.totalPayment
            itemView.tv_purchase_status.text = viewHolder.status
            itemView.tv_purchase_text.text = "Purchase"

            val status = viewHolder.status.toString()
            val target = itemView.imgView_purchase_icon_trans_his
            if (status == "success") {

                target.setImageResource(R.drawable.receiptsuccessful)
            } else {

                target.setImageResource(R.drawable.receiptfail)
            }




        }

//                override fun onCancelled(error: DatabaseError) {
//
//                }

//            })

        }


    override fun getItemCount() = payment.size
    }
//}