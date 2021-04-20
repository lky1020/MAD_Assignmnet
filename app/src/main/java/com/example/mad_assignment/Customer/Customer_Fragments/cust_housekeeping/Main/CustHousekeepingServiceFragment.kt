package com.example.mad_assignment.Customer.Customer_Fragments.cust_housekeeping.Main

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainerView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mad_assignment.Customer.Customer_Fragments.cust_housekeeping.Adapter.HousekeepingServicesAdapter
import com.example.mad_assignment.Customer.Customer_Fragments.cust_housekeeping.Class.Housekeeping
import com.example.mad_assignment.Customer.Customer_Fragments.cust_housekeeping.Model.CustHousekeepingServicesModel
import com.example.mad_assignment.R

class CustHousekeepingServiceFragment : Fragment() {

    private lateinit var custHousekeepingServicesModel: CustHousekeepingServicesModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var parentLayout: ViewGroup
    private lateinit var parentElement: FragmentContainerView

    // Search
    private lateinit var etSearch: EditText
    private lateinit var housekeepingList: ArrayList<Housekeeping>

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?
    ): View? {
        parentLayout = container?.parent as ViewGroup
        parentElement = parentLayout.parent as FragmentContainerView

        return inflater.inflate(R.layout.customer_fragment_housekeeping_service, container, false)
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

        recyclerView = view.findViewById(R.id.rv_housekeeping)
        recyclerView.layoutManager = GridLayoutManager(activity, 2)
        recyclerView.adapter = HousekeepingServicesAdapter(ArrayList<Housekeeping>(), requireActivity()) //Initialize adapter
        recyclerView.setHasFixedSize(true)

        //Get the viewmodel for housekeeping
        custHousekeepingServicesModel = ViewModelProvider(this).get(CustHousekeepingServicesModel::class.java)

        //Retrieve data from db
        custHousekeepingServicesModel.retrieveHousekeepingFromDB()

        //Observe the housekeeping list and set it
        custHousekeepingServicesModel.getHousekeepingList().observe(viewLifecycleOwner, Observer {
            recyclerView.adapter = HousekeepingServicesAdapter(it, requireActivity())
            housekeepingList = it
        })
    }

    @SuppressLint("DefaultLocale")
    private fun filter(text: String){
        if(activity != null) {
            val filteredList = ArrayList<Housekeeping>()

            for (i in housekeepingList) {
                if (i.title.toLowerCase().contains(text.toLowerCase())) {
                    filteredList.add(i)
                }
            }

            recyclerView.adapter = HousekeepingServicesAdapter(filteredList, requireActivity())
        }
    }
}