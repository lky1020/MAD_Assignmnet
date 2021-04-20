package com.example.mad_assignment.Customer.Customer_Fragments.cust_housekeeping.Services.Item

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

class CustHousekeepingAvailableItemFragment : Fragment() {

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val root: View = inflater.inflate(R.layout.customer_fragment_available_housekeeping_item, container,false)

        // Initialize the fragment
        val ft: FragmentTransaction = activity?.supportFragmentManager!!.beginTransaction()
        ft.replace(R.id.framel_Housekeeping_Item, CustHousekeepingItemAvailableFragment())
        ft.commit()

        val tvAvailable: TextView = root.findViewById(R.id.tv_houseKeeping_item_available)
        val tvOrdered: TextView = root.findViewById(R.id.tv_houseKeeping_item_ordered)
        val etItemSearch: EditText = root.findViewById(R.id.et_housekeeping_item_search)

        tvAvailable.setOnClickListener {
            tvAvailable.setTextColor(ContextCompat.getColor(requireActivity(), R.color.black))
            tvAvailable.typeface = Typeface.DEFAULT_BOLD

            tvOrdered.setTextColor(ContextCompat.getColor(requireActivity(), R.color.gray))
            tvOrdered.typeface = Typeface.DEFAULT

            etItemSearch.setText("")

            val ft: FragmentTransaction = activity?.supportFragmentManager!!.beginTransaction()
            ft.replace(R.id.framel_Housekeeping_Item, CustHousekeepingItemAvailableFragment())
            ft.commit()
        }

        tvOrdered.setOnClickListener {
            tvOrdered.setTextColor(ContextCompat.getColor(requireActivity(), R.color.black))
            tvOrdered.typeface = Typeface.DEFAULT_BOLD

            tvAvailable.setTextColor(ContextCompat.getColor(requireActivity(), R.color.gray))
            tvAvailable.typeface = Typeface.DEFAULT

            etItemSearch.setText("")

            val ft: FragmentTransaction = activity?.supportFragmentManager!!.beginTransaction()
            ft.replace(R.id.framel_Housekeeping_Item, CustHousekeepingItemOrderedFragment())
            ft.commit()
        }

        root.setOnTouchListener { v, event ->
            etItemSearch.clearFocus()

            // Disable virtual k
            val imm = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
            imm!!.hideSoftInputFromWindow(etItemSearch.windowToken, 0)

            true
        }

        return root
    }
}