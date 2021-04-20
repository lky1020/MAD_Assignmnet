package com.example.mad_assignment.Staff.Staff_Fragments.staff_housekeeping.Main.Main

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
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mad_assignment.Customer.Customer_Fragments.cust_housekeeping.Class.Housekeeping
import com.example.mad_assignment.R
import com.example.mad_assignment.Staff.Staff_Fragments.staff_housekeeping.Main.Adapter.StaffHousekeepingServicesAdapter
import com.example.mad_assignment.Staff.Staff_Fragments.staff_housekeeping.Main.Model.StaffHousekeepingServicesModel
import java.util.*


class StaffHousekeepingMainFragment : Fragment() {

    private lateinit var staffHousekeepingServicesModel: StaffHousekeepingServicesModel
    private lateinit var recyclerView: RecyclerView

    // Search
    private lateinit var etSearch: EditText
    private var housekeepingList = ArrayList<Housekeeping>()

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val root: View = inflater.inflate(R.layout.staff_fragment_housekeeping, container, false)

        etSearch = root.findViewById(R.id.et_housekeeping_search)

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

        recyclerView = view.findViewById(R.id.rv_staff_housekeeping)
        recyclerView.layoutManager = GridLayoutManager(activity, 2)
        recyclerView.adapter = StaffHousekeepingServicesAdapter(ArrayList<Housekeeping>(), requireActivity()) //Initialize adapter
        recyclerView.setHasFixedSize(true)

        staffHousekeepingServicesModel = ViewModelProvider(this).get(StaffHousekeepingServicesModel::class.java)

        //Retrieve data from db
        staffHousekeepingServicesModel.retrieveHousekeepingFromDB()

        //Observe the housekeeping list and set it
        staffHousekeepingServicesModel.getHousekeepingList().observe(viewLifecycleOwner, Observer {
            recyclerView.adapter = StaffHousekeepingServicesAdapter(it, requireActivity())
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

            recyclerView.adapter = StaffHousekeepingServicesAdapter(filteredList, requireActivity())
        }
    }
}