package com.example.mad_assignment.Customer_Fragments.cust_housekeeping

import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mad_assignment.Customer.Customer_Fragments.cust_housekeeping.Class.Housekeeping
import com.example.mad_assignment.Customer.Customer_Fragments.cust_housekeeping.CustHousekeepingRequestFragment
import com.example.mad_assignment.Customer.Customer_Fragments.cust_housekeeping.CustHousekeepingServiceFragment
import com.example.mad_assignment.Customer.Customer_Fragments.cust_housekeeping.HousekeepingAdapter
import com.example.mad_assignment.R

//belongs to customer_fragment_housekeeping.xml
class CustHousekeepingFragment : Fragment() {

//    private lateinit var custHousekeepingModel: CustHousekeepingModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val root: View =  inflater.inflate(R.layout.customer_fragment_housekeeping, container, false)

        // Initialize the fragment
        val ft: FragmentTransaction = activity?.supportFragmentManager!!.beginTransaction()
        ft.replace(R.id.framel_Housekeeping, CustHousekeepingServiceFragment())
        ft.addToBackStack(null)
        ft.commit()

        val tvServices: TextView = root.findViewById(R.id.tv_houseKeeping_services)
        val tvRequested: TextView = root.findViewById(R.id.tv_houseKeeping_requested)

        tvServices.setOnClickListener {
            tvServices.setTextColor(ContextCompat.getColor(requireActivity(), R.color.black))
            tvServices.typeface = Typeface.DEFAULT_BOLD;

            tvRequested.setTextColor(ContextCompat.getColor(requireActivity(), R.color.gray))
            tvRequested.typeface = Typeface.DEFAULT;

            val ft: FragmentTransaction = activity?.supportFragmentManager!!.beginTransaction()
            ft.replace(R.id.framel_Housekeeping, CustHousekeepingServiceFragment())
            ft.addToBackStack(null)
            ft.commit()
        }

        tvRequested.setOnClickListener {
            tvRequested.setTextColor(ContextCompat.getColor(requireActivity(), R.color.black))
            tvRequested.typeface = Typeface.DEFAULT_BOLD;

            tvServices.setTextColor(ContextCompat.getColor(requireActivity(), R.color.gray))
            tvServices.typeface = Typeface.DEFAULT;

            val ft: FragmentTransaction = activity?.supportFragmentManager!!.beginTransaction()
            ft.replace(R.id.framel_Housekeeping, CustHousekeepingRequestFragment())
            ft.addToBackStack(null)
            ft.commit()
        }

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        val recyclerView: RecyclerView = view.findViewById(R.id.rv_housekeeping)
//        recyclerView.layoutManager = GridLayoutManager(activity, 2)
//        recyclerView.adapter = HousekeepingAdapter(ArrayList<Housekeeping>()) //Initialize adapter
//        recyclerView.setHasFixedSize(true)
//
//        //Get the viewmodel for housekeeping
//        custHousekeepingModel = ViewModelProvider(this).get(CustHousekeepingModel::class.java)
//
//        //Observe the housekeeping list and set it
//        custHousekeepingModel.getHousekeepingList().observe(viewLifecycleOwner, Observer {
//            recyclerView.adapter = HousekeepingAdapter(it)
//        })

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