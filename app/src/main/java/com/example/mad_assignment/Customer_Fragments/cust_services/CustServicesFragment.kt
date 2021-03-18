package com.example.mad_assignment.Customer_Fragments.cust_services

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.mad_assignment.R

class CustServicesFragment : Fragment() {

    private lateinit var custServicesViewModel: CustServicesViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        custServicesViewModel =
                ViewModelProvider(this).get(CustServicesViewModel::class.java)
        val root = inflater.inflate(R.layout.customer_fragment_services, container, false)
        val textView: TextView = root.findViewById(R.id.text_services)
        custServicesViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        return root
    }
}