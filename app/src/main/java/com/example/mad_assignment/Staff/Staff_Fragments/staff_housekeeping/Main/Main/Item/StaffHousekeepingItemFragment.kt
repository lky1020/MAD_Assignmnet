package com.example.mad_assignment.Staff.Staff_Fragments.staff_housekeeping.Main.Main.Item

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
import androidx.appcompat.widget.ContentFrameLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mad_assignment.Customer.Customer_Fragments.cust_housekeeping.Class.HousekeepingItem
import com.example.mad_assignment.R
import com.example.mad_assignment.Staff.Staff_Fragments.staff_housekeeping.Main.Adapter.StaffHousekeepingItemAdapter
import com.example.mad_assignment.Staff.Staff_Fragments.staff_housekeeping.Main.Model.StaffHousekeepingAvailableItemModel

class StaffHousekeepingItemFragment(private val title: String) : Fragment() {

    private lateinit var staffHousekeepingAvailableItemModel: StaffHousekeepingAvailableItemModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var parentLayout: ViewGroup
    private lateinit var parentElement: ContentFrameLayout

    private lateinit var servicesType: String
    private lateinit var etSearch: EditText
    private lateinit var housekeepingItemList: ArrayList<HousekeepingItem>

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        parentLayout = container?.parent as ViewGroup
        parentElement = parentLayout.parent as ContentFrameLayout

        val root: View =  inflater.inflate(R.layout.staff_fragment_housekeeping_item, container, false)

        //Get the viewmodel for housekeeping
        staffHousekeepingAvailableItemModel = ViewModelProvider(this).get(StaffHousekeepingAvailableItemModel::class.java)

        //Get action bar title for retrieve data from db
        servicesType = title

        etSearch = parentElement.findViewById(R.id.et_staff_housekeeping_services_search)

        etSearch.hint = "Search Here..."

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
                if(!etSearch.text.equals("")){
                    filter(s.toString())
                }
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

            }
        })

        recyclerView = view.findViewById(R.id.rv_staff_housekeeping_item)
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.adapter = StaffHousekeepingItemAdapter(ArrayList<HousekeepingItem>(), requireActivity()) //Initialize adapter
        recyclerView.setHasFixedSize(true)

        //Retrieve data from db
        staffHousekeepingAvailableItemModel.retrieveHousekeepingItemFromDB(servicesType)

        //Observe the housekeeping list and set it
        staffHousekeepingAvailableItemModel.gethousekeepingItemList().observe(viewLifecycleOwner, Observer {
            recyclerView.adapter = StaffHousekeepingItemAdapter(it, requireActivity())
            housekeepingItemList = it
        })
    }

    @SuppressLint("DefaultLocale")
    private fun filter(text: String){
        if(activity != null) {
            val filteredList = java.util.ArrayList<HousekeepingItem>()

            for (i in housekeepingItemList) {
                if (i.title.toLowerCase().contains(text.toLowerCase())) {
                    filteredList.add(i)
                }
            }

            recyclerView.adapter = StaffHousekeepingItemAdapter(filteredList, requireActivity())
        }
    }
}