package com.example.mad_assignment.Staff.room.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mad_assignment.R
import com.example.mad_assignment.Staff.room.Class.Room

class RoomsAdapter(private val context: Context): RecyclerView.Adapter<RoomsAdapter.RoomsViewHolder>() {

    private var dataList = mutableListOf<Room>()

    fun setListData(data: MutableList<Room>){
        dataList = data
    }

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
        holder.bindView(rooms)
    }

    override fun getItemCount(): Int {
        if(dataList.size > 0)
            return dataList.size
        else{
            return 0
        }
    }

inner class RoomsViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
    val roomNo: TextView
    val roomStatus: TextView

    init {
        roomNo = itemView.findViewById(R.id.tv_ri_room_no)
        roomStatus = itemView.findViewById(R.id.tv_ri_room_status)
    }

    fun bindView(rooms: Room){
        roomNo.text = rooms.roomNo
        roomStatus.text = rooms.status
    }

}


}