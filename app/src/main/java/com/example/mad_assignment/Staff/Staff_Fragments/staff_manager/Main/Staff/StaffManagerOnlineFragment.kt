package com.example.mad_assignment.Staff.Staff_Fragments.staff_manager.Main.Staff

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
import android.widget.FrameLayout
import android.widget.Toast
import androidx.appcompat.widget.ContentFrameLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mad_assignment.Class.Staff
import com.example.mad_assignment.Customer.Customer_Fragments.cust_housekeeping.Adapter.AvailableLaundryServicesAdadpter
import com.example.mad_assignment.Customer.Customer_Fragments.cust_housekeeping.Adapter.AvailableRoomCleaningServicesAdadpter
import com.example.mad_assignment.Customer.Customer_Fragments.cust_housekeeping.Class.HousekeepingItem
import com.example.mad_assignment.Customer.Customer_Fragments.cust_housekeeping.Class.RoomCleaningService
import com.example.mad_assignment.Customer.Customer_Fragments.cust_housekeeping.Model.CustAvailableHousekeepingServicesModel
import com.example.mad_assignment.R
import com.example.mad_assignment.Staff.Staff_Fragments.staff_housekeeping.Main.Adapter.StaffHousekeepingItemAdapter
import com.example.mad_assignment.Staff.Staff_Fragments.staff_manager.Adapter.StaffManagerAdapter
import com.example.mad_assignment.Staff.Staff_Fragments.staff_manager.Model.StaffManagerModel
import kotlinx.android.synthetic.main.customer_fragment_available_housekeeping_services.*

class StaffManagerOnlineFragment : Fragment() {

    private lateinit var staffManagerModel: StaffManagerModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var parentLayout: ViewGroup
    private lateinit var parentElement: FrameLayout

    private lateinit var etManagerSeacrh: EditText
    private var staffList = ArrayList<Staff>()

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        parentLayout = container?.parent as ViewGroup
        parentElement = parentLayout.parent as FrameLayout

        val root: View = inflater.inflate(R.layout.staff_fragment_manager_online, container, false)

        staffManagerModel = ViewModelProvider(this).get(StaffManagerModel::class.java)
        staffManagerModel.getStatus().observe(viewLifecycleOwner, Observer {
            if (it == false) {
                Toast.makeText(requireContext(), "No Staff Available!", Toast.LENGTH_SHORT).show()
            }
        })

        etManagerSeacrh = parentElement.findViewById(R.id.et_manager_search)

        root.setOnTouchListener { v, event ->
            etManagerSeacrh.clearFocus()

            // Disable virtual k
            val imm = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
            imm!!.hideSoftInputFromWindow(etManagerSeacrh.windowToken, 0)

            true
        }

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        etManagerSeacrh.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {
                if(!etManagerSeacrh.text.equals("") && staffList.size > 0){
                    filter(s.toString())
                }
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

            }
        })

        recyclerView = view.findViewById(R.id.rv_staff)
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.adapter = StaffManagerAdapter(ArrayList<Staff>(), requireActivity()) //Initialize adapter
        recyclerView.setHasFixedSize(true)

        staffManagerModel.retrieveOnlineStaffFromDB("Online")
        staffManagerModel.getStaffList().observe(viewLifecycleOwner, Observer{
            recyclerView.adapter = StaffManagerAdapter(it, requireActivity())
            staffList = it
        })
    }

    @SuppressLint("DefaultLocale")
    private fun filter(text: String){
        if(activity != null) {
            val filteredList = ArrayList<Staff>()

            for (i in staffList) {
                if (i.name.toLowerCase().contains(text.toLowerCase())) {
                    filteredList.add(i)
                }
            }

            recyclerView.adapter = StaffManagerAdapter(filteredList, requireActivity())
        }
    }
}