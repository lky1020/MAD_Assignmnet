package com.example.mad_assignment.Customer.Customer_Fragments.cust_housekeeping.Services.Item

import android.graphics.Typeface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentTransaction
import com.example.mad_assignment.Customer.Customer_Fragments.cust_housekeeping.Class.HousekeepingBottomSheetFragment
import com.example.mad_assignment.Customer.Customer_Fragments.cust_housekeeping.Main.CustHousekeepingRequestFragment
import com.example.mad_assignment.Customer.Customer_Fragments.cust_housekeeping.Main.CustHousekeepingServiceFragment
import com.example.mad_assignment.R

class CustHousekeepingAvailableItemFragment : Fragment() {

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

        tvAvailable.setOnClickListener {
            tvAvailable.setTextColor(ContextCompat.getColor(requireActivity(), R.color.black))
            tvAvailable.typeface = Typeface.DEFAULT_BOLD;

            tvOrdered.setTextColor(ContextCompat.getColor(requireActivity(), R.color.gray))
            tvOrdered.typeface = Typeface.DEFAULT;

            val ft: FragmentTransaction = activity?.supportFragmentManager!!.beginTransaction()
            ft.replace(R.id.framel_Housekeeping_Item, CustHousekeepingItemAvailableFragment())
            ft.commit()
        }

        tvOrdered.setOnClickListener {
            tvOrdered.setTextColor(ContextCompat.getColor(requireActivity(), R.color.black))
            tvOrdered.typeface = Typeface.DEFAULT_BOLD;

            tvAvailable.setTextColor(ContextCompat.getColor(requireActivity(), R.color.gray))
            tvAvailable.typeface = Typeface.DEFAULT;

            val ft: FragmentTransaction = activity?.supportFragmentManager!!.beginTransaction()
            ft.replace(R.id.framel_Housekeeping_Item, CustHousekeepingItemOrderedFragment())
            ft.commit()
        }

        return root
    }
}