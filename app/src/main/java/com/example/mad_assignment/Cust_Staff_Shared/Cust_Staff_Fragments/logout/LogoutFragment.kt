package com.example.mad_assignment.Customer.Cust_Staff_Fragments.logout

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.mad_assignment.CustomerMain
import com.example.mad_assignment.MainActivity
import com.example.mad_assignment.R

//belongs to fragment_logout.xml
class LogoutFragment : Fragment() {

    private lateinit var logoutViewModel: LogoutViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        logoutViewModel =
                ViewModelProvider(this).get(LogoutViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_logout, container, false)

        //action of confirm logout button
        val btnLogout: Button = root.findViewById(R.id.btnLogout)
        btnLogout.setOnClickListener(){
            val intent = Intent(context, MainActivity::class.java)
            startActivity(intent)
        }

        val textView: TextView = root.findViewById(R.id.text_logout)
        logoutViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        return root
    }
}