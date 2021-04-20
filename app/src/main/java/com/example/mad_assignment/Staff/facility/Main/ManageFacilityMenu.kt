package com.example.mad_assignment.Staff.facility.Main

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.mad_assignment.R
import com.example.mad_assignment.Staff.Staff_Fragments.StaffHomeFragment
import com.example.mad_assignment.Staff.facility.Adapter.FacilityMenuAdapter
import com.example.mad_assignment.Staff.facility.Class.Facility
import com.example.mad_assignment.Staff.facility.Model.FacilityViewModel
import kotlinx.android.synthetic.main.manage_facility_menu.*

class ManageFacilityMenu : AppCompatActivity() {

    private var facilityList = java.util.ArrayList<Facility>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.manage_facility_menu)

        var context = this
        //------------------------------------------------------
        //---------------------- Toolbar -----------------------
        //------------------------------------------------------

        //for toolbar
        val toolbar: Toolbar = findViewById(R.id.facility_menu_toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = null

        //back button
        val backButton = findViewById<ImageView>(R.id.manage_facility_menu_back_icon)
        backButton.setOnClickListener{
           // val intent = Intent(this, ManageFacilityMenu::class.java)
           // startActivity(intent)

            finish()
            //var fragment =  getFragmentManager().findFragmentById(R.id.staff_fragment_home) as (StaffHomeFragment)
            //fragment()
        }

        //------------------------------------------------------
        //--------------------- Add room------------------------
        //------------------------------------------------------

        btn_add_facility.setOnClickListener {
            //val intent = Intent(this, AddFacility::class.java)
            //startActivity(intent)
            if (fragmentManager.backStackEntryCount != 0) {
                fragmentManager.popBackStack()
            }
        }

        //------------------------------------------------------
        //------------------ Recycle view ----------------------
        //------------------------------------------------------

        rv_manage_facility.layoutManager = GridLayoutManager(this, 2)

        //Initialize adapter
        rv_manage_facility.adapter = FacilityMenuAdapter(ArrayList<Facility>(), this)
        rv_manage_facility.setHasFixedSize(true)

        //Get the view model for room type menu
        var facilityModel = ViewModelProvider(this).get(FacilityViewModel::class.java)

        //Retrieve data from db
        facilityModel.fetchFacilityData()

        //Observe the room type menu list and set it
        facilityModel.getFacility().observe(this, Observer {
            rv_manage_facility.adapter = FacilityMenuAdapter(it, this)
            facilityList = it
        })

        facilityModel.getStatus().observe(this, Observer {
            if (it == false) {
                Toast.makeText(this, "No data found", Toast.LENGTH_SHORT).show()
            }
        })

        //------------------------------------------------------
        //--------------------- Search -------------------------
        //------------------------------------------------------


        et_facility_search.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {
                if(!et_facility_search.text.equals("")){
                    filter(s.toString())
                }else{
                    rv_manage_facility.adapter = FacilityMenuAdapter(facilityList, context)
                }
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

            }
        })
    }

    @SuppressLint("DefaultLocale")
    private fun filter(text: String){
        val filteredList = java.util.ArrayList<Facility>()

        for (i in facilityList) {
            if (i.facilityName?.toLowerCase()?.contains(text.toLowerCase()) == true) {
                filteredList.add(i)
            }
        }

        rv_manage_facility.adapter = FacilityMenuAdapter(filteredList, this)
    }

}