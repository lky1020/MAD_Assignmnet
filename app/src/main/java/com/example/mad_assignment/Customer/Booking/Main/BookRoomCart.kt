package com.example.mad_assignment.Customer.Booking.Main

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mad_assignment.Customer.Booking.Adapter.BookRoomCartAdapter
import com.example.mad_assignment.Customer.Booking.Class.ReservationDetail
import com.example.mad_assignment.Customer.Booking.Class.RoomType
import com.example.mad_assignment.R
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.book_room_cart.*
import kotlinx.android.synthetic.main.cust_add_selected_room.view.*
import java.lang.reflect.Type
import java.text.SimpleDateFormat


class BookRoomCart : AppCompatActivity() {

    private var selectedRoomList: ArrayList<ReservationDetail>? = ArrayList<ReservationDetail>()
    lateinit var roomTypeList: ArrayList<RoomType>

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.book_room_cart)

        //get from shared preferences
        val sharedPreferences = getSharedPreferences("myKey", MODE_PRIVATE)
        val startDate = convertLongToDate(sharedPreferences.getLong("startDate", 0))
        val endDate = convertLongToDate(sharedPreferences.getLong("endDate", 0))
        val guest = sharedPreferences.getInt("guest", 1)
        val nights = sharedPreferences.getLong("night", 0).toInt()

        //set date & guest
        tv_brc_date.text = "$startDate to $endDate"
        tv_brc_guest.text = "$guest guest"

        //set toolbar as support action bar
        setSupportActionBar(confirm_book_toolbar)
        supportActionBar?.title = null

        //back button
        confirm_book_back_icon.setOnClickListener{
            finish()
        }

        //retrieve data from previous page
        val gson = Gson()
        val selectedRoom = gson.fromJson(intent.getStringExtra("selectedRoom"), ReservationDetail::class.java)

        val groupListType: Type = object : TypeToken<ArrayList<RoomType?>?>() {}.type
        roomTypeList = gson.fromJson(intent.getStringExtra("roomTypeList"), groupListType)

        selectedRoomList = ArrayList()
        selectedRoomList!!.add(selectedRoom)


        //recycle view
        val recyclerView: RecyclerView = findViewById(R.id.rv_cart_item)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter =
            selectedRoomList?.let { BookRoomCartAdapter(this.selectedRoomList!!, this, nights) } //Initialize adapter
        recyclerView.setHasFixedSize(true)

        //---------------------------------------------------------------------
        //---------------------  redirect confirm page  -----------------------
        //---------------------------------------------------------------------
        btn_brc_reserve.setOnClickListener {
            //pass selected room to next activity
            if(selectedRoomList!!.size == 0){
                Toast.makeText(applicationContext, "Please select a room before proceed to payment", Toast.LENGTH_LONG).show()
            }else{
                val gson = Gson()
                val intent = Intent(this, ConfirmBooking::class.java)
                intent.putExtra("selectedRoomList", gson.toJson(selectedRoomList))
                startActivity(intent)
            }

        }

        //---------------------------------------------------------------------
        //----------------  display the dialog for add room -------------------
        //---------------------------------------------------------------------
        btn_brc_add.setOnClickListener {

            var selectedRoomType: String? = null
            var selectedQty: Int? = 0


            //Inflate the dialog with custom view
            val mDialogView = LayoutInflater.from(this).inflate(R.layout.cust_add_selected_room, null)

            //AlertDialogBuilder
            val mBuilder = AlertDialog.Builder(this)
                    .setView(mDialogView)
                    .setTitle("Add Room")

            //show dialog
            val  mAlertDialog = mBuilder.show()

            //Room Type Spinner
            mDialogView.spinner_cust_add_room_type.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                    selectedRoomType = parent.getItemAtPosition(position).toString()
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            })

            //Quantity Spinner
            mDialogView.spinner_cust_add_room_qty.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                    selectedQty = parent?.selectedItemPosition!! + 1
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            })

            //--------------------------------------------------
            //----------------  ADD FUNCTION -------------------
            //--------------------------------------------------
            mDialogView.btn_cust_add_room_add.setOnClickListener {
                //dismiss dialog
                mAlertDialog.dismiss()

                var status = true
                var checkExist = false

             //   try{
                    Log.d("book",selectedRoomType.toString())

                    if(!selectedRoomType.equals("Select Room Type")){
                        //Add
                            if(selectedRoomList!!.size != 0){
                                for(item in this.selectedRoomList!!){

                                    //if exist same room type, update it
                                    if(item.roomType?.roomType.equals(selectedRoomType)){
                                        checkExist = true
                                        var updatedQty = selectedQty?.plus(item.qty)

                                        if (updatedQty != null) {

                                            if(updatedQty <= 10){

                                                item.qty = updatedQty
                                                recyclerView.adapter?.notifyDataSetChanged()
                                                break
                                            }else{

                                                //display error message
                                                status= false
                                                Toast.makeText(applicationContext, "Only maximum 10 rooms can be added", Toast.LENGTH_SHORT).show()
                                                break
                                            }

                                        }
                                    }

                                }
                                if(!checkExist){
                                    for(room in roomTypeList){
                                        if(room.roomType.equals(selectedRoomType)){
                                            val subtotal: Double? = (room.price?.times(nights) )?.times(selectedQty!!)
                                            val newRoom = ReservationDetail(room, selectedQty!!, subtotal)
                                            selectedRoomList!!.add(0,newRoom)
                                            recyclerView.adapter?.notifyDataSetChanged()
                                        }
                                    }
                                }
                            }else{


                                //add new room
                                for(room in roomTypeList){
                                    if(room.roomType.equals(selectedRoomType)){
                                        val subtotal: Double? = (room.price?.times(nights) )?.times(selectedQty!!)
                                        val newRoom = ReservationDetail(room, selectedQty!!, subtotal)
                                      //  selectedRoomList = ArrayList()
                                        var newlist: ArrayList<ReservationDetail>? = ArrayList()
                                        newlist!!.add(newRoom)
                                        selectedRoomList = newlist
                                        recyclerView.adapter =
                                            selectedRoomList?.let { BookRoomCartAdapter(this.selectedRoomList!!, this, nights) }
                                        //recyclerView.adapter?.notifyDataSetChanged()
                                    }
                                }
                            }

                    }else{
                        status= false
                        Toast.makeText(applicationContext, "Please select room type", Toast.LENGTH_SHORT).show()
                    }
            //    }
            //    catch (ex: Exception) {
           //         Log.e("Exception", "$ex")
           //     }

                //display successfully message
                if(status)
                    Toast.makeText(applicationContext, "Added succesfully", Toast.LENGTH_SHORT).show()


            }

            //cancel button
            mDialogView.btn_cust_add_room_cancel.setOnClickListener {
                //dismiss dialog
                mAlertDialog.dismiss()
            }

        }
        
    }


    @SuppressLint("SimpleDateFormat")
    private fun convertLongToDate(date: Long?): String {
        val format = SimpleDateFormat("dd MMM")
        return format.format(date)
    }
}