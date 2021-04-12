package com.example.mad_assignment.Staff.Staff_Fragments.staff_housekeeping.Main.Main.Services

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mad_assignment.Customer.Customer_Fragments.cust_housekeeping.Adapter.HousekeepingServicesAdapter
import com.example.mad_assignment.Customer.Customer_Fragments.cust_housekeeping.Class.Housekeeping
import com.example.mad_assignment.Customer.Customer_Fragments.cust_housekeeping.Class.RoomCleaningService
import com.example.mad_assignment.Customer.Customer_Fragments.cust_housekeeping.Model.CustHousekeepingServicesModel
import com.example.mad_assignment.R
import com.example.mad_assignment.Staff.Staff_Fragments.staff_housekeeping.Main.Adapter.StaffLaundryServicesAdadpter
import com.example.mad_assignment.Staff.Staff_Fragments.staff_housekeeping.Main.Adapter.StaffRoomCleaningServicesAdadpter
import com.example.mad_assignment.Staff.Staff_Fragments.staff_housekeeping.Main.Model.StaffHousekeepingAvailableServicesModel

class StaffHousekeepingServicesFragment(private val title: String) : Fragment() {

    private lateinit var staffHousekeepingAvailableServicesModel: StaffHousekeepingAvailableServicesModel
    private lateinit var recyclerView: RecyclerView

    private lateinit var servicesType: String

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val root: View = inflater.inflate(R.layout.staff_fragment_housekeeping_services, container, false)

        //Get the viewmodel for housekeeping
        staffHousekeepingAvailableServicesModel = ViewModelProvider(this).get(StaffHousekeepingAvailableServicesModel::class.java)

        //Get action bar title for retrieve data from db
        servicesType = title

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.rv_staff_housekeeping_services)
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.adapter = StaffRoomCleaningServicesAdadpter(ArrayList<RoomCleaningService>(), requireActivity()) //Initialize adapter
        recyclerView.setHasFixedSize(true)

        if(servicesType == "Room Cleaning"){
            //Retrieve data from db
            staffHousekeepingAvailableServicesModel.retrieveHousekeepingServicesFromDB("Room Cleaning")

            //Observe the housekeeping list and set it
            staffHousekeepingAvailableServicesModel.getRoomCleaningServicesList().observe(viewLifecycleOwner, Observer {
                recyclerView.adapter = StaffRoomCleaningServicesAdadpter(it, requireActivity())
            })
        }else{
            //Retrieve data from db
            staffHousekeepingAvailableServicesModel.retrieveHousekeepingServicesFromDB("Laundry Service")

            //Observe the housekeeping list and set it
            staffHousekeepingAvailableServicesModel.getLaundryServicesList().observe(viewLifecycleOwner, Observer {
                recyclerView.adapter = StaffLaundryServicesAdadpter(it, requireActivity())
            })
        }

    }
}