package com.example.mad_assignment.Staff.Staff_Fragments.staff_housekeeping.Main.Adapter

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.mad_assignment.Customer.Customer_Fragments.cust_housekeeping.Class.HousekeepingItem
import com.example.mad_assignment.R
import com.google.android.material.imageview.ShapeableImageView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.staff_edit_item_dialog.view.*

class StaffHousekeepingItemAdapter(private var housekeepingItemList: ArrayList<HousekeepingItem>, private var mContext: FragmentActivity, private val servicesType: String): RecyclerView.Adapter<StaffHousekeepingItemAdapter.StaffHousekeepingItemViewHolder>() {

    private lateinit var enteredQuantity: EditText

    class StaffHousekeepingItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val ivItemOrdered: ShapeableImageView = itemView.findViewById(R.id.iv_item_ordered)
        val tvOrderedTitle: TextView = itemView.findViewById(R.id.tv_ordered_title)
        val tvOrderedQuantity: TextView = itemView.findViewById(R.id.tv_ordered_quantity)
        val btnItemEdit: ImageView = itemView.findViewById(R.id.btn_item_edit)
        val btnItemDelete: ImageView = itemView.findViewById(R.id.btn_item_delete)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): StaffHousekeepingItemViewHolder {
        val itemView = LayoutInflater.from(mContext).inflate(R.layout.cardview_staff_housekeeping_item, parent, false)

        return StaffHousekeepingItemViewHolder(itemView)
    }

    @SuppressLint("SetTextI18n", "SimpleDateFormat")
    override fun onBindViewHolder(holder: StaffHousekeepingItemViewHolder, position: Int) {
        val currentItem = housekeepingItemList[position]

        Picasso.get().load(currentItem.img).into(holder.ivItemOrdered);
        holder.tvOrderedTitle.text = currentItem.title
        holder.tvOrderedQuantity.text = "Quantity: " + currentItem.quantity.toString()

        holder.btnItemEdit.setOnClickListener {

            // Inflate the dialog
            val editDialogView = LayoutInflater.from(mContext).inflate(R.layout.staff_edit_item_dialog, null)

            editDialogView.tv_edit_item_quantity.text = "Quantity: "

            //Alert dialog builder
            val mBuilder = AlertDialog.Builder(mContext)
                    .setView(editDialogView)
                    .setTitle("Edit $servicesType")

            //show dialog
            val mAlertDialog = mBuilder.show()

            enteredQuantity = editDialogView.et_edit_item_quantity

            //Button Action
            editDialogView.btn_edit_cancel.setOnClickListener{
                mAlertDialog.dismiss()
            }

            editDialogView.btn_edit_item.setOnClickListener{

                if(enteredQuantity.text.toString() != ""){

                    try{
                        if(enteredQuantity.text.toString().toInt() > 0){
                            mAlertDialog.dismiss()

                            updateItem(currentItem, enteredQuantity.text.toString().toInt())

                            Toast.makeText(mContext, "Edit Success", Toast.LENGTH_SHORT).show()
                        }

                    }catch (ex: Exception){
                        Toast.makeText(mContext, "Invalid Input", Toast.LENGTH_SHORT).show()
                    }
                }else{
                    Toast.makeText(mContext, "Invalid Input", Toast.LENGTH_SHORT).show()
                }
            }

        }

        holder.btnItemDelete.setOnClickListener {
            deleteItem(currentItem, position)
        }
    }

    override fun getItemCount(): Int {
        return housekeepingItemList.size
    }

    private fun updateItem(currentItem: HousekeepingItem, quantity: Int){
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

    private fun deleteItem(currentItem: HousekeepingItem, position: Int){
        val myRef = FirebaseDatabase.getInstance().getReference("Housekeeping")
                .child(servicesType).child("ItemAvailable")

        myRef.child(currentItem.title).removeValue()

        housekeepingItemList.remove(currentItem);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, housekeepingItemList.size);

        Toast.makeText(mContext, "Item Deleted", Toast.LENGTH_SHORT).show()
    }
}