package com.example.mad_assignment.Customer.Booking.Main

import android.annotation.SuppressLint
import android.os.Bundle
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
import java.util.*
import kotlin.collections.ArrayList


class BookRoomMenu : AppCompatActivity() {

    private lateinit var bookRoomTypeMenuModel: BookRoomTypeMenuModel

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.book_room_menu)


        //get from shared preferences
        val sharedPreferences = getSharedPreferences("myKey", MODE_PRIVATE)
        val startDate = sharedPreferences.getLong("startDate",0)
        val endDate = sharedPreferences.getLong("endDate",0)
        val guest = sharedPreferences.getInt("guest",1)

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


        // Create slider image component
        //val brm_sliderLayout = SliderImage(this)

        // add images URLs
      //  val images = listOf(
      //      "https://static01.nyt.com/images/2019/03/24/travel/24trending-shophotels1/24trending-shophotels1-jumbo.jpg?quality=90&auto=webp",
     //       "https://blisssaigon.com/wp-content/uploads/2019/10/Untitled.jpg",
       // )

        // Add image URLs to sliderImage
        //brm_sliderLayout.setItems(images)

        // Add slider component to a container
        //layout_brm_item.addView(brm_sliderLayout)


        //recycle view
        val recyclerView: RecyclerView = findViewById(R.id.rv_room_select_item)
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
        })

        bookRoomTypeMenuModel.getStatus().observe(this, Observer {
            if (it == false) {
                Toast.makeText(this, "No data found", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun convertLongToDate(date: Long?): String {
        val format:SimpleDateFormat = SimpleDateFormat("dd MMM")
        return format.format(date)
    }
}