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
import androidx.fragment.app.FragmentContainerView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mad_assignment.Customer.Customer_Fragments.cust_housekeeping.Class.Housekeeping
import com.example.mad_assignment.Customer.Customer_Fragments.cust_housekeeping.Adapter.HousekeepingServicesAdapter
import com.example.mad_assignment.Customer.Customer_Fragments.cust_housekeeping.Class.LaundryService
import com.example.mad_assignment.Customer.Customer_Fragments.cust_housekeeping.Class.RoomCleaningService
import com.example.mad_assignment.Customer.Customer_Fragments.cust_housekeeping.Model.CustHousekeepingServicesModel
import com.example.mad_assignment.R
import com.google.firebase.database.FirebaseDatabase

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

        // Room Cleaning
//        val servicesList1 = ArrayList<RoomCleaningService>()
//        servicesList1.add(RoomCleaningService("Apr 24 2021","11:00 AM", "11:30 PM", "Available"))
//        servicesList1.add(RoomCleaningService("Apr 24 2021","11:30 AM", "12:00 PM", "Available"))
//        servicesList1.add(RoomCleaningService("Apr 24 2021","12:00 PM", "12:30 PM", "Available"))
//        servicesList1.add(RoomCleaningService("Apr 24 2021","12:30 PM", "01:00 PM", "Available"))
//        servicesList1.add(RoomCleaningService("Apr 24 2021","01:00 PM", "01:30 PM", "Available"))
//
//        val myRef1 = FirebaseDatabase.getInstance().getReference("Housekeeping").child("Room Cleaning").child("ServicesAvailable")
//        myRef1.child("Apr 24 2021").setValue(servicesList1)
//
//        //Laundry Service
//        val servicesList2 = ArrayList<LaundryService>()
//        servicesList2.add(LaundryService("Apr 24 2021","11:00 AM", "03:00 PM", "Available"))
//        servicesList2.add(LaundryService("Apr 24 2021","11:30 AM", "05:00 PM", "Available"))
//        servicesList2.add(LaundryService("Apr 24 2021","12:00 PM", "05:00 PM", "Not Available"))
//        servicesList2.add(LaundryService("Apr 24 2021","12:30 PM", "08:00 PM", "Available"))
//        servicesList2.add(LaundryService("Apr 24 2021","01:30 PM", "08:00 PM", "Available"))
//        servicesList2.add(LaundryService("Apr 24 2021","02:00 PM", "03:00 PM", "Available"))
//        servicesList2.add(LaundryService("Apr 24 2021","03:30 PM", "04:00 PM", "Available"))
//        servicesList2.add(LaundryService("Apr 24 2021","04:00 PM", "05:00 PM", "Not Available"))
//        servicesList2.add(LaundryService("Apr 24 2021","05:30 PM", "05:30 PM", "Available"))
//        servicesList2.add(LaundryService("Apr 24 2021","05:30 PM", "06:00 PM", "Available"))
//
//        val myRef2 = FirebaseDatabase.getInstance().getReference("Housekeeping").child("Laundry Service").child("ServicesAvailable")
//        myRef2.child("Apr 24 2021").setValue(servicesList2)

        //Hardcode db first (waiting staff housekeeping)
        //Housekeeping
//        val database = FirebaseDatabase.getInstance()
//        val myRef = database.getReference("Housekeeping")
//
//        val housekeepingItem = Housekeeping("Bed Textiles",
//                "https://firebasestorage.googleapis.com/v0/b/quadcorehms-5b4ed.appspot.com/o/housekeeping%2FBed%20Textiles?alt=media&token=3bb64699-12d6-4ecf-bebc-45d5ae964165")
//
//        myRef.child(housekeepingItem.title).setValue(housekeepingItem)
//
//        val filePath: Uri = Uri.parse("res:///" + R.drawable.register_img)
//
//        val imageRef = FirebaseStorage.getInstance().getReference("Housekeeping")
//        imageRef.putFile(filePath)

        //Bed Textiles
//        val database = FirebaseDatabase.getInstance()
//        val myRef = database.getReference("Housekeeping").child("Bed Textiles").child("ItemAvailable")
//
//        val bedTextilesItem = HousekeepingItem("Cushion",
//                "https://firebasestorage.googleapis.com/v0/b/quadcorehms-5b4ed.appspot.com/o/bedTextiles%2FCushion.png?alt=media&token=14b11887-220b-4e13-b67b-04874dfa2f1c")
//
//        myRef.child(bedTextilesItem.title).setValue(bedTextilesItem)

        //Toiletries
//        val database = FirebaseDatabase.getInstance()
//        val myRef = database.getReference("Housekeeping").child("Toiletries").child("ItemAvailable")
//
//        val bedTextilesItem = HousekeepingItem("Lotion",
//                "https://firebasestorage.googleapis.com/v0/b/quadcorehms-5b4ed.appspot.com/o/toiletries%2FLotion.png?alt=media&token=b8cd5f43-1619-4914-a88b-26ffe895718b")
//
//        myRef.child(bedTextilesItem.title).setValue(bedTextilesItem)

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