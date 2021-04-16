package com.example.mad_assignment.Customer_Fragments.cust_home

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mad_assignment.Customer.Customer_Fragments.cust_home.CustHomeAdapter
import com.example.mad_assignment.Customer.Customer_Fragments.cust_home.Cust_Home_Item
//import com.example.mad_assignment.Customer.Customer_Fragments.cust_housekeeping.Main.CustHousekeepingFragment
//import com.example.mad_assignment.Customer_Fragments.cust_facilities.CustFacilitiesFragment
import com.example.mad_assignment.R
import kotlinx.android.synthetic.main.customer_fragment_home.*

//belongs to customer_fragment_home.xml
class CustHomeFragment : Fragment() {
    //temp variable
    var isbooking = true //false

    //private lateinit var custHomeViewModel: CustHomeViewModel
    private lateinit var recyclerView: RecyclerView
    private var home_items = ArrayList<Cust_Home_Item>()

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        //allow app bar
        (requireActivity() as AppCompatActivity).supportActionBar?.show()

        //custHomeViewModel =  ViewModelProvider(this).get(CustHomeViewModel::class.java)
        val root = inflater.inflate(R.layout.customer_fragment_home, container, false)

        home_items.add(Cust_Home_Item("Room", R.drawable.banner_hotelroom, "We have many room type which single rooms to quad rooms, queen rooms, king rooms, and president suite.", "Book Room >>"))
        home_items.add(Cust_Home_Item("Facilities", R.drawable.banner_hotelfacility, "There are many facilities such as spa, swimming pool, gym room and etc. Waiting you to enjoy it!", "Book Facility >>"))
        //need modify the 3rd der
        if(isbooking) {
            home_items.add(Cust_Home_Item("Services", R.drawable.banner_hotelroom, "Let's book our services! We will provide the best services to you.", "Book Services >>"))
        }

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        recyclerView = view.findViewById(R.id.rv_cust_home)
        recyclerView.layoutManager = LinearLayoutManager(context)//activity, 2)
        recyclerView.adapter = CustHomeAdapter(requireActivity(),home_items) //Initialize adapter

    }
}


