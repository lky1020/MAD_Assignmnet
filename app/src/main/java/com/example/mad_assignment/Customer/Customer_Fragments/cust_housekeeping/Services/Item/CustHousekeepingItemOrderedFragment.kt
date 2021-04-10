package com.example.mad_assignment.Customer.Customer_Fragments.cust_housekeeping.Services.Item

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
import com.example.mad_assignment.Customer.Customer_Fragments.cust_housekeeping.Adapter.HousekeepingItemOrderedAdapter
import com.example.mad_assignment.Customer.Customer_Fragments.cust_housekeeping.Adapter.HousekeepingRequestedAdapter
import com.example.mad_assignment.Customer.Customer_Fragments.cust_housekeeping.Class.BookedHousekeepingService
import com.example.mad_assignment.Customer.Customer_Fragments.cust_housekeeping.Class.HousekeepingOrderedItem
import com.example.mad_assignment.Customer.Customer_Fragments.cust_housekeeping.Model.CustHousekeepingItemOrderedModel
import com.example.mad_assignment.Customer.Customer_Fragments.cust_housekeepingRequested.Model.CustHousekeepingRequestedModel
import com.example.mad_assignment.R

class CustHousekeepingItemOrderedFragment : Fragment() {

    private lateinit var custHousekeepingItemOrderedModel: CustHousekeepingItemOrderedModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?)
    : View? {
        return inflater.inflate(R.layout.customer_fragment_ordered_housekeeping_item, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView: RecyclerView = view.findViewById(R.id.rv_houseKeeping_ordered)
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.adapter = HousekeepingItemOrderedAdapter(ArrayList<HousekeepingOrderedItem>(), requireActivity()) //Initialize adapter
        recyclerView.setHasFixedSize(true)

        //Get the viewmodel for housekeeping item ordered
        custHousekeepingItemOrderedModel = ViewModelProvider(this).get(CustHousekeepingItemOrderedModel::class.java)

        //Retrieve data from db
        custHousekeepingItemOrderedModel.retrieveHousekeepingItemOrderedFromDB("Bed Textiles")
        custHousekeepingItemOrderedModel.retrieveHousekeepingItemOrderedFromDB("Toiletries")

        //Observe the Item Ordered list and set it
        custHousekeepingItemOrderedModel.gethousekeepingItemOrderedList().observe(viewLifecycleOwner, Observer {
            recyclerView.adapter = HousekeepingItemOrderedAdapter(it, requireActivity())
        })

        custHousekeepingItemOrderedModel.getStatus().observe(viewLifecycleOwner, Observer {
            if (it == false) {
                Toast.makeText(requireContext(), "No Item Ordered!", Toast.LENGTH_SHORT).show()
            }
        })
    }
}