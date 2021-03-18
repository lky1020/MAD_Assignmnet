package com.example.mad_assignment.Fragments.trans_history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.mad_assignment.R

class TransHistoryFragment : Fragment() {

    private lateinit var transHistoryViewModel: TransHistoryViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        transHistoryViewModel =
                ViewModelProvider(this).get(TransHistoryViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_customer_services, container, false)
        val textView: TextView = root.findViewById(R.id.text_gallery)
        transHistoryViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        return root
    }
}