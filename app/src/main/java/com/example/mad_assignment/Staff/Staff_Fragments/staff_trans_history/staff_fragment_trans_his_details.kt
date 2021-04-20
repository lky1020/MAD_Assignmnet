package com.example.mad_assignment.Staff.Staff_Fragments.staff_trans_history

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mad_assignment.Customer.Booking.Class.Reservation
import com.example.mad_assignment.Customer.Booking.Class.ReservationDetail
import com.example.mad_assignment.R
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.fragment_staff_trans_his_details.*
import kotlinx.android.synthetic.main.fragment_staff_trans_his_record.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [staff_fragment_trans_his_details.newInstance] factory method to
 * create an instance of this fragment.
 */
class staff_fragment_trans_his_details : Fragment() {

    private var reservation_list = ArrayList<ReservationDetail>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        reservation_list.clear()
        val root = inflater.inflate(R.layout.fragment_staff_trans_his_details, container, false)

        val arguments = arguments
        val currentUID = arguments!!.getString("currentUserUID")
        val currentReservedID = arguments!!.getString("currentReservedID")


        val ref = FirebaseDatabase.getInstance().getReference("/Reservation/$currentUID/$currentReservedID")
        ref.addValueEventListener(object: ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {

                val reservation = snapshot.getValue(Reservation::class.java)

                val countItem = reservation?.reservationDetail?.size?.minus(1)


                for(i in 0.. countItem!!){
                    reservation_list.add(ReservationDetail(reservation.reservationDetail!![i].roomType,reservation.reservationDetail!![i].qty, reservation.reservationDetail!![i].subtotal) )
                }

                if(reservation_list.size > 0){
                    fragment_staff_trans_his_details_recycler_view.layoutManager = LinearLayoutManager(activity)
                    fragment_staff_trans_his_details_recycler_view.adapter = staff_fragment_trans_his_details_row(requireActivity(),reservation_list)
                }

            }

            override fun onCancelled(error: DatabaseError) {

            }

        })

        return root
    }


}