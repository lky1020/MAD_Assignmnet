package com.example.mad_assignment.Customer.Customer_Fragments.cust_housekeeping.Services.Item

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mad_assignment.Customer.Customer_Fragments.cust_housekeeping.Adapter.HousekeepingItemAdapter
import com.example.mad_assignment.Customer.Customer_Fragments.cust_housekeeping.Class.HousekeepingItem
import com.example.mad_assignment.Customer.Customer_Fragments.cust_housekeeping.Model.CustHousekeepingItemModel
import com.example.mad_assignment.CustomerMain
import com.example.mad_assignment.R

class CustHousekeepingItemAvailableFragment : Fragment() {

    private lateinit var custHousekeepingItemModel: CustHousekeepingItemModel
    private lateinit var servicesType: String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?)
    : View? {
        val root: View = inflater.inflate(R.layout.customer_fragment_housekeeping_item_available, container, false)

        //Get action bar title for retrieve data from db
        servicesType = (activity as CustHousekeepingAvailableItemActivity?)!!.supportActionBar!!.title.toString()

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView: RecyclerView = view.findViewById(R.id.rv_housekeeping_item)
        recyclerView.layoutManager = GridLayoutManager(activity, 2)
        recyclerView.adapter = HousekeepingItemAdapter(ArrayList<HousekeepingItem>(), requireActivity()) //Initialize adapter
        recyclerView.setHasFixedSize(true)

        //Get the viewmodel for housekeeping
        custHousekeepingItemModel = ViewModelProvider(this).get(CustHousekeepingItemModel::class.java)

        //Retrieve data from db
        custHousekeepingItemModel.retrieveHousekeepingItemFromDB(servicesType)

        //Observe the housekeeping list and set it
        custHousekeepingItemModel.getHousekeepingItemList().observe(viewLifecycleOwner, Observer {
            recyclerView.adapter = HousekeepingItemAdapter(it, requireActivity())
        })
    }
}