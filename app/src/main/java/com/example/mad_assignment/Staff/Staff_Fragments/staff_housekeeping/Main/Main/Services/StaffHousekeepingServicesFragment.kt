package com.example.mad_assignment.Staff.Staff_Fragments.staff_housekeeping.Main.Main.Services

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.ContentFrameLayout
import androidx.fragment.app.FragmentContainerView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mad_assignment.Customer.Customer_Fragments.cust_housekeeping.Adapter.HousekeepingRequestedAdapter
import com.example.mad_assignment.Customer.Customer_Fragments.cust_housekeeping.Adapter.HousekeepingServicesAdapter
import com.example.mad_assignment.Customer.Customer_Fragments.cust_housekeeping.Class.BookedHousekeepingService
import com.example.mad_assignment.Customer.Customer_Fragments.cust_housekeeping.Class.Housekeeping
import com.example.mad_assignment.Customer.Customer_Fragments.cust_housekeeping.Class.LaundryService
import com.example.mad_assignment.Customer.Customer_Fragments.cust_housekeeping.Class.RoomCleaningService
import com.example.mad_assignment.Customer.Customer_Fragments.cust_housekeeping.Model.CustHousekeepingServicesModel
import com.example.mad_assignment.R
import com.example.mad_assignment.Staff.Staff_Fragments.staff_housekeeping.Main.Adapter.StaffLaundryServicesAdadpter
import com.example.mad_assignment.Staff.Staff_Fragments.staff_housekeeping.Main.Adapter.StaffRoomCleaningServicesAdadpter
import com.example.mad_assignment.Staff.Staff_Fragments.staff_housekeeping.Main.Model.StaffHousekeepingAvailableServicesModel
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.staff_add_services_dialog.*
import kotlinx.android.synthetic.main.staff_add_services_dialog.view.*
import java.text.SimpleDateFormat
import java.time.LocalTime
import java.util.*
import kotlin.collections.ArrayList

class StaffHousekeepingServicesFragment(private val title: String) : Fragment(), DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener  {

    private lateinit var staffHousekeepingAvailableServicesModel: StaffHousekeepingAvailableServicesModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var parentLayout: ViewGroup
    private lateinit var parentElement: ContentFrameLayout

    private lateinit var servicesType: String
    private lateinit var etSearch: EditText
    private var roomCleaningList = java.util.ArrayList<RoomCleaningService>()
    private var laundryServiceList = java.util.ArrayList<LaundryService>()

    //Date
    private lateinit var selectedDate: TextView
    private var year = 0
    private var month = 0
    private var day = 0

    //Time
    private lateinit var selectedFrom: TextView
    private lateinit var selectedTo: TextView
    private var hour = 0
    private var minute = 0
    private var fromSelected = true

    private lateinit var btnAddService: Button

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        parentLayout = container?.parent as ViewGroup
        parentElement = parentLayout.parent as ContentFrameLayout

        val root: View = inflater.inflate(R.layout.staff_fragment_housekeeping_services, container, false)

        //Get the viewmodel for housekeeping
        staffHousekeepingAvailableServicesModel = ViewModelProvider(this).get(StaffHousekeepingAvailableServicesModel::class.java)

        staffHousekeepingAvailableServicesModel.getStatus().observe(viewLifecycleOwner, Observer {
            if (it == false) {
                Toast.makeText(requireContext(), "No Services Available!", Toast.LENGTH_SHORT).show()
            }
        })

        //Get action bar title for retrieve data from db
        servicesType = title

        etSearch = parentElement.findViewById(R.id.et_staff_housekeeping_services_search)

        etSearch.hint = "Search Date Here: Apr 17 2021"

        root.setOnTouchListener { v, event ->
            etSearch.clearFocus()

            // Disable virtual k
            val imm = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
            imm!!.hideSoftInputFromWindow(etSearch.windowToken, 0)

            true
        }

        //Set the button
        btnAddService = parentElement.findViewById(R.id.btn_add_service)

        return root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        etSearch.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {
                if(!etSearch.text.equals("") && etSearch.text.length >= 10 && etSearch.text.length <= 11){
                    displayAvailableServiceRV(view)
                }
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

            }
        })

        //Add service
        btnAddService.setOnClickListener {
            // Inflate the dialog
            val addDialogView = LayoutInflater.from(requireContext()).inflate(R.layout.staff_add_services_dialog, null)

            addDialogView.tv_add_date.text = "Date: "
            addDialogView.tv_add_time.text = "Time: "

            //Alert dialog builder
            val mBuilder = AlertDialog.Builder(requireContext())
                .setView(addDialogView)
                .setTitle("Add $servicesType")

            //show dialog
            val mAlertDialog = mBuilder.show()

            //Pick Date
            selectedDate = addDialogView.tv_add_selectDate
            selectedDate.setOnClickListener {
                pickDate()
            }
            addDialogView.iv_add_selectDate.setOnClickListener {
                pickDate()
            }

            //Pick Time - From
            selectedFrom = addDialogView.tv_selectedTimeFrom
            selectedFrom.setOnClickListener {
                fromSelected = true
                pickTime()
            }
            addDialogView.iv_selectedTimeFrom.setOnClickListener {
                fromSelected = true
                pickTime()
            }

            //Pick Time - To
            selectedTo = addDialogView.tv_selectedTimeTo
            selectedTo.setOnClickListener {
                fromSelected = false
                pickTime()
            }
            addDialogView.iv_selectedTimeTo.setOnClickListener {
                fromSelected = false
                pickTime()
            }

            //Button Action
            addDialogView.btn_add_cancel.setOnClickListener{
                mAlertDialog.dismiss()
            }

            addDialogView.btn_add_add.setOnClickListener{

                if(selectedDate.text != "Select Date" && selectedFrom.text != "From" && selectedTo.text != "To"){

                    mAlertDialog.dismiss()

                    if(servicesType == "Room Cleaning"){
                        updateRoomCleaning()
                    }

                    Toast.makeText(requireActivity(), "Added Success", Toast.LENGTH_SHORT).show()
                }else{
                    Toast.makeText(requireActivity(), "Invalid Input", Toast.LENGTH_SHORT).show()
                }
            }
        }

    }

    private fun displayAvailableServiceRV(view: View){
        recyclerView = view.findViewById(R.id.rv_staff_housekeeping_services)
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.adapter = StaffRoomCleaningServicesAdadpter(ArrayList<RoomCleaningService>(), requireActivity()) //Initialize adapter
        recyclerView.setHasFixedSize(true)

        if(servicesType == "Room Cleaning"){
            //Retrieve data from db
            staffHousekeepingAvailableServicesModel.retrieveHousekeepingServicesFromDB("Room Cleaning", etSearch.text.toString())

            //Observe the housekeeping list and set it
            staffHousekeepingAvailableServicesModel.getRoomCleaningServicesList().observe(viewLifecycleOwner, Observer {
                recyclerView.adapter = StaffRoomCleaningServicesAdadpter(it, requireActivity())
                roomCleaningList = it
            })
        }else{
            //Retrieve data from db
            staffHousekeepingAvailableServicesModel.retrieveHousekeepingServicesFromDB("Laundry Service", etSearch.text.toString())

            //Observe the housekeeping list and set it
            staffHousekeepingAvailableServicesModel.getLaundryServicesList().observe(viewLifecycleOwner, Observer {
                recyclerView.adapter = StaffLaundryServicesAdadpter(it, requireActivity())
                laundryServiceList = it
            })
        }
    }

    private fun pickDate(){
        getDateCalendar()

        DatePickerDialog(this.requireContext(), this, year, month, day).show()
    }

    private fun getDateCalendar(){
        val cal: Calendar = Calendar.getInstance()
        year = cal.get(Calendar.YEAR)
        month = cal.get(Calendar.MONTH)
        day = cal.get(Calendar.DAY_OF_MONTH)
    }

    @SuppressLint("SetTextI18n", "SimpleDateFormat")
    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {

        val simpleDateFormat = SimpleDateFormat("EEEE")
        val date = Date(year, month, dayOfMonth - 1)
        val dayString = simpleDateFormat.format(date).substring(0, 3)

        val monthString = convertMonth(month)

        selectedDate.text = "$dayString, $monthString $dayOfMonth"
    }

    private fun convertMonth(month: Int): String{
        when (month){
            0 -> return "Jan"
            1 -> return "Feb"
            2 -> return "Mar"
            3 -> return "Apr"
            4 -> return "May"
            5 -> return "Jun"
            6 -> return "Jul"
            7 -> return "Aug"
            8 -> return "Sep"
            9 -> return "Oct"
            10 -> return "Nov"
            11 -> return "Dec"
        }

        return ""
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getTime(){
        val cal: Calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"))
        cal.timeZone = TimeZone.getTimeZone("Asia/Kuala_Lumpur");

        hour = cal.get(Calendar.HOUR_OF_DAY)
        minute = cal.get(Calendar.MINUTE)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun pickTime(){

        getTime()

        TimePickerDialog(requireContext(), this, hour, minute, true).show()
    }

    @SuppressLint("SetTextI18n")
    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {

        if(fromSelected){
            selectedFrom.text = String.format("%02d", hourOfDay) + ":" + String.format("%02d", minute)

        }else{
            selectedTo.text = String.format("%02d", hourOfDay) + ":" + String.format("%02d", minute)
        }
    }

    private fun updateRoomCleaning(){

        val retrieveDate = selectedDate.text.substring(5, 11) + " " + year

        val ref: DatabaseReference = FirebaseDatabase.getInstance().getReference("Housekeeping")
            .child(servicesType).child("ServicesAvailable").child(retrieveDate)

        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            @RequiresApi(Build.VERSION_CODES.O)
            override fun onDataChange(snapshot: DataSnapshot) {
                var lastItem = 0

                if (snapshot.exists()) {

                    for (i in snapshot.children) {
                        //At last, it will be a new index to add into the db
                        lastItem += 1
                    }
                }

                //change 24 hour to 12 hour
                val timeFromHour = selectedFrom.text.substring(0, 2)
                val timeFromMinute = selectedFrom.text.substring(3, 5)
                val timeFromSeason = ""
                val timeToHour = selectedTo.text.substring(0, 2)
                val timeToMinute = selectedTo.text.substring(3, 5)
                val timeToSeason = ""

                val timeFromList = mutableListOf<Any>(timeFromHour, timeFromMinute, timeFromSeason)
                val timeToList = mutableListOf<Any>(timeToHour, timeToMinute, timeToSeason)

                val dbTimeFrom = initTime(timeFromList)
                val dbTimeTo = initTime(timeToList)

                //Create new room cleaning class and add to the list
                val newRoomCleaning = RoomCleaningService(retrieveDate, dbTimeFrom, dbTimeTo, "Available")

                //Update to db
                val updateRef = FirebaseDatabase.getInstance().getReference("Housekeeping").child("Room Cleaning").child("ServicesAvailable").child(retrieveDate)
                updateRef.child(lastItem.toString()).setValue(newRoomCleaning)

            }
            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun initTime(time: MutableList<Any>): String{

        // Change hour to 12 hour
        if(time[0].toString().toInt() >= 12){

            if(time[0].toString().toInt() != 12){
                time[0] = time[0].toString().toInt() - 12
            }

            time[2] = "PM"
        }
        else{
            time[2] = "AM"
        }

        if(time[1].toString().toInt() in 1..30){
            time[1] = 30
        }
        else if(time[1].toString().toInt() == 0){
            time[1] = 0
        }
        else{
            time[0] = time[0].toString().toInt() + 1
            time[1] = 0
        }

        return String.format("%02d", time[0].toString().toInt()) + ":"+ String.format("%02d", time[1].toString().toInt()) + " " + time[2].toString()

    }
}