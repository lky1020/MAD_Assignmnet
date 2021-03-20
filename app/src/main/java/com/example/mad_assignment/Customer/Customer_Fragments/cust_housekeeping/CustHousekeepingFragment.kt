package com.example.mad_assignment.Customer_Fragments.cust_housekeeping

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.mad_assignment.R

//belongs to customer_fragment_housekeeping.xml
class CustHousekeepingFragment : Fragment() {

    private lateinit var custHousekeepingModel: CustHousekeepingModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        custHousekeepingModel =
                ViewModelProvider(this).get(CustHousekeepingModel::class.java)
        val root = inflater.inflate(R.layout.customer_fragment_housekeeping, container, false)
        val textView: TextView = root.findViewById(R.id.text_services)
        custHousekeepingModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        return root
    }
}