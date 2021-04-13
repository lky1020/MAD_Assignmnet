package com.example.mad_assignment.Staff.Staff_Fragments.staff_housekeeping.Main.Main.Item

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.media.Image
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.widget.ContentFrameLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mad_assignment.Customer.Customer_Fragments.cust_housekeeping.Class.HousekeepingItem
import com.example.mad_assignment.R
import com.example.mad_assignment.Staff.Staff_Fragments.staff_housekeeping.Main.Adapter.StaffHousekeepingItemAdapter
import com.example.mad_assignment.Staff.Staff_Fragments.staff_housekeeping.Main.Model.StaffHousekeepingAvailableItemModel
import com.google.android.material.imageview.ShapeableImageView
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.staff_add_item_dialog.view.*
import kotlinx.android.synthetic.main.staff_edit_item_dialog.view.*

class StaffHousekeepingItemFragment(private val title: String) : Fragment() {

    private lateinit var staffHousekeepingAvailableItemModel: StaffHousekeepingAvailableItemModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var parentLayout: ViewGroup
    private lateinit var parentElement: ContentFrameLayout

    private lateinit var servicesType: String
    private lateinit var etSearch: EditText
    private lateinit var housekeepingItemList: ArrayList<HousekeepingItem>

    private lateinit var btnAddItem: Button
    private lateinit var etItemTitle: EditText
    private lateinit var etItemQuantity: EditText
    private lateinit var imgUri: Uri
    private lateinit var ivItemImg: ShapeableImageView
    private lateinit var imgURL: String

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        parentLayout = container?.parent as ViewGroup
        parentElement = parentLayout.parent as ContentFrameLayout

        val root: View =  inflater.inflate(R.layout.staff_fragment_housekeeping_item, container, false)

        //Get the viewmodel for housekeeping
        staffHousekeepingAvailableItemModel = ViewModelProvider(this).get(StaffHousekeepingAvailableItemModel::class.java)

        //Get action bar title for retrieve data from db
        servicesType = title

        etSearch = parentElement.findViewById(R.id.et_staff_housekeeping_services_search)

        etSearch.hint = "Search Here..."

        root.setOnTouchListener { v, event ->
            etSearch.clearFocus()

            // Disable virtual k
            val imm = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
            imm!!.hideSoftInputFromWindow(etSearch.windowToken, 0)

            true
        }

        //set the button
        btnAddItem = parentElement.findViewById(R.id.btn_add_service)

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        etSearch.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {
                if(!etSearch.text.equals("")){
                    filter(s.toString())
                }
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

            }
        })

        //add item
        btnAddItem.setOnClickListener {
            // Inflate the dialog
            val addDialogView = LayoutInflater.from(requireContext()).inflate(R.layout.staff_add_item_dialog, null)

            //Alert dialog builder
            val mBuilder = AlertDialog.Builder(requireContext())
                    .setView(addDialogView)
                    .setTitle("Add $servicesType")

            //show dialog
            val mAlertDialog = mBuilder.show()

            etItemTitle = addDialogView.findViewById(R.id.et_add_item_title)
            etItemQuantity = addDialogView.findViewById(R.id.et_add_item_quantity)
            ivItemImg = addDialogView.findViewById(R.id.iv_item_img)

            ivItemImg.setOnClickListener {
                Toast.makeText(requireContext(), "Opening Gallery", Toast.LENGTH_SHORT).show()

                val i = Intent()
                i.type = "image/*"
                i.action = Intent.ACTION_GET_CONTENT
                startActivityForResult(Intent.createChooser(i, "Choose Picture"), 111)
            }

            //Button action
            addDialogView.btn_add_cancel.setOnClickListener{
                mAlertDialog.dismiss()
            }

            addDialogView.btn_add_item.setOnClickListener{

                if(etItemTitle.text.toString() != "" && etItemQuantity.text.toString() != ""){
                    try {

                        if(etItemQuantity.text.toString().toInt() > 0){
                            mAlertDialog.dismiss()

                            addItem()

                            Toast.makeText(requireActivity(), "Added Success", Toast.LENGTH_SHORT).show()
                        }

                    }catch (ex: Exception){
                        Toast.makeText(requireActivity(), "Invalid Input", Toast.LENGTH_SHORT).show()
                    }

                }else{
                    Toast.makeText(requireActivity(), "Invalid Input", Toast.LENGTH_SHORT).show()
                }


            }
        }

        recyclerView = view.findViewById(R.id.rv_staff_housekeeping_item)
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.adapter = StaffHousekeepingItemAdapter(ArrayList<HousekeepingItem>(), requireActivity(), servicesType) //Initialize adapter
        recyclerView.setHasFixedSize(true)

        //Retrieve data from db
        staffHousekeepingAvailableItemModel.retrieveHousekeepingItemFromDB(servicesType)

        //Observe the housekeeping list and set it
        staffHousekeepingAvailableItemModel.gethousekeepingItemList().observe(viewLifecycleOwner, Observer {
            recyclerView.adapter = StaffHousekeepingItemAdapter(it, requireActivity(), servicesType)
            housekeepingItemList = it
        })
    }

    @SuppressLint("DefaultLocale")
    private fun filter(text: String){
        if(activity != null) {
            val filteredList = java.util.ArrayList<HousekeepingItem>()

            for (i in housekeepingItemList) {
                if (i.title.toLowerCase().contains(text.toLowerCase())) {
                    filteredList.add(i)
                }
            }

            recyclerView.adapter = StaffHousekeepingItemAdapter(filteredList, requireActivity(), servicesType)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == 111 && resultCode == Activity.RESULT_OK && data != null){
            imgUri = data.data!!

            ivItemImg.setImageURI(imgUri)
        }
    }

    private fun addItem(){

        val imageRef: StorageReference = if(servicesType == "Bed Textiles"){
            FirebaseStorage.getInstance().reference.child("bedTextiles/" + etItemTitle.text.toString())
        }else{
            FirebaseStorage.getInstance().reference.child("toiletries/" + etItemTitle.text.toString())
        }

        imageRef.putFile(imgUri)
            .addOnSuccessListener {
                imageRef.downloadUrl.addOnSuccessListener {
                    imgURL = it.toString()

                    val myRef = FirebaseDatabase.getInstance().getReference("Housekeeping")
                            .child(servicesType).child("ItemAvailable")

                    val newItem = HousekeepingItem(etItemTitle.text.toString(), imgURL, etItemQuantity.text.toString().toInt())

                    myRef.child(newItem.title).setValue(newItem)
                }
            }
            .addOnFailureListener{
                Toast.makeText(activity, "Fail to Upload Image", Toast.LENGTH_SHORT).show()
            }
    }
}