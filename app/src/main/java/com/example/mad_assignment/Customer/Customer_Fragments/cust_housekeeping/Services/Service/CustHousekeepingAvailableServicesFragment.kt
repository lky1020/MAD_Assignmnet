package com.example.mad_assignment.Customer.Customer_Fragments.cust_housekeeping.Services.Service

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mad_assignment.Class.User
import com.example.mad_assignment.Customer.Chat.messages.LatestMessages
import com.example.mad_assignment.Customer.Chat.messages.NewMessage
import com.example.mad_assignment.Customer.Customer_Fragments.cust_housekeeping.Adapter.AvailableLaundryServicesAdadpter
import com.example.mad_assignment.Customer.Customer_Fragments.cust_housekeeping.Adapter.AvailableRoomCleaningServicesAdadpter
import com.example.mad_assignment.Customer.Customer_Fragments.cust_housekeeping.Adapter.HousekeepingRequestedAdapter
import com.example.mad_assignment.Customer.Customer_Fragments.cust_housekeeping.Class.BookedHousekeepingService
import com.example.mad_assignment.Customer.Customer_Fragments.cust_housekeeping.Class.LaundryService
import com.example.mad_assignment.Customer.Customer_Fragments.cust_housekeeping.Class.RoomCleaningService
import com.example.mad_assignment.Customer.Customer_Fragments.cust_housekeeping.Model.CustAvailableHousekeepingServicesModel
import com.example.mad_assignment.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.customer_fragment_available_housekeeping_services.*
import java.text.SimpleDateFormat
import java.util.*


class CustHousekeepingAvailableServicesFragment(private var title: String, private var imageUrl: String) : Fragment(), DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    //Date
    private var year = 0
    private var month = 0
    private var day = 0

    //Time
    private var hour = 0
    private var minute = 0
    private var fromSelected = true

    private lateinit var servicesType: String
    private lateinit var imgUrl: String
    private lateinit var tvDate: TextView
    private lateinit var tvTimeFrom: TextView
    private lateinit var tvTimeTo: TextView
    private lateinit var ivDate: ImageView
    private lateinit var ivTimeFrom: ImageView
    private lateinit var ivTimeTo: ImageView

    private lateinit var custAvailableHousekeepingServicesModel: CustAvailableHousekeepingServicesModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var etServicesSeacrh: EditText
    private var roomCleaningList = ArrayList<RoomCleaningService>()
    private var laundryServiceList =  ArrayList<LaundryService>()

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val root: View = inflater.inflate(R.layout.customer_fragment_available_housekeeping_services, container, false)

        //Get the viewmodel for housekeeping
        custAvailableHousekeepingServicesModel = ViewModelProvider(this).get(
                CustAvailableHousekeepingServicesModel::class.java
        )

        custAvailableHousekeepingServicesModel.getStatus().observe(viewLifecycleOwner, Observer {
            if (it == false) {
                Toast.makeText(requireContext(), "No Services Available!", Toast.LENGTH_SHORT).show()
            }
        })

        //Get action bar title for retrieve data from db
        servicesType = title

        //Get the image from the housekeeping
        imgUrl = imageUrl

        etServicesSeacrh = root.findViewById(R.id.et_housekeeping_services_search)

        root.setOnTouchListener { v, event ->
            etServicesSeacrh.clearFocus()

            // Disable virtual k
            val imm = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
            imm!!.hideSoftInputFromWindow(etServicesSeacrh.windowToken, 0)

            true
        }

        return root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        etServicesSeacrh.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {
                if(!etServicesSeacrh.text.equals("") && (roomCleaningList.size > 0 || laundryServiceList.size > 0)){
                    filter(s.toString())
                }
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

            }
        })

        tvDate = view.findViewById(R.id.tv_selectedDate)
        tvDate.setOnClickListener {
            pickDate()
        }

        tvDate.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {}

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            @RequiresApi(Build.VERSION_CODES.O)
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (!tvTimeFrom.text.equals("From") && !tvTimeTo.text.equals("To")) {
                    displayAvailableServiceRV(view)
                }
            }
        })

        ivDate = view.findViewById(R.id.iv_selectedDate)
        ivDate.setOnClickListener {
            pickDate()
        }

        tvTimeFrom = view.findViewById(R.id.tv_selectedTimeFrom)
        tvTimeFrom.setOnClickListener {
            fromSelected = true
            pickTime()
        }

        tvTimeFrom.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {}

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            @RequiresApi(Build.VERSION_CODES.O)
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (!tv_selectedDate.text.equals("Select Date") && !tvTimeTo.text.equals("To")) {
                    displayAvailableServiceRV(view)
                }
            }
        })

        ivTimeFrom = view.findViewById(R.id.iv_selectedTimeFrom)
        ivTimeFrom.setOnClickListener {
            fromSelected = true
            pickTime()
        }

        tvTimeTo = view.findViewById(R.id.tv_selectedTimeTo)
        tvTimeTo.setOnClickListener {
            fromSelected = false
            pickTime()
        }

        tvTimeTo.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {}

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            @RequiresApi(Build.VERSION_CODES.O)
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (!tv_selectedDate.text.equals("Select Date") && !tvTimeFrom.text.equals("From")) {
                    displayAvailableServiceRV(view)
                }
            }
        })

        ivTimeTo = view.findViewById(R.id.iv_selectedTimeTo)
        ivTimeTo.setOnClickListener {
            fromSelected = false
            pickTime()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun displayAvailableServiceRV(view: View){
        recyclerView = view.findViewById(R.id.rv_housekeeping_available_services)
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.adapter = AvailableRoomCleaningServicesAdadpter(ArrayList<RoomCleaningService>(), requireActivity(), servicesType, imgUrl) //Initialize adapter
        recyclerView.setHasFixedSize(true)

        //Retrieve data from db
        val retrieveDate = tvDate.text.substring(5, 11) + " " + year
        val timeFromHour = tvTimeFrom.text.substring(0, 2)
        val timeFromMinute = tvTimeFrom.text.substring(3, 5)
        val timeToHour = tvTimeTo.text.substring(0, 2)
        val timeToMinute = tvTimeTo.text.substring(3, 5)

        if(servicesType == "Room Cleaning"){
            custAvailableHousekeepingServicesModel.retrieveHousekeepingServicesFromDB(servicesType, retrieveDate, timeFromHour.toInt(), timeFromMinute.toInt(), timeToHour.toInt(), timeToMinute.toInt())

            //Observe the housekeeping list and set it
            custAvailableHousekeepingServicesModel.getRoomCleaningServicesList().observe(viewLifecycleOwner, Observer {
                recyclerView.adapter = AvailableRoomCleaningServicesAdadpter(it, requireActivity(), servicesType, imgUrl)
                roomCleaningList = it
            })
        }
        else if(servicesType == "Laundry Service"){
            custAvailableHousekeepingServicesModel.retrieveHousekeepingServicesFromDB(servicesType, retrieveDate, timeFromHour.toInt(), timeFromMinute.toInt(), timeToHour.toInt(), timeToMinute.toInt())

            //Observe the housekeeping list and set it
            custAvailableHousekeepingServicesModel.getLaundryServicesList().observe(viewLifecycleOwner, Observer {
                recyclerView.adapter = AvailableLaundryServicesAdadpter(it, requireActivity(), tv_selectedDate.text.toString(), servicesType, imgUrl)
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

        tvDate.text = "$dayString, $monthString $dayOfMonth "
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

    private fun getTime(){
        val cal: Calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"))
        cal.timeZone = TimeZone.getTimeZone("Asia/Kuala_Lumpur");

        hour = cal.get(Calendar.HOUR_OF_DAY)
        minute = cal.get(Calendar.MINUTE)
    }

    private fun pickTime(){
        getTime()

        TimePickerDialog(requireContext(), this, hour, minute, true).show()
    }

    @SuppressLint("SetTextI18n")
    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {

        if(fromSelected){
            tvTimeFrom.text = String.format("%02d", hourOfDay) + ":" + String.format("%02d", minute)
        }else{
            tvTimeTo.text = String.format("%02d", hourOfDay) + ":" + String.format("%02d", minute)
        }
    }

    @SuppressLint("DefaultLocale")
    private fun filter(text: String){
        if(activity != null) {

            if(roomCleaningList.size > 0) {
                val filteredRoomList = ArrayList<RoomCleaningService>()

                if (text != "") {
                    for (i in roomCleaningList) {
                        if (text.toLowerCase()[0] == 'n') {
                            if (i.status == "Not Available") {
                                filteredRoomList.add(i)
                            }
                        } else if (text.toLowerCase()[0] == 'a') {
                            if (i.status == "Available") {
                                filteredRoomList.add(i)
                            }
                        }
                    }
                }else{
                    for (i in roomCleaningList) {
                        filteredRoomList.add(i)
                    }
                }

                recyclerView.adapter = AvailableRoomCleaningServicesAdadpter(filteredRoomList, requireActivity(), servicesType, imgUrl)

            }
            else{
                val filteredLaundryList = ArrayList<LaundryService>()

                if(text != ""){
                    for (i in laundryServiceList) {
                        if(text.toLowerCase()[0] == 'n'){
                            if (i.status == "Not Available") {
                                filteredLaundryList.add(i)
                            }
                        }else if(text.toLowerCase()[0] == 'a') {
                            if (i.status == "Available") {
                                filteredLaundryList.add(i)
                            }
                        }
                    }
                }else{
                    for (i in laundryServiceList) {
                        filteredLaundryList.add(i)
                    }
                }

                recyclerView.adapter = AvailableLaundryServicesAdadpter(filteredLaundryList, requireActivity(), tv_selectedDate.text.toString(), servicesType, imgUrl)

            }
        }
    }
}