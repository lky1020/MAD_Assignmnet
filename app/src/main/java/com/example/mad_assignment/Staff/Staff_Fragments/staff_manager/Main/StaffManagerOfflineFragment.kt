package com.example.mad_assignment.Staff.Staff_Fragments.staff_manager.Main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.mad_assignment.R

class StaffManagerOfflineFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.staff_fragment_manager_offline, container, false)
    }
    
}