package com.example.mad_assignment.Customer.Customer_Fragments.cust_housekeeping.Main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mad_assignment.Customer.Customer_Fragments.cust_housekeeping.Adapter.HousekeepingRequestedAdapter
import com.example.mad_assignment.Customer.Customer_Fragments.cust_housekeeping.Class.BookedHousekeepingService
import com.example.mad_assignment.Customer.Customer_Fragments.cust_housekeepingRequested.Model.CustHousekeepingRequestedModel
import com.example.mad_assignment.R

class CustHousekeepingRequestFragment : Fragment() {

    private lateinit var custHousekeepingRequestedModel: CustHousekeepingRequestedModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.customer_fragment_housekeeping_request, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView: RecyclerView = view.findViewById(R.id.rv_houseKeeping_requested)
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.adapter = HousekeepingRequestedAdapter(ArrayList<BookedHousekeepingService>(), requireActivity()) //Initialize adapter
        recyclerView.setHasFixedSize(true)

        //Get the viewmodel for housekeeping requested
        custHousekeepingRequestedModel = ViewModelProvider(this).get(CustHousekeepingRequestedModel::class.java)

        //Retrieve data from db
        custHousekeepingRequestedModel.retrieveHousekeepingRequestedFromDB("Room Cleaning")
        custHousekeepingRequestedModel.retrieveHousekeepingRequestedFromDB("Laundry Service")

        //Observe the housekeeping list and set it
        custHousekeepingRequestedModel.gethousekeepingRequestedList().observe(viewLifecycleOwner, Observer {
            recyclerView.adapter = HousekeepingRequestedAdapter(it, requireActivity())
        })

        custHousekeepingRequestedModel.getStatus().observe(viewLifecycleOwner, Observer {
            if (it == false) {
                Toast.makeText(requireContext(), "No Services Requested!", Toast.LENGTH_SHORT).show()
            }
        })
    }
}