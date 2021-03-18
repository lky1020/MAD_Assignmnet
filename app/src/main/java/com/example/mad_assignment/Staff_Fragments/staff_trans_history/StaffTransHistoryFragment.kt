package com.example.mad_assignment.Staff_Fragments.staff_trans_history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.mad_assignment.R

class StaffTransHistoryFragment : Fragment() {

    private lateinit var staffTransHistoryViewModel: StaffTransHistoryViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        staffTransHistoryViewModel =
                ViewModelProvider(this).get(StaffTransHistoryViewModel::class.java)
        val root = inflater.inflate(R.layout.staff_fragment_trans_history, container, false)
        val textView: TextView = root.findViewById(R.id.text_transHis1)
        staffTransHistoryViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        return root
    }
}