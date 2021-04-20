package com.example.mad_assignment.Customer_Fragments.cust_trans_history

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import com.example.mad_assignment.Customer.Customer_Fragments.cust_trans_history.payment.model.Payment
import com.example.mad_assignment.Customer.Customer_Fragments.cust_trans_history.payment.views.cust_transaction_history
import com.example.mad_assignment.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.customer_fragment_trans_history.*


//belongs to customer_fragment_services.xml
class CustTransHistoryFragment : Fragment() {

    private var payment_list = ArrayList<Payment>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.customer_fragment_trans_history, container, false)

        val uid = FirebaseAuth.getInstance().uid
        val ref = FirebaseDatabase.getInstance().getReference("/Payment/$uid")
        ref.addValueEventListener(object: ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.children.forEach {

                    val payment = it.getValue(Payment::class.java)
                    if(payment != null)
                        payment_list.add(Payment(payment.invoiceID,payment.name,payment.paidDateTime,payment.totalPayment, payment.paymentMethod, payment.status))
                }

                recyclerview_trans_history.adapter = cust_transaction_history(requireActivity(),payment_list)
                recyclerview_trans_history.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
        return root
    }
}
