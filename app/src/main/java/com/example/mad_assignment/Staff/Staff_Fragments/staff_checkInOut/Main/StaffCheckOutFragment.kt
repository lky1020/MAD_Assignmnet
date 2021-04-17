package com.example.mad_assignment.Staff.Staff_Fragments.staff_checkInOut.Main

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mad_assignment.Customer.Booking.Class.Reservation
import com.example.mad_assignment.R
import com.example.mad_assignment.Staff.Staff_Fragments.staff_checkInOut.Adapter.StaffCheckInOutAdapter
import com.example.mad_assignment.Staff.Staff_Fragments.staff_checkInOut.Model.StaffCheckInOutModel

class StaffCheckOutFragment : Fragment() {

    private lateinit var staffCheckInOutModel: StaffCheckInOutModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var parentLayout: ViewGroup
    private lateinit var parentElement: FrameLayout

    // Search
    private lateinit var etSearch: EditText
    private lateinit var checkInOutList: ArrayList<Reservation>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        parentLayout = container?.parent as ViewGroup
        parentElement = parentLayout.parent as FrameLayout

        return inflater.inflate(R.layout.staff_fragment_check_out, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        etSearch = parentElement.findViewById(R.id.et_check_in_out_search)

        etSearch.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {
                if(!etSearch.text.equals("")){
                    filter(s.toString())
                }
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

            }
        })

        recyclerView = view.findViewById(R.id.rv_staff_check_out)
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.adapter = StaffCheckInOutAdapter(ArrayList<Reservation>(), requireActivity()) //Initialize adapter
        recyclerView.setHasFixedSize(true)

        //Get the viewmodel for housekeeping requested
        staffCheckInOutModel = ViewModelProvider(this).get(StaffCheckInOutModel::class.java)

        //Retrieve data from db
        staffCheckInOutModel.retrieveCheckInOutFromDB("check in")

        //Observe the housekeeping list and set it
        staffCheckInOutModel.getCheckInOutList().observe(viewLifecycleOwner, Observer {
            recyclerView.adapter = StaffCheckInOutAdapter(it, requireActivity())
            checkInOutList = it
        })

        staffCheckInOutModel.getStatus().observe(viewLifecycleOwner, Observer {
            if (it == false) {
                Toast.makeText(requireContext(), "No Record Available!", Toast.LENGTH_SHORT).show()
            }
        })
    }

    @SuppressLint("DefaultLocale")
    private fun filter(text: String){
        if(activity != null) {
            val filteredList = ArrayList<Reservation>()

            for (i in checkInOutList) {
                if (i.custName!!.toLowerCase().contains(text.toLowerCase())) {
                    filteredList.add(i)
                }
            }

            recyclerView.adapter = StaffCheckInOutAdapter(filteredList, requireActivity())
        }
    }

}