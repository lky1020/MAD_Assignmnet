package com.example.mad_assignment.Customer_Fragments.cust_facilities

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.mad_assignment.R

//belongs to customer_fragment_facilities.xml
class CustFacilitiesFragment : Fragment() {

    private lateinit var custFacilitiesViewModel: CustFacilitiesViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        custFacilitiesViewModel =
                ViewModelProvider(this).get(CustFacilitiesViewModel::class.java)
        val root = inflater.inflate(R.layout.customer_fragment_facilities, container, false)
        val textView: TextView = root.findViewById(R.id.text_services)
        custFacilitiesViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        return root
    }
}