package com.example.mad_assignment.Staff.Staff_Fragments.staff_housekeeping

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.mad_assignment.Customer.Customer_Fragments.cust_housekeeping.Model.CustHousekeepingServicesModel
import com.example.mad_assignment.R
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference


class StaffHousekeepingFragment : Fragment() {
    private lateinit var custHousekeepingServicesModel: CustHousekeepingServicesModel
    lateinit var filePath: Uri
    lateinit var imageView: ImageView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val root = inflater.inflate(R.layout.staff_fragment_housekeeping, container, false)
        imageView = root.findViewById(R.id.iv_testing)
//        val recyclerView: RecyclerView = root.findViewById(R.id.rv_housekeeping)
//
//        //Get the viewmodel for housekeeping
//        custHousekeepingModel = ViewModelProvider(this).get(CustHousekeepingModel::class.java)
//
//        //Observe the housekeeping list and set it
//        custHousekeepingModel.getHousekeepingList().observe(viewLifecycleOwner, Observer {
//            recyclerView.adapter = HousekeepingAdapter(it)
//        })
//
//        recyclerView.layoutManager = GridLayoutManager(this.context, 2)
//        recyclerView.setHasFixedSize(true)

        val btnUpload: Button = root.findViewById(R.id.btn_choose)
        btnUpload.setOnClickListener {
            var i = Intent()
            i.setType("image/*")
            i.setAction(Intent.ACTION_GET_CONTENT)
            startActivityForResult(Intent.createChooser(i, "Choose Picture"), 111)
        }

        return root
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == 111 && resultCode == Activity.RESULT_OK && data != null){
            filePath = data.data!!

            val bitmap = MediaStore.Images.Media.getBitmap(context?.contentResolver, filePath)
            imageView.setImageBitmap(bitmap)

            val imageRef: StorageReference = FirebaseStorage.getInstance().reference.child("housekeeping/Room Cleaning")
//            val imageRef: StorageReference = FirebaseStorage.getInstance().reference.child("housekeeping/Laundry Service")
//            val imageRef: StorageReference = FirebaseStorage.getInstance().reference.child("housekeeping/Toiletries")
//            val imageRef: StorageReference = FirebaseStorage.getInstance().reference.child("housekeeping/Bed Textiles")

            imageRef.putFile(filePath)
                .addOnSuccessListener {
                    Toast.makeText(activity, "File Uploaded", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener{
                    Toast.makeText(activity, "File Fail", Toast.LENGTH_SHORT).show()
                }
        }
    }
}