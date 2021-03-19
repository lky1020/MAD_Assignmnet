package com.example.mad_assignment.Customer_Fragments.cust_trans_history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.mad_assignment.R

//belongs to customer_fragment_services.xml
class CustTransHistoryFragment : Fragment() {

    private lateinit var custTransHistoryViewModel: CustTransHistoryViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        custTransHistoryViewModel =
                ViewModelProvider(this).get(CustTransHistoryViewModel::class.java)
        val root = inflater.inflate(R.layout.customer_fragment_trans_history, container, false)
        val textView: TextView = root.findViewById(R.id.text_transHis)
        custTransHistoryViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        return root
    }
}