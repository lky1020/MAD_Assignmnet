package com.example.mad_assignment.Customer.Customer_Fragments.cust_housekeeping.Main

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Typeface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.example.mad_assignment.R

//belongs to customer_fragment_housekeeping.xml
class CustHousekeepingFragment : Fragment() {

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root: View =  inflater.inflate(R.layout.customer_fragment_housekeeping, container,false)

        // Initialize the fragment
        val ft: FragmentTransaction = activity?.supportFragmentManager!!.beginTransaction()
        ft.replace(R.id.framel_Housekeeping, CustHousekeepingServiceFragment())
        ft.commit()

        val tvServices: TextView = root.findViewById(R.id.tv_houseKeeping_services)
        val tvRequested: TextView = root.findViewById(R.id.tv_houseKeeping_requested)
        val etSearch: EditText = root.findViewById(R.id.et_housekeeping_search)

        tvServices.setOnClickListener {
            tvServices.setTextColor(ContextCompat.getColor(requireActivity(), R.color.black))
            tvServices.typeface = Typeface.DEFAULT_BOLD

            tvRequested.setTextColor(ContextCompat.getColor(requireActivity(), R.color.gray))
            tvRequested.typeface = Typeface.DEFAULT

            etSearch.setText("")

            val ft: FragmentTransaction = activity?.supportFragmentManager!!.beginTransaction()
            ft.replace(R.id.framel_Housekeeping, CustHousekeepingServiceFragment())
            ft.commit()
        }

        tvRequested.setOnClickListener {
            tvRequested.setTextColor(ContextCompat.getColor(requireActivity(), R.color.black))
            tvRequested.typeface = Typeface.DEFAULT_BOLD

            tvServices.setTextColor(ContextCompat.getColor(requireActivity(), R.color.gray))
            tvServices.typeface = Typeface.DEFAULT

            etSearch.setText("")

            val ft: FragmentTransaction = activity?.supportFragmentManager!!.beginTransaction()
            ft.replace(R.id.framel_Housekeeping, CustHousekeepingRequestFragment())
            ft.commit()
        }

        root.setOnTouchListener { v, event ->
            etSearch.clearFocus()

            // Disable virtual k
            val imm = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
            imm!!.hideSoftInputFromWindow(etSearch.windowToken, 0)

            true
        }

        return root
    }
}