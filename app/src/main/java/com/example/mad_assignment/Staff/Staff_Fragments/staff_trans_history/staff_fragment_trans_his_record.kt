package com.example.mad_assignment.Staff.Staff_Fragments.staff_trans_history

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mad_assignment.Customer.Customer_Fragments.cust_trans_history.payment.model.Payment
import com.example.mad_assignment.R
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.customer_fragment_trans_history.*
import kotlinx.android.synthetic.main.fragment_staff_trans_his_record.*
import kotlinx.android.synthetic.main.staff_fragment_trans_history.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [staff_fragment_trans_his_record.newInstance] factory method to
 * create an instance of this fragment.
 */
class staff_fragment_trans_his_record : Fragment() {

    private var payment_List = ArrayList<Payment>()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        payment_List.clear()
        val root = inflater.inflate(R.layout.fragment_staff_trans_his_record, container, false)

        val arguments = arguments
        val currentUID = arguments!!.getString("currentUserUID")


        val ref = FirebaseDatabase.getInstance().getReference("/Payment/$currentUID")
        ref.addValueEventListener(object: ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.children.forEach {

                    val payment = it.getValue(Payment::class.java)
                    if(payment != null)
                        payment_List.add(Payment(payment.invoiceID,payment.name,payment.paidDateTime,payment.totalPayment, payment.paymentMethod, payment.status, payment.uid, payment.reserveID))
                }

                if(payment_List.size > 0){
                    fragment_staff_trans_his_record_recycler_view.layoutManager = LinearLayoutManager(activity)
                    fragment_staff_trans_his_record_recycler_view.adapter = staff_trans_his_record(requireActivity(),payment_List)
                    fragment_staff_trans_his_record_recycler_view.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
                }

            }

            override fun onCancelled(error: DatabaseError) {

            }

        })



        return root
    }
}