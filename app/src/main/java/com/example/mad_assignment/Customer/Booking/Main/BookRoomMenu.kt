package com.example.mad_assignment.Customer.Booking.Main

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mad_assignment.Customer.Booking.Adapter.BookRoomAdapter
import com.example.mad_assignment.Customer.Booking.Class.RoomType
import com.example.mad_assignment.Customer.Booking.Model.BookRoomTypeMenuModel
import com.example.mad_assignment.R
import kotlinx.android.synthetic.main.book_room_menu.*
import java.text.SimpleDateFormat


class BookRoomMenu : AppCompatActivity() {

    private lateinit var bookRoomTypeMenuModel: BookRoomTypeMenuModel
    private var roomTypeList = java.util.ArrayList<RoomType>()
    private lateinit var recyclerView: RecyclerView
    private lateinit var etSearch: EditText
    private var checkedItem: Int = 0

    @SuppressLint("SetTextI18n", "ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.book_room_menu)


        var context = this

        //get from shared preferences
        val sharedPreferences = getSharedPreferences("myKey", MODE_PRIVATE)
        val startDate = sharedPreferences.getLong("startDate", 0)
        val endDate = sharedPreferences.getLong("endDate", 0)
        val guest = sharedPreferences.getInt("guest", 1)

        //set date and guest
        tv_brm_date_value.text = "${convertLongToDate(startDate)} to ${convertLongToDate(endDate)}"
        tv_brm_guest_value.text = "$guest guest"

        //set toolbar as support action bar
        val toolbar: Toolbar = findViewById(R.id.confirm_book_toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = null

        //back button
        val backButton = findViewById<ImageView>(R.id.confirm_book_back_icon)
        backButton.setOnClickListener{
            finish()
        }


        //recycle view
        recyclerView = findViewById(R.id.rv_room_select_item)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = BookRoomAdapter(ArrayList<RoomType>(), this) //Initialize adapter
        recyclerView.setHasFixedSize(true)

        //Get the view model for room type menu
        bookRoomTypeMenuModel = ViewModelProvider(this).get(BookRoomTypeMenuModel::class.java)

        //Retrieve data from db
        bookRoomTypeMenuModel.fetchBookRoomType()

        //Observe the room type menu list and set it
        bookRoomTypeMenuModel.getBookRoomTypeList().observe(this, Observer {
            recyclerView.adapter = BookRoomAdapter(it, this)
            roomTypeList = it
        })

        bookRoomTypeMenuModel.getStatus().observe(this, Observer {
            if (it == false) {
                Toast.makeText(this, "No data found", Toast.LENGTH_SHORT).show()
            }
        })

        //------------------------------------------------------
        //--------------------- Search -------------------------
        //------------------------------------------------------

        etSearch = findViewById(R.id.et_brm_search)

        etSearch.setOnKeyListener(View.OnKeyListener { view, i, keyEvent ->
            if (i == KeyEvent.KEYCODE_BACK) {
                etSearch.clearFocus()
                true
            } else false
        })


        etSearch.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {
                if (!etSearch.text.equals("")) {
                    filter(s.toString())
                } else {
                    recyclerView.adapter = BookRoomAdapter(roomTypeList, context)
                }
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

            }
        })

        //------------------------------------------------------
        //---------------------- Sort --------------------------
        //------------------------------------------------------

        ib_brm_filter.setOnClickListener {
            val alertDialog = AlertDialog.Builder(this)
            val filteredList = roomTypeList
            val items = arrayOf(
                "Name Ascending",
                "Name Descending",
                "Price Ascending",
                "Price Descending",
                "Occupancy Ascending",
                "Occupancy Descending"
            )

            alertDialog.setTitle("Sort By:")
                .setCancelable(false)
                .setPositiveButton("Sort") { dialog, _ ->
                    recyclerView.adapter = BookRoomAdapter(filteredList, this)
                    dialog.dismiss()
                }
                .setNegativeButton("No") { dialog, id ->
                    // Dismiss the dialog
                    dialog.dismiss()
                }
                .setSingleChoiceItems(
                    items, checkedItem
                ) { dialog, which ->
                    when (which) {
                        //Name Ascending
                        0 -> {
                            checkedItem = 0
                            filteredList.sortBy { it.roomType }
                        }
                        //Name Descending
                        1 ->{
                            checkedItem = 1
                            filteredList.sortByDescending { it.roomType }
                        }
                        //Price Ascending
                        2 -> {
                            checkedItem = 2
                            filteredList.sortBy { it.price }
                        }
                        //Price Descending
                        3 -> {
                            checkedItem = 3
                            filteredList.sortByDescending { it.price }
                        }
                        //Occupancy Ascending
                        4 -> {
                            checkedItem = 4
                            filteredList.sortBy { it.occupancy }
                        }
                        //Occupancy Descending
                        5 ->{
                            checkedItem = 5
                            filteredList.sortByDescending { it.occupancy }
                        }
                    }
            }
            val alert = alertDialog.create()
            alert.setCanceledOnTouchOutside(false)
            alert.show()

        }

    }


    @SuppressLint("DefaultLocale")
    private fun filter(text: String){
        val filteredList = java.util.ArrayList<RoomType>()

        for (i in roomTypeList) {
            if (i.roomType?.toLowerCase()?.contains(text.toLowerCase()) == true) {
                filteredList.add(i)
            }
        }

        recyclerView.adapter = BookRoomAdapter(filteredList, this)
    }


    @SuppressLint("SimpleDateFormat")
    private fun convertLongToDate(date: Long?): String {
        val format:SimpleDateFormat = SimpleDateFormat("dd MMM")
        return format.format(date)
    }
}