package com.example.mad_assignment.Staff.Staff_Fragments.staff_housekeeping.Main.Main.Services

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
import androidx.appcompat.widget.ContentFrameLayout
import androidx.fragment.app.FragmentContainerView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mad_assignment.Customer.Customer_Fragments.cust_housekeeping.Adapter.HousekeepingRequestedAdapter
import com.example.mad_assignment.Customer.Customer_Fragments.cust_housekeeping.Adapter.HousekeepingServicesAdapter
import com.example.mad_assignment.Customer.Customer_Fragments.cust_housekeeping.Class.BookedHousekeepingService
import com.example.mad_assignment.Customer.Customer_Fragments.cust_housekeeping.Class.Housekeeping
import com.example.mad_assignment.Customer.Customer_Fragments.cust_housekeeping.Class.LaundryService
import com.example.mad_assignment.Customer.Customer_Fragments.cust_housekeeping.Class.RoomCleaningService
import com.example.mad_assignment.Customer.Customer_Fragments.cust_housekeeping.Model.CustHousekeepingServicesModel
import com.example.mad_assignment.R
import com.example.mad_assignment.Staff.Staff_Fragments.staff_housekeeping.Main.Adapter.StaffLaundryServicesAdadpter
import com.example.mad_assignment.Staff.Staff_Fragments.staff_housekeeping.Main.Adapter.StaffRoomCleaningServicesAdadpter
import com.example.mad_assignment.Staff.Staff_Fragments.staff_housekeeping.Main.Model.StaffHousekeepingAvailableServicesModel

class StaffHousekeepingServicesFragment(private val title: String) : Fragment() {

    private lateinit var staffHousekeepingAvailableServicesModel: StaffHousekeepingAvailableServicesModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var parentLayout: ViewGroup
    private lateinit var parentElement: ContentFrameLayout

    private lateinit var servicesType: String
    private lateinit var etSearch: EditText
    private var roomCleaningList = java.util.ArrayList<RoomCleaningService>()
    private var laundryServiceList = java.util.ArrayList<LaundryService>()

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        parentLayout = container?.parent as ViewGroup
        parentElement = parentLayout.parent as ContentFrameLayout

        val root: View = inflater.inflate(R.layout.staff_fragment_housekeeping_services, container, false)

        //Get the viewmodel for housekeeping
        staffHousekeepingAvailableServicesModel = ViewModelProvider(this).get(StaffHousekeepingAvailableServicesModel::class.java)

        staffHousekeepingAvailableServicesModel.getStatus().observe(viewLifecycleOwner, Observer {
            if (it == false) {
                Toast.makeText(requireContext(), "No Services Available!", Toast.LENGTH_SHORT).show()
            }
        })

        //Get action bar title for retrieve data from db
        servicesType = title

        etSearch = parentElement.findViewById(R.id.et_staff_housekeeping_services_search)

        etSearch.hint = "Search Date Here: Apr 17 2021"

        root.setOnTouchListener { v, event ->
            etSearch.clearFocus()

            // Disable virtual k
            val imm = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
            imm!!.hideSoftInputFromWindow(etSearch.windowToken, 0)

            true
        }

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        etSearch.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {
                if(!etSearch.text.equals("") && etSearch.text.length >= 10 && etSearch.text.length <= 11){
                    displayAvailableServiceRV(view)
                }
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

            }
        })
    }

    private fun displayAvailableServiceRV(view: View){
        recyclerView = view.findViewById(R.id.rv_staff_housekeeping_services)
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.adapter = StaffRoomCleaningServicesAdadpter(ArrayList<RoomCleaningService>(), requireActivity()) //Initialize adapter
        recyclerView.setHasFixedSize(true)

        if(servicesType == "Room Cleaning"){
            //Retrieve data from db
            staffHousekeepingAvailableServicesModel.retrieveHousekeepingServicesFromDB("Room Cleaning", etSearch.text.toString())

            //Observe the housekeeping list and set it
            staffHousekeepingAvailableServicesModel.getRoomCleaningServicesList().observe(viewLifecycleOwner, Observer {
                recyclerView.adapter = StaffRoomCleaningServicesAdadpter(it, requireActivity())
                roomCleaningList = it
            })
        }else{
            //Retrieve data from db
            staffHousekeepingAvailableServicesModel.retrieveHousekeepingServicesFromDB("Laundry Service", etSearch.text.toString())

            //Observe the housekeeping list and set it
            staffHousekeepingAvailableServicesModel.getLaundryServicesList().observe(viewLifecycleOwner, Observer {
                recyclerView.adapter = StaffLaundryServicesAdadpter(it, requireActivity())
                laundryServiceList = it
            })
        }
    }
}