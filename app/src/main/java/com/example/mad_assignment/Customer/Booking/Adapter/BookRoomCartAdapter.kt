package com.example.mad_assignment.Customer.Booking.Adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.widget.AppCompatImageButton
import androidx.recyclerview.widget.RecyclerView
import com.example.mad_assignment.Customer.Booking.Class.ReservationDetail
import com.example.mad_assignment.R
import com.squareup.picasso.Picasso
import java.util.*


open class BookRoomCartAdapter(
        private var cartList: ArrayList<ReservationDetail>,
        private var mContext: Context,
        private var nights: Int

): RecyclerView.Adapter<BookRoomCartAdapter.CartViewHolder>(), AdapterView.OnItemSelectedListener {

    private var qtySelected: Int = 0

    class CartViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val ivRoom: ImageView = itemView.findViewById(R.id.iv_bmc_item)
        val tvRoomType: TextView = itemView.findViewById(R.id.tv_brc_item_room_type)
        val tvNight: TextView = itemView.findViewById(R.id.tv_brc_item_price_title)
        val tvRoomPrice: TextView = itemView.findViewById(R.id.tv_brc_item_price)
        val tvRoomBeds: TextView = itemView.findViewById(R.id.tv_brc_item_beds)
        val tvRoomOccupancy: TextView = itemView.findViewById(R.id.tv_brc_item_occupancy)
        val qty:Spinner = itemView.findViewById(R.id.brc_item_spinner)
        val btnDelete: AppCompatImageButton = itemView.findViewById(R.id.brc_item_delete)

    }

    override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
    ): CartViewHolder {
        val itemView = LayoutInflater.from(mContext).inflate(
                R.layout.book_room_cart_item,
                parent,
                false
        )

        return CartViewHolder(itemView)
    }

    @SuppressLint("SetTextI18n", "SimpleDateFormat")
    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val currentItem = cartList[position]

        //set spinner
        val adapter = ArrayAdapter.createFromResource(
                mContext,
                R.array.room_select_qty,
                android.R.layout.simple_spinner_item
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        holder.qty!!.adapter = adapter
        holder.qty!!.onItemSelectedListener = this
        holder.qty!!.setSelection(currentItem.qty - 1)


        // Process date
        Picasso.get().load(currentItem.roomType?.img).into(holder.ivRoom)
        Picasso.get().isLoggingEnabled = true


        //calculate price based on nights
        currentItem.subtotal = nights * currentItem.roomType?.price!!

        holder.tvRoomType.text = currentItem.roomType?.roomType
        holder.tvRoomPrice.text = "RM ${currentItem.subtotal!!.format(2)}"
        holder.tvRoomBeds.text =  "${currentItem.roomType?.beds} beds"
        holder.tvRoomOccupancy.text = "${currentItem.roomType?.occupancy} guest"
        holder.tvNight.text = "price for $nights nights per room"

        //delete button
        holder.btnDelete.setOnClickListener {
            deleteSelectedRoom(currentItem, position)
        }
    }


    override fun getItemCount(): Int {
        return cartList.size
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        this.qtySelected = parent?.selectedItemPosition!! +1
    }

    private fun deleteSelectedRoom(currentItem: ReservationDetail, position: Int){
        cartList.remove(currentItem);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, cartList.size);

        Toast.makeText(mContext, "Removed successfully", Toast.LENGTH_SHORT).show()

    }

    fun Double.format(digits: Int) = "%.${digits}f".format(this)



}