package com.example.mad_assignment.Customer.Booking.Main

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.mad_assignment.R
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.booking_search_page.*
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit


@Suppress("CAST_NEVER_SUCCEEDS")
class BookingSearchPage : AppCompatActivity() {

    private var startDate: Long = 0
    private var endDate: Long = 0
    private var daysDiff: Long = 0

    @SuppressLint("SimpleDateFormat", "SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.booking_search_page)

        //back button
        booking_search_back_icon.setOnClickListener{
            finish()
        }

        //Next button
        btn_bs_next.setOnClickListener {

            val checkStart = Date(startDate)
            val now = Calendar.getInstance()
           // now.add(Calendar.DATE,

            //validation
            when {
                btn_prefer_date.text.toString().toLowerCase(Locale.ROOT) == "pick your date" -> {
                    Toast.makeText(this, "Please select your date", Toast.LENGTH_LONG).show()
                }
                checkStart.before(now.time) -> {
                    Toast.makeText(this, "Please select now or future dates", Toast.LENGTH_LONG).show()
                }
                else -> {
                    val sharedPref = getSharedPreferences("myKey", MODE_PRIVATE)
                    val editor = sharedPref.edit()
                    editor.putLong("startDate", startDate)
                    editor.putLong("endDate", endDate)
                    editor.putLong("night", daysDiff)
                    editor.putInt("guest", tv_bs_guest.text.toString().toInt())
                    editor.apply()

                    val intent = Intent(this, BookRoomMenu::class.java)
                    this.startActivity(intent)
                }
            }


        }

        //Calender
        btn_prefer_date.setOnClickListener {

            val builder = MaterialDatePicker.Builder.dateRangePicker().setTheme(R.style.ThemeOverlay_MaterialComponents_MaterialCalendar)

            val picker = builder.build()

            picker.show(supportFragmentManager, picker.toString())

            picker.addOnPositiveButtonClickListener{

                //Get the selected DATE RANGE
                startDate = it.first!!
                endDate = it.second!!

                val msDiff = endDate - startDate
                daysDiff = TimeUnit.MILLISECONDS.toDays(msDiff)

                btn_prefer_date.text = convertLongToDate(startDate) + " - "+ convertLongToDate(
                    endDate
                )
            }

        }

        //Guest control
        iv_bs_plus.setOnClickListener { increaseInteger() }
        iv_bs_minus.setOnClickListener { decreaseInteger() }


    }

    @SuppressLint("SimpleDateFormat")
    private fun convertLongToDate(time: Long): String {
        val date = Date(time)
        val format = SimpleDateFormat("dd/MM/yyyy")
        return format.format(date)
    }

    private fun increaseInteger() {
        display(tv_bs_guest.text.toString().toInt() + 1)
    }

    private fun decreaseInteger() {
        //if greater than 1, decrease
        if(tv_bs_guest.text.toString().toInt() > 1)
            display(tv_bs_guest.text.toString().toInt() - 1)
    }

    private fun display(number: Int) {
        tv_bs_guest.text = "$number"
    }
}