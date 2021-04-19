package com.example.mad_assignment.Customer.Customer_Fragments.cust_facilities.Main

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mad_assignment.Customer.Customer_Fragments.cust_facilities.Adapter.CustFacilitiesAdapter
import com.example.mad_assignment.R
import com.example.mad_assignment.Staff.facility.Class.Facility
import com.example.mad_assignment.Staff.facility.Model.FacilityViewModel

//belongs to customer_fragment_facilities.xml
class CustFacilitiesFragment : Fragment() {

    private lateinit var custFacilitiesViewModel: FacilityViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var etSearch: EditText
    private lateinit var facilityList: ArrayList<Facility>

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        val root = inflater.inflate(R.layout.customer_fragment_facilities, container, false)


        //Search

        etSearch = root.findViewById(R.id.et_facility_search)

        etSearch.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {
                if(!etSearch.text.equals("")){
                    filter(s.toString())
                }
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

            }
        })

        //Recycle view
        recyclerView = root.findViewById(R.id.rv_manage_facility)
        recyclerView.layoutManager = GridLayoutManager(activity, 2)

        recyclerView.adapter = CustFacilitiesAdapter(ArrayList<Facility>(), requireActivity())//Initialize adapter
        recyclerView.setHasFixedSize(true)

        //Get the viewmodel for facility
        custFacilitiesViewModel = ViewModelProvider(this).get(FacilityViewModel::class.java)

        //Retrieve data from db
        custFacilitiesViewModel.fetchFacilityData()

        //Observe the housekeeping list and set it
        custFacilitiesViewModel.getFacility().observe(viewLifecycleOwner, Observer {
            recyclerView.adapter = CustFacilitiesAdapter(it, requireActivity())
            facilityList = it
        })

        custFacilitiesViewModel.getStatus().observe(viewLifecycleOwner, Observer {
            if (it == false) {
                Toast.makeText(requireContext(), "No facility found!", Toast.LENGTH_SHORT).show()
            }
        })

        root.setOnTouchListener { v, event ->
            etSearch.clearFocus()

            // Disable virtual k
            val imm = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
            imm!!.hideSoftInputFromWindow(etSearch.windowToken, 0)

            true
        }

        return root
    }

    @SuppressLint("DefaultLocale")
    private fun filter(text: String){
        if(activity != null) {
            val filteredList = ArrayList<Facility>()

            for (i in facilityList) {
                if (i.facilityName!!.toLowerCase().contains(text.toLowerCase())) {
                    filteredList.add(i)
                }
            }

            recyclerView.adapter = CustFacilitiesAdapter(filteredList, requireActivity())
        }
    }
}