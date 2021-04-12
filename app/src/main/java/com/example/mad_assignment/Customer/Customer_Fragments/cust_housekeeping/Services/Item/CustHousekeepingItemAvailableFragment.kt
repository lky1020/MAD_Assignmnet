package com.example.mad_assignment.Customer.Customer_Fragments.cust_housekeeping.Services.Item

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
import androidx.fragment.app.FragmentContainerView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mad_assignment.Customer.Customer_Fragments.cust_housekeeping.Adapter.HousekeepingItemAdapter
import com.example.mad_assignment.Customer.Customer_Fragments.cust_housekeeping.Adapter.HousekeepingServicesAdapter
import com.example.mad_assignment.Customer.Customer_Fragments.cust_housekeeping.Class.Housekeeping
import com.example.mad_assignment.Customer.Customer_Fragments.cust_housekeeping.Class.HousekeepingItem
import com.example.mad_assignment.Customer.Customer_Fragments.cust_housekeeping.Model.CustHousekeepingItemModel
import com.example.mad_assignment.CustomerMain
import com.example.mad_assignment.R

class CustHousekeepingItemAvailableFragment : Fragment() {

    private lateinit var custHousekeepingItemModel: CustHousekeepingItemModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var parentLayout: ViewGroup
    private lateinit var parentElement: FrameLayout

    // Search
    private lateinit var etItemSearch: EditText
    private lateinit var housekeepingItemList: ArrayList<HousekeepingItem>
    private lateinit var servicesType: String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?)
    : View? {

        parentLayout = container?.parent as ViewGroup
        parentElement = parentLayout.parent as FrameLayout

        val root: View = inflater.inflate(R.layout.customer_fragment_housekeeping_item_available, container, false)

        //Get action bar title for retrieve data from db
        servicesType = (activity as CustHousekeepingAvailableItemActivity?)!!.supportActionBar!!.title.toString()

        return root
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

        recyclerView = view.findViewById(R.id.rv_housekeeping_item)
        recyclerView.layoutManager = GridLayoutManager(activity, 2)
        recyclerView.adapter = HousekeepingItemAdapter(ArrayList<HousekeepingItem>(), requireActivity(), servicesType) //Initialize adapter
        recyclerView.setHasFixedSize(true)

        //Get the viewmodel for housekeeping
        custHousekeepingItemModel = ViewModelProvider(this).get(CustHousekeepingItemModel::class.java)

        //Retrieve data from db
        custHousekeepingItemModel.retrieveHousekeepingItemFromDB(servicesType)

        //Observe the housekeeping list and set it
        custHousekeepingItemModel.getHousekeepingItemList().observe(viewLifecycleOwner, Observer {
            recyclerView.adapter = HousekeepingItemAdapter(it, requireActivity(), servicesType)
            housekeepingItemList = it
        })
    }

    @SuppressLint("DefaultLocale")
    private fun filter(text: String){
        if(activity != null) {
            val filteredList = ArrayList<HousekeepingItem>()

            for (i in housekeepingItemList) {
                if (i.title.toLowerCase().contains(text.toLowerCase())) {
                    filteredList.add(i)
                }
            }

            recyclerView.adapter = HousekeepingItemAdapter(filteredList, requireActivity(), servicesType)
        }
    }
}