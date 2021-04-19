package com.example.mad_assignment.Staff.Staff_Fragments.staff_checkInOut.Main

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mad_assignment.Customer.Booking.Class.Reservation
import com.example.mad_assignment.R
import com.example.mad_assignment.Staff.Staff_Fragments.staff_checkInOut.Adapter.StaffCheckInOutAdapter
import com.example.mad_assignment.Staff.Staff_Fragments.staff_checkInOut.Model.StaffCheckInOutModel
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class StaffCheckOutFragment : Fragment(), DatePickerDialog.OnDateSetListener {

    private lateinit var staffCheckInOutModel: StaffCheckInOutModel
    private lateinit var parentLayout: ViewGroup
    private lateinit var parentElement: FrameLayout

    //Date
    private var year = 0
    private var month = 0
    private var day = 0

    // Search
    private lateinit var ivDate: ImageView
    private lateinit var tvDate: TextView
    private lateinit var etSearch: EditText
    private var checkInOutList = ArrayList<Reservation>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        parentLayout = container?.parent as ViewGroup
        parentElement = parentLayout.parent as FrameLayout

        return inflater.inflate(R.layout.staff_fragment_check_out, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView: RecyclerView = view.findViewById(R.id.rv_staff_check_out)

        etSearch = parentElement.findViewById(R.id.et_check_in_out_search)

        etSearch.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {

            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if(!etSearch.text.equals("")){
                    filter(s.toString(), recyclerView)
                }
            }
        })

        tvDate = parentElement.findViewById(R.id.tv_selectedDate)

        tvDate.setOnClickListener {
            pickDate()
        }

        tvDate.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {}

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            @RequiresApi(Build.VERSION_CODES.O)
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (!tvDate.text.equals("Select Date")) {
                    displayCheckOutRV(view, recyclerView)
                }
            }
        })

        if(tvDate.text != "Select Date"){
            displayCheckOutRV(view, recyclerView)
        }

        ivDate = parentElement.findViewById(R.id.iv_selectedDate)
        ivDate.setOnClickListener {
            pickDate()
        }
    }

    private fun displayCheckOutRV(view: View, recyclerView: RecyclerView){
        if(activity != null) {
            recyclerView.layoutManager = LinearLayoutManager(activity)
            recyclerView.adapter = StaffCheckInOutAdapter(ArrayList<Reservation>(), requireActivity()) //Initialize adapter
            recyclerView.setHasFixedSize(true)

            //Get the viewmodel for housekeeping requested
            staffCheckInOutModel = ViewModelProvider(this).get(StaffCheckInOutModel::class.java)

            //Retrieve data from db
            staffCheckInOutModel.retrieveCheckInOutFromDB("check in", tvDate.text.toString())

            //Observe the housekeeping list and set it
            staffCheckInOutModel.getCheckInOutList().observe(viewLifecycleOwner, Observer {
                recyclerView.adapter = StaffCheckInOutAdapter(it, requireActivity())
                checkInOutList = it
            })
        }
    }

    @SuppressLint("DefaultLocale")
    private fun filter(text: String, recyclerView: RecyclerView){
        if(activity != null) {
            val filteredList = ArrayList<Reservation>()

            for (i in checkInOutList) {
                if (i.custName!!.toLowerCase().contains(text.toLowerCase())) {
                    filteredList.add(i)
                }
            }

            recyclerView.adapter = StaffCheckInOutAdapter(filteredList, requireActivity())
        }
    }

    private fun pickDate(){
        getDateCalendar()

        val datePickerDialog = DatePickerDialog(this.requireContext(), this, year, month, day)
        datePickerDialog.window?.setFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE, WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE)

        datePickerDialog.show()

        datePickerDialog.window?.decorView?.systemUiVisibility = (View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION)

        datePickerDialog.window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE)
    }

    private fun getDateCalendar(){
        val cal: Calendar = Calendar.getInstance()
        year = cal.get(Calendar.YEAR)
        month = cal.get(Calendar.MONTH)
        day = cal.get(Calendar.DAY_OF_MONTH)
    }

    @SuppressLint("SetTextI18n", "SimpleDateFormat")
    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {

        val date = Date(year - 1900, month, dayOfMonth)
        val format: SimpleDateFormat = SimpleDateFormat("dd/MM/yyyy")

        tvDate.text = format.format(date)
    }

}