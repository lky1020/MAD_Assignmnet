package com.example.mad_assignment.Staff.room.Adapter

import android.app.AlertDialog
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.mad_assignment.R
import com.example.mad_assignment.Staff.room.Class.Room
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.alert_edit_room.view.*


class RoomsAdapter(
        private var dataList: ArrayList<Room>,
        private val context: Context
): RecyclerView.Adapter<RoomsAdapter.RoomsViewHolder>() {


    //delete function
    fun deleteItem(index: Int){
        dataList.removeAt(index)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoomsViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.rooms_item, parent, false)
        return RoomsViewHolder(view)
    }

    override fun onBindViewHolder(holder: RoomsViewHolder, position: Int) {
        val rooms:Room = dataList[position]
        holder.bindView(rooms, position)
    }

    override fun getItemCount(): Int {
        return if(dataList.size > 0)
            dataList.size
        else{
            0
        }
    }

inner class RoomsViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
    private val roomNo: TextView = itemView.findViewById(R.id.tv_ri_room_no)
    private val roomStatus: TextView = itemView.findViewById(R.id.tv_ri_room_status)
    private val editBtn: ImageButton = itemView.findViewById(R.id.ib_rooms_edit)
    private val deleteBtn: ImageButton = itemView.findViewById(R.id.ib_rooms_delete)

    fun bindView(rooms: Room, position: Int){
        roomNo.text = rooms.roomNo
        roomStatus.text = rooms.status

        //-------------------------------------------------------------
        //-----------------------Edit Room-----------------------------
        //-------------------------------------------------------------
        editBtn.setOnClickListener {
            val mDialogView = LayoutInflater.from(context).inflate(R.layout.alert_edit_room, null)
            val dialog = androidx.appcompat.app.AlertDialog.Builder(context)

            mDialogView.switch_edit_room_status.isChecked = rooms.status.equals("available")


            dialog.setCancelable(false)
                    .setTitle("${rooms.roomNo}")
                    .setPositiveButton("Edit") { dialog, _ ->
                        //dismiss dialog
                        dialog.dismiss()

                        if(mDialogView.switch_edit_room_status.isChecked)
                            rooms.status = "available"
                        else
                            rooms.status = "not available"

                        //update database
                        updateRoom(rooms, position)
                    }
                    .setNegativeButton("Cancel") { dialog, _ ->
                        //dismiss dialog
                        dialog.dismiss()
                    }
                    .setView(mDialogView)
            dialog.create().show()
        }

        //-------------------------------------------------------------
        //-----------------------Delete Room---------------------------
        //-------------------------------------------------------------
        deleteBtn.setOnClickListener {
            val builder = AlertDialog.Builder(context)
            builder.setMessage("Are you sure you want to Delete?")
                    .setCancelable(false)
                    .setPositiveButton("Yes") { _, _ ->
                        deleteRoom(rooms, position)
                    }
                    .setNegativeButton("No") { dialog, id ->
                        // Dismiss the dialog
                        dialog.dismiss()
                    }
            val alert = builder.create()
            alert.show()

        }
    }

}

    private fun deleteRoom(currentItem: Room, position: Int){

        val myRef = FirebaseDatabase.getInstance().getReference("Rooms")
                .child(currentItem.roomType?.roomID.toString())

        currentItem.roomNo?.let {
            myRef.child(it).removeValue()
                    .addOnSuccessListener {
                        Log.d("Manage Room", "Successfully delete room")
                        Toast.makeText(context, "Delete Success", Toast.LENGTH_SHORT).show()
                    }
                    .addOnFailureListener{
                        Log.d("Manage Room", "Fail to delete")
                        Toast.makeText(context, "Fail to delete", Toast.LENGTH_SHORT).show()
                    }
        }

        dataList.remove(currentItem);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, dataList.size);

    }

    private fun updateRoom(currentItem: Room, position: Int){
        val myRef = FirebaseDatabase.getInstance().getReference("Rooms")
                .child(currentItem.roomType?.roomID.toString())
                .child(currentItem.roomNo.toString())

        myRef.addListenerForSingleValueEvent(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    myRef.child("status").setValue(currentItem.status)
                            .addOnSuccessListener {
                                Log.d("Manage Room", "Successfully set room status")
                                Toast.makeText(context, "Edit Success", Toast.LENGTH_SHORT).show()
                            }
                            .addOnFailureListener{
                                Log.d("Manage Room", "Fail to change room status")
                                Toast.makeText(context, "Fail to Edit", Toast.LENGTH_SHORT).show()
                            }
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })

        notifyItemRangeChanged(position, dataList.size);
    }

}