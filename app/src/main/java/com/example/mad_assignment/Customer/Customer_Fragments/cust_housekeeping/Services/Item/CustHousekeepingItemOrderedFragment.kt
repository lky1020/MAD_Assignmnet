package com.example.mad_assignment.Customer.Customer_Fragments.cust_housekeeping.Services.Item

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mad_assignment.Customer.Customer_Fragments.cust_housekeeping.Adapter.HousekeepingItemOrderedAdapter
import com.example.mad_assignment.Customer.Customer_Fragments.cust_housekeeping.Class.HousekeepingOrderedItem
import com.example.mad_assignment.Customer.Customer_Fragments.cust_housekeeping.Model.CustHousekeepingItemOrderedModel
import com.example.mad_assignment.R

class CustHousekeepingItemOrderedFragment : Fragment() {

    private lateinit var custHousekeepingItemOrderedModel: CustHousekeepingItemOrderedModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var parentLayout: ViewGroup
    private lateinit var parentElement: FrameLayout

    // Search
    private lateinit var etItemSearch: EditText
    private lateinit var housekeepingItemOrderedList: ArrayList<HousekeepingOrderedItem>

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?)
    : View? {
        parentLayout = container?.parent as ViewGroup
        parentElement = parentLayout.parent as FrameLayout

        return inflater.inflate(R.layout.customer_fragment_ordered_housekeeping_item, container, false)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        etItemSearch = parentElement.findViewById(R.id.et_housekeeping_item_search)

        etItemSearch.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {
                if(!etItemSearch.text.equals("")){
                    filter(s.toString())
                }
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

            }
        })

        recyclerView = view.findViewById(R.id.rv_houseKeeping_ordered)
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.adapter = HousekeepingItemOrderedAdapter(ArrayList<HousekeepingOrderedItem>(), requireActivity()) //Initialize adapter
        recyclerView.setHasFixedSize(true)

        //Get the viewmodel for housekeeping item ordered
        custHousekeepingItemOrderedModel = ViewModelProvider(this).get(CustHousekeepingItemOrderedModel::class.java)

        //Retrieve data from db
        custHousekeepingItemOrderedModel.retrieveHousekeepingItemOrderedFromDB("Bed Textiles")
        custHousekeepingItemOrderedModel.retrieveHousekeepingItemOrderedFromDB("Toiletries")

        //Observe the Item Ordered list and set it
        custHousekeepingItemOrderedModel.gethousekeepingItemOrderedList().observe(viewLifecycleOwner, Observer {
            recyclerView.adapter = HousekeepingItemOrderedAdapter(it, requireActivity())
            housekeepingItemOrderedList = it
        })

        custHousekeepingItemOrderedModel.getStatus().observe(viewLifecycleOwner, Observer {
            if (it == false) {
                Toast.makeText(requireContext(), "No Item Ordered!", Toast.LENGTH_SHORT).show()
            }
        })
    }

    @SuppressLint("DefaultLocale")
    private fun filter(text: String){
        if(activity != null) {
            val filteredList = ArrayList<HousekeepingOrderedItem>()

            for (i in housekeepingItemOrderedList) {
                if (i.title.toLowerCase().contains(text.toLowerCase())) {
                    filteredList.add(i)
                }
            }

            recyclerView.adapter = HousekeepingItemOrderedAdapter(filteredList, requireActivity())
        }
    }
}