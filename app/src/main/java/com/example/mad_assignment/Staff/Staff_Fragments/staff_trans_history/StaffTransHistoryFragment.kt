package com.example.mad_assignment.Staff_Fragments.staff_trans_history

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mad_assignment.Class.User
import com.example.mad_assignment.Customer.Booking.Class.Reservation
import com.example.mad_assignment.Customer.Customer_Fragments.cust_trans_history.payment.cust_payment_transaction_details
import com.example.mad_assignment.Customer.Customer_Fragments.cust_trans_history.payment.model.Payment
import com.example.mad_assignment.Customer.Customer_Fragments.cust_trans_history.payment.views.cust_transaction_history
import com.example.mad_assignment.R
import com.example.mad_assignment.Staff.Staff_Fragments.staff_checkInOut.Adapter.StaffCheckInOutAdapter
import com.example.mad_assignment.Staff.Staff_Fragments.staff_trans_history.staff_trans_his
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.customer_fragment_trans_history.*
import kotlinx.android.synthetic.main.staff_fragment_trans_history.*
import kotlinx.android.synthetic.main.staff_fragment_trans_history_row.view.*

//belongs to staff_fragment_trans_history.xml
class StaffTransHistoryFragment : Fragment() {

    var currentUser: User? = null
    private var user_List = ArrayList<User>()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        user_List.clear()
        val root = inflater.inflate(R.layout.staff_fragment_trans_history, container, false)

        val uid = FirebaseAuth.getInstance().uid
        val refCurrentUser = FirebaseDatabase.getInstance().getReference("/User/$uid")
        refCurrentUser.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                currentUser = snapshot.getValue(User::class.java)

            }

            override fun onCancelled(error: DatabaseError) {

            }

        })



        val ref = FirebaseDatabase.getInstance().getReference("/User")
        ref.addValueEventListener(object: ValueEventListener {


            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.children.forEach {

                    val user = it.getValue(User::class.java)
                    if(user != null && user.role == "Member"){
                        user_List.add(User(user.name,user.uid, user.password, user.phoneNum, user.email,user.role, user.img))

                    }
                    staff_fragment_recycler_view.layoutManager = LinearLayoutManager(activity)
                    staff_fragment_recycler_view.adapter = staff_trans_his(requireActivity(),user_List,root) //Initialize adapter
                    staff_fragment_recycler_view.setHasFixedSize(true)
                }


            }

            override fun onCancelled(error: DatabaseError) {

            }


        })
        return root
    }

}