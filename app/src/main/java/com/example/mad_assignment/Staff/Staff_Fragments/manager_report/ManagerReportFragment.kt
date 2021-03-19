package com.example.mad_assignment.Staff_Fragments.manager_report

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.mad_assignment.R

//belongs to staff_fragment_home.xml
class ManagerReportFragment : Fragment() {

    private lateinit var managerReportViewModel: ManagerReportViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        managerReportViewModel =
                ViewModelProvider(this).get(ManagerReportViewModel::class.java)
        val root = inflater.inflate(R.layout.staff_fragment_report, container, false)
        val textView: TextView = root.findViewById(R.id.text_report)
        managerReportViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        return root
    }
}