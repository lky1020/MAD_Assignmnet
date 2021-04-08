package com.example.mad_assignment.Customer.Customer_Fragments.cust_housekeeping.Services.Item

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentTransaction
import com.example.mad_assignment.R

class CustHousekeepingAvailableItemFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val root: View = inflater.inflate(R.layout.customer_fragment_available_housekeeping_item, container,false)

        // Initialize the fragment
        val ft: FragmentTransaction = activity?.supportFragmentManager!!.beginTransaction()
        ft.replace(R.id.framel_Housekeeping_Item, CustHousekeepingItemAvailableFragment())
        ft.commit()

        return root
    }
}