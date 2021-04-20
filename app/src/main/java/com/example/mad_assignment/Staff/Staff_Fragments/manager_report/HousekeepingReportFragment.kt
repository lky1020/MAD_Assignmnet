package com.example.mad_assignment.Staff.Staff_Fragments.manager_report

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.example.mad_assignment.R
import com.google.firebase.database.*
import java.time.LocalDate

class HousekeepingReportFragment : Fragment() {

    private lateinit var tvTotalBedTextile:TextView
    private lateinit var tvTotalLaundryService:TextView
    private lateinit var tvTotalRoomCleaning:TextView
    private lateinit var tvTotalToiletries:TextView


    @SuppressLint("SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?,
    ): View? {
        val root: View = inflater.inflate(R.layout.fragment_housekeeping_report, container, false)

        val tvTitle: TextView = root.findViewById(R.id.tv_title)
        val tvGenTitle: TextView = root.findViewById(R.id.tv_date_genTitle)
        val tvReportName: TextView = root.findViewById(R.id.tv_report_name)
        val tvDateGen: TextView = root.findViewById(R.id.tv_date_gen)

        tvTotalBedTextile = root.findViewById(R.id.tv_total_bed_textile)
        tvTotalLaundryService = root.findViewById(R.id.tv_total_laundry_servce)
        tvTotalRoomCleaning = root.findViewById(R.id.tv_total_room_cleaning)
        tvTotalToiletries = root.findViewById(R.id.tv_total_toiletries)

        //get report name
        tvTitle.text = "Title : "
        tvGenTitle.text = "Date  : "
        val year:String = LocalDate.now().year.toString()
        val month:String = LocalDate.now().month.toString()
        tvReportName.text = "$month $year HOUSEKEEPING REPORT"

        tvDateGen.text = LocalDate.now().toString()

        fetchHousekeepingData()

        return root
    }

    private fun fetchHousekeepingData(){
        fetchItemOrdered("Bed Textiles")

        fetchServiceRequested("Laundry Service")

        fetchServiceRequested("Room Cleaning")

        fetchItemOrdered("Toiletries")

    }

    private fun fetchItemOrdered(serviceType: String){

        var itemOrdered = 0

        val ref: DatabaseReference = FirebaseDatabase.getInstance().getReference("Housekeeping")
            .child(serviceType).child("ItemOrdered")

        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){

                    for (i in snapshot.children) {
                        itemOrdered++
                    }
                }

                if(serviceType == "Bed Textiles"){
                    tvTotalBedTextile.text = itemOrdered.toString()

                }else{
                    tvTotalToiletries.text = itemOrdered.toString()
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

    private fun fetchServiceRequested(servicesType: String){

        var serviceOrdered = 0

        val query: Query = FirebaseDatabase.getInstance().getReference("Housekeeping")
            .child(servicesType).child("ServicesBooked")

        //Check got data or not
        query.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {

                    for(i in snapshot.children){
                        serviceOrdered++
                    }

                }

                if(servicesType == "Room Cleaning"){
                    tvTotalRoomCleaning.text = serviceOrdered.toString()

                }else{
                    tvTotalLaundryService.text = serviceOrdered.toString()
                }

            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }
}