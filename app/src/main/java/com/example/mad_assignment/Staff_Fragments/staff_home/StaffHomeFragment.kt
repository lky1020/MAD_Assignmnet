package com.example.mad_assignment.Staff_Fragments.staff_home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.mad_assignment.R

class StaffHomeFragment : Fragment() {

    private lateinit var staffHomeViewModel: StaffHomeViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        staffHomeViewModel =
                ViewModelProvider(this).get(StaffHomeViewModel::class.java)
        val root = inflater.inflate(R.layout.staff_fragment_home, container, false)
        val textView: TextView = root.findViewById(R.id.text_home1)
        staffHomeViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        return root
    }
}