package com.example.mad_assignment.Customer_Fragments.cust_housekeeping

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mad_assignment.Customer.Customer_Fragments.cust_housekeeping.HousekeepingAdapter
import com.example.mad_assignment.R

//belongs to customer_fragment_housekeeping.xml
class CustHousekeepingFragment : Fragment() {

    private lateinit var custHousekeepingModel: CustHousekeepingModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        val root = inflater.inflate(R.layout.customer_fragment_housekeeping, container, false)
        val recyclerView: RecyclerView = root.findViewById(R.id.rv_housekeeping)

        //Get the viewmodel for housekeeping
        custHousekeepingModel = ViewModelProvider(this).get(CustHousekeepingModel::class.java)

        //Observe the housekeeping list and set it
        custHousekeepingModel.getHousekeepingList().observe(viewLifecycleOwner, Observer{
            recyclerView.adapter = HousekeepingAdapter(it)
        })

        recyclerView.layoutManager = GridLayoutManager(this.context, 2)
        recyclerView.setHasFixedSize(true)

        return root
    }
}