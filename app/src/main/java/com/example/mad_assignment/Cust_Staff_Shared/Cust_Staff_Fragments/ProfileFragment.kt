package com.example.mad_assignment.Cust_Staff_Shared.Cust_Staff_Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.mad_assignment.R

//belongs to fragment_profile.xml
class ProfileFragment : Fragment() {

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        //retrieve data from CustomerMain.kt /StaffMain.kt
        val userID = activity?.intent?.getStringExtra("UserID")
        val root = inflater.inflate(R.layout.fragment_profile, container, false)
        val textView: TextView = root.findViewById(R.id.text_profile)

        //assign data to test
        textView.text = userID.toString()
        return root
    }


}