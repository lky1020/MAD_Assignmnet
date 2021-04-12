package com.example.mad_assignment.Customer.Customer_Fragments.cust_housekeeping.Main

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.FragmentContainerView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mad_assignment.Customer.Customer_Fragments.cust_housekeeping.Adapter.HousekeepingRequestedAdapter
import com.example.mad_assignment.Customer.Customer_Fragments.cust_housekeeping.Adapter.HousekeepingServicesAdapter
import com.example.mad_assignment.Customer.Customer_Fragments.cust_housekeeping.Class.BookedHousekeepingService
import com.example.mad_assignment.Customer.Customer_Fragments.cust_housekeeping.Class.Housekeeping
import com.example.mad_assignment.Customer.Customer_Fragments.cust_housekeepingRequested.Model.CustHousekeepingRequestedModel
import com.example.mad_assignment.R

class CustHousekeepingRequestFragment : Fragment() {

    private lateinit var custHousekeepingRequestedModel: CustHousekeepingRequestedModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var parentLayout: ViewGroup
    private lateinit var parentElement: FragmentContainerView

    // Search
    private lateinit var etSearch: EditText
    private lateinit var housekeepingRequestedList: ArrayList<BookedHousekeepingService>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        parentLayout = container?.parent as ViewGroup
        parentElement = parentLayout.parent as FragmentContainerView

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.customer_fragment_housekeeping_request, container, false)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        etSearch = parentElement.findViewById(R.id.et_housekeeping_search)

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

        recyclerView = view.findViewById(R.id.rv_houseKeeping_requested)
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.adapter = HousekeepingRequestedAdapter(ArrayList<BookedHousekeepingService>(), requireActivity()) //Initialize adapter
        recyclerView.setHasFixedSize(true)

        //Get the viewmodel for housekeeping requested
        custHousekeepingRequestedModel = ViewModelProvider(this).get(CustHousekeepingRequestedModel::class.java)

        //Retrieve data from db
        custHousekeepingRequestedModel.retrieveHousekeepingRequestedFromDB("Room Cleaning")
        custHousekeepingRequestedModel.retrieveHousekeepingRequestedFromDB("Laundry Service")

        //Observe the housekeeping list and set it
        custHousekeepingRequestedModel.gethousekeepingRequestedList().observe(viewLifecycleOwner, Observer {
            recyclerView.adapter = HousekeepingRequestedAdapter(it, requireActivity())
            housekeepingRequestedList = it
        })

        custHousekeepingRequestedModel.getStatus().observe(viewLifecycleOwner, Observer {
            if (it == false) {
                Toast.makeText(requireContext(), "No Services Requested!", Toast.LENGTH_SHORT).show()
            }
        })
    }

    @SuppressLint("DefaultLocale")
    private fun filter(text: String){
        if(activity != null) {
            val filteredList = ArrayList<BookedHousekeepingService>()

            for (i in housekeepingRequestedList) {
                if (i.serviceType.toLowerCase().contains(text.toLowerCase())) {
                    filteredList.add(i)
                }
            }

            recyclerView.adapter = HousekeepingRequestedAdapter(filteredList, requireActivity())
        }
    }
}