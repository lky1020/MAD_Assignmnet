package com.example.mad_assignment.Customer.Customer_Fragments.cust_housekeeping

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mad_assignment.Customer.Customer_Fragments.cust_housekeeping.Class.Housekeeping
import com.example.mad_assignment.Customer_Fragments.cust_housekeeping.CustHousekeepingModel
import com.example.mad_assignment.R

class CustHousekeepingServiceFragment : Fragment() {

    private lateinit var custHousekeepingModel: CustHousekeepingModel

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.customer_fragment_housekeeping_service, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView: RecyclerView = view.findViewById(R.id.rv_housekeeping)
        recyclerView.layoutManager = GridLayoutManager(activity, 2)
        recyclerView.adapter = HousekeepingAdapter(ArrayList<Housekeeping>()) //Initialize adapter
        recyclerView.setHasFixedSize(true)

        //Get the viewmodel for housekeeping
        custHousekeepingModel = ViewModelProvider(this).get(CustHousekeepingModel::class.java)

        //Retrieve data from db
        custHousekeepingModel.retrieveHousekeepingFromDB()

        //Observe the housekeeping list and set it
        custHousekeepingModel.getHousekeepingList().observe(viewLifecycleOwner, Observer {
            recyclerView.adapter = HousekeepingAdapter(it)
        })

        //Hardcore db first (waiting staff housekeeping)
//        val database = FirebaseDatabase.getInstance()
//        val myRef = database.getReference("Housekeeping")
//
//        val housekeepingItem = Housekeeping("Bed Textiles",
//                "https://firebasestorage.googleapis.com/v0/b/quadcorehms-5b4ed.appspot.com/o/housekeeping%2FBed%20Textiles?alt=media&token=3bb64699-12d6-4ecf-bebc-45d5ae964165")
//
//        myRef.child(housekeepingItem.title).setValue(housekeepingItem)

//        val filePath: Uri = Uri.parse("res:///" + R.drawable.register_img)
//
//        val imageRef = FirebaseStorage.getInstance().getReference("Housekeeping")
//        imageRef.putFile(filePath)

    }
}