package com.example.mad_assignment.Customer.Customer_Fragments.cust_housekeeping.Class

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.example.mad_assignment.Class.User
import com.example.mad_assignment.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.text.SimpleDateFormat
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*

class HousekeepingBottomSheetFragment(private val currentItem: HousekeepingItem, private val servicesType: String, private var imageUrl: String): BottomSheetDialogFragment() {

    private lateinit var tvTitle: TextView
    private lateinit var tvQuantity: TextView
    private var dismissMessage = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.housekeeping_item_bottom_sheet_fragment, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tvTitle = view.findViewById(R.id.tv_bottom_sheet_title)
        tvQuantity = view.findViewById(R.id.tv_bottom_sheet_item_quantity)
        val ivMinus: ImageView = view.findViewById(R.id.iv_bottom_sheet_minus)
        val ivPlus: ImageView = view.findViewById(R.id.iv_bottom_sheet_plus)
        val btnCancel: Button = view.findViewById(R.id.btn_cancel_quantity)
        val btnOrder: Button = view.findViewById(R.id.btn_order_quantity)

        tvTitle.text = currentItem.title
        tvQuantity.text = "1"

        //Button
        ivMinus.setOnClickListener {
            if(tvQuantity.text.toString().toInt() > 1){
                tvQuantity.text = (tvQuantity.text.toString().toInt() - 1).toString()
            }else{
                Toast.makeText(requireActivity(), "Quantity cannot be 0!", Toast.LENGTH_SHORT).show()
            }
        }

        ivPlus.setOnClickListener {
            if(tvQuantity.text.toString().toInt() < 3){
                tvQuantity.text = (tvQuantity.text.toString().toInt() + 1).toString()
            }else{
                Toast.makeText(requireActivity(), "Cannot order more than 3 Quantity", Toast.LENGTH_SHORT).show()
            }
        }

        btnCancel.setOnClickListener {
            dismissMessage = ""
            dismiss()
        }

        btnOrder.setOnClickListener {
            if(tvQuantity.text.toString().toInt() <= currentItem.quantity){
                val estimateReceivedDate = getDateCalendar()
                val estimateReceivedTime = getTime()
                val estimateReceived = "$estimateReceivedDate, $estimateReceivedTime"
                orderItemForUser(estimateReceived)
                updateItemQuantity(currentItem.quantity - tvQuantity.text.toString().toInt())
                dismissMessage = "Item Ordered"
                dismiss()
            }else{
                Toast.makeText(requireContext(), "Sorry, Lack of Stock!", Toast.LENGTH_SHORT).show()
            }

        }
    }

    private fun orderItemForUser(estimateReceived: String){
        val uid = FirebaseAuth.getInstance().uid
        val ref = FirebaseDatabase.getInstance().getReference("/User/$uid")

        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            @RequiresApi(Build.VERSION_CODES.O)
            override fun onDataChange(snapshot: DataSnapshot) {
                val currentUser = snapshot.getValue(User::class.java)!!

                //Set unique id for the child
                val timeZone: ZoneId = ZoneId.of("Asia/Kuala_Lumpur")
                val now: LocalTime = LocalTime.now(timeZone)
                val dtf: DateTimeFormatter = DateTimeFormatter.ofPattern("hh:mm:ss a")

                val bookedTime = estimateReceived.substring(0, 13) + now.format(dtf)

                // Order item for User
                val myRef = FirebaseDatabase.getInstance().getReference("Housekeeping").child(servicesType).child("ItemOrdered").child(currentUser.name + " - " + bookedTime)
                val itemInfo = HousekeepingOrderedItem(
                        tvTitle.text.toString(),
                        imageUrl,
                        tvQuantity.text.toString().toInt(),
                        bookedTime,
                        estimateReceived,
                        servicesType,
                        currentUser.name)

                myRef.setValue(itemInfo)
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }

    private fun updateItemQuantity(quantity: Int){
        val myRef = FirebaseDatabase.getInstance().getReference("Housekeeping")
                .child(servicesType).child("ItemAvailable")

        myRef.addListenerForSingleValueEvent(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (i in snapshot.children) {
                        val currentSelectedItem = i.getValue(HousekeepingItem::class.java)

                        val updateItem = HousekeepingItem(
                                currentItem.title,
                                currentItem.img,
                                quantity
                        )

                        myRef.child(currentItem.title).setValue(updateItem)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

    @SuppressLint("SimpleDateFormat")
    private fun getDateCalendar(): String{
        val cal: Calendar = Calendar.getInstance()
        val year = cal.get(Calendar.YEAR)
        val month = cal.get(Calendar.MONTH)
        val day = cal.get(Calendar.DAY_OF_MONTH)

        val simpleDateFormat = SimpleDateFormat("EEEE")
        val date = Date(year, month, day - 1)
        val dayString = simpleDateFormat.format(date).substring(0, 3)

        val monthString = convertMonth(month)

        return "$dayString, $monthString $day"
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
    private fun getTime(): String{
        val cal: Calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"))
        cal.timeZone = TimeZone.getTimeZone("Asia/Kuala_Lumpur")

        val hour = cal.get(Calendar.HOUR_OF_DAY)
        val minute = cal.get(Calendar.MINUTE)
        val session = ""

        val estimateTimeList = mutableListOf<Any>(hour + 2, minute, session)

        return initTime(estimateTimeList)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun initTime(estimateTimeList: MutableList<Any>): String{

        // Change hour to 12 hour
        if(estimateTimeList[0].toString().toInt() >= 12){
            if(estimateTimeList[0].toString().toInt() != 12){
                estimateTimeList[0] = estimateTimeList[0].toString().toInt() - 12
            }

            estimateTimeList[2] = "PM"
        }
        else{
            estimateTimeList[2] = "AM"
        }

        if(estimateTimeList[1].toString().toInt() in 1..30){
            estimateTimeList[1] = 30
        }
        else if(estimateTimeList[1].toString().toInt() == 0){
            estimateTimeList[1] = 0
        }
        else{
            estimateTimeList[0] = estimateTimeList[0].toString().toInt() + 1
            estimateTimeList[1] = 0
        }

        return String.format("%02d", estimateTimeList[0]) + ":"+ String.format("%02d", estimateTimeList[1]) + " " + estimateTimeList[2].toString()

    }

    override fun dismiss() {
        super.dismiss()

        if(dismissMessage != ""){
            Toast.makeText(requireContext(), dismissMessage, Toast.LENGTH_SHORT).show()
        }

    }
}