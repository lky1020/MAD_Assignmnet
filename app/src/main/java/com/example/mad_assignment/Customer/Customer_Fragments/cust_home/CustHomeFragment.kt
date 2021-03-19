package com.example.mad_assignment.Customer_Fragments.cust_home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.mad_assignment.R

//belongs to customer_fragment_home.xml
class CustHomeFragment : Fragment() {

    private lateinit var custHomeViewModel: CustHomeViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        custHomeViewModel =
                ViewModelProvider(this).get(CustHomeViewModel::class.java)
        val root = inflater.inflate(R.layout.customer_fragment_home, container, false)
        val textView: TextView = root.findViewById(R.id.text_home)
        custHomeViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        return root
    }
}