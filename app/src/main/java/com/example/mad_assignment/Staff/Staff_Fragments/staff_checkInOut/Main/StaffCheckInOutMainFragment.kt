package com.example.mad_assignment.Staff.Staff_Fragments.staff_checkInOut.Main

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

class StaffCheckInOutMainFragment : Fragment() {

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?,
    ): View? {

        val root: View =  inflater.inflate(R.layout.staff_fragment_check_in_out, container, false)

        val arguments = arguments
        val type = arguments!!.getString("type")

        val tvCheckIn: TextView = root.findViewById(R.id.tv_check_in)
        val tvCheckOut: TextView = root.findViewById(R.id.tv_check_out)
        val etSearch: EditText = root.findViewById(R.id.et_check_in_out_search)

        // Initialize the fragment
        val ft: FragmentTransaction = activity?.supportFragmentManager!!.beginTransaction()

        if(type == "check in"){
            tvCheckIn.setTextColor(ContextCompat.getColor(requireActivity(), R.color.black))
            tvCheckIn.typeface = Typeface.DEFAULT_BOLD;

            ft.replace(R.id.framel_check_in_out, StaffCheckInFragment())
        }else{
            tvCheckOut.setTextColor(ContextCompat.getColor(requireActivity(), R.color.black))
            tvCheckOut.typeface = Typeface.DEFAULT_BOLD;

            ft.replace(R.id.framel_check_in_out, StaffCheckOutFragment())
        }

        ft.commit()


        tvCheckIn.setOnClickListener {
            tvCheckIn.setTextColor(ContextCompat.getColor(requireActivity(), R.color.black))
            tvCheckIn.typeface = Typeface.DEFAULT_BOLD;

            tvCheckOut.setTextColor(ContextCompat.getColor(requireActivity(), R.color.gray))
            tvCheckOut.typeface = Typeface.DEFAULT;

            etSearch.setText("")

            val ft: FragmentTransaction = activity?.supportFragmentManager!!.beginTransaction()
            ft.replace(R.id.framel_check_in_out, StaffCheckInFragment())
            ft.commit()
        }

        tvCheckOut.setOnClickListener {
            tvCheckOut.setTextColor(ContextCompat.getColor(requireActivity(), R.color.black))
            tvCheckOut.typeface = Typeface.DEFAULT_BOLD;

            tvCheckIn.setTextColor(ContextCompat.getColor(requireActivity(), R.color.gray))
            tvCheckIn.typeface = Typeface.DEFAULT;

            etSearch.setText("")

            val ft: FragmentTransaction = activity?.supportFragmentManager!!.beginTransaction()
            ft.replace(R.id.framel_check_in_out, StaffCheckOutFragment())
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