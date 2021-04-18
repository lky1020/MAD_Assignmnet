package com.example.mad_assignment.Staff.room.Main

import android.app.Activity
import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.InputFilter
import android.text.Spanned
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.net.toUri
import com.example.mad_assignment.Customer.Booking.Class.RoomType
import com.example.mad_assignment.R
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.edit_room_desc.*
import kotlinx.android.synthetic.main.manage_room.*
import java.util.*
import java.util.regex.Matcher
import java.util.regex.Pattern
import java.util.regex.PatternSyntaxException


class EditRoomDesc : AppCompatActivity() {

    private val PICK_IMAGE_REQUEST = 71
    private var firebaseStore: FirebaseStorage? = null
    private var storageReference: StorageReference? = null
    var selectPhotoUri:Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.edit_room_desc)

        firebaseStore = FirebaseStorage.getInstance()
        storageReference = FirebaseStorage.getInstance().reference

        //------------------------------------------------------
        //------- retrieve data from previous page -------------
        //------------------------------------------------------

        val gson = Gson()
        val roomType = gson.fromJson<RoomType>(intent.getStringExtra("roomType"), RoomType::class.java)

        //set toolbar as support action bar
        val toolbar: Toolbar = findViewById(R.id.room_menu_toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = null

        //back button
        val backButton = findViewById<ImageView>(R.id.edit_room_desc_back_icon)
        backButton.setOnClickListener {
            finish()
        }

        //assign value
        selectPhotoUri = roomType.img?.toUri()
        Picasso.get().load(selectPhotoUri).into(iv_edit_desc_img)
        //Picasso.get().isLoggingEnabled = true

        tv_edit_room_desc_subtitle.text = roomType.roomType

        et_edit_occupancy.setText(roomType.occupancy.toString())
        et_edit_price.setText(roomType.price.toString())
        et_edit_size.setText(roomType.size.toString())
        et_edit_beds.setText(roomType.beds.toString())

        // max 000.00
        et_edit_price.inputFilterDecimal(
                maxDigitsIncludingPoint = 6,
                maxDecimalPlaces = 2
        )

        //img
        ib_rooms_edit.setOnClickListener {
            val intentAction = Intent(Intent.ACTION_PICK)
            intentAction.type = "image/*"
            startActivityForResult(intentAction, 0)
        }

        //------------------------------------------------------
        //----------------------- edit -------------------------
        //------------------------------------------------------
        btn_edit_room.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setMessage("Are you sure you want to Edit?")
                    .setCancelable(false)
                    .setPositiveButton("Yes") { _, _ ->

                        //validation

                        if(et_edit_occupancy.text.toString() == ""){
                            Toast.makeText(this, "Occupancy cannot be null!", Toast.LENGTH_SHORT).show()
                        }else if(et_edit_price.text.toString() == ""){
                            Toast.makeText(this, "Price cannot be null!", Toast.LENGTH_SHORT).show()
                        }else if(et_edit_size.text.toString() == ""){
                            Toast.makeText(this, "Size cannot be null!", Toast.LENGTH_SHORT).show()
                        }else if(et_edit_beds.text.toString() == ""){
                            Toast.makeText(this, "Beds cannot be null!", Toast.LENGTH_SHORT).show()
                        }else{



                            roomType.occupancy = et_edit_occupancy.text.toString().toInt()
                            roomType.price = et_edit_price.text.toString().toDouble()
                            roomType.size = et_edit_size.text.toString().toInt()
                            roomType.beds = et_edit_beds.text.toString()

                            if(selectPhotoUri == null){
                                //update db
                                updateRoom(roomType, this)
                            }else{
                                uploadImgToFirebaseStorage(selectPhotoUri!!, roomType)
                            }

                            val gson = Gson()
                            val intent = Intent(this, ManageRoom::class.java)
                            intent.putExtra("RoomType", gson.toJson(roomType))
                            startActivity(intent)
                         }

                    }
                    .setNegativeButton("No") { dialog, id ->
                        // Dismiss the dialog
                        dialog.dismiss()
                    }
            val alert = builder.create()
            alert.show()
        }



    }

    //select photo in phone
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        //proceed and check what the selected image was...
        if (requestCode == 0 && resultCode == Activity.RESULT_OK && data != null) {
            Log.d("Edit Room", "image is selected")
            selectPhotoUri = data.data
            Picasso.get().load(selectPhotoUri).into(iv_edit_desc_img)
            //upload the data img into Firebase
            //uploadImgToFirebaseStorage(selectPhotoUri!!)
        }
    }


    //upload img to firebase storage and firebase
    private fun uploadImgToFirebaseStorage(imguri: Uri, roomType: RoomType) {
        if (imguri == null) return

        val ref = FirebaseStorage.getInstance().getReference("/room/${roomType.roomType!!.toLowerCase()}/")

        ref.putFile(imguri!!)
                .addOnSuccessListener {
                    Log.d("Edit Room", "Successfully uploaded image: ${it.metadata?.path}")

                    ref.downloadUrl.addOnSuccessListener {
                        Log.d("Edit Room", "File Location: $it")

                        roomType.img = it.toString()
                        updateRoom(roomType, this)

                    //saveUserToFirebaseDB(user)
                    }
                }
                .addOnFailureListener {
                    Log.d("Edit Room", "Failed to upload image to storage: ${it.message}")
                    Toast.makeText(this, "Your photo is fail to upload", Toast.LENGTH_LONG)
                            .show()
                }
    }


}


private fun updateRoom(currentItem: RoomType, context: Context){
    val myRef = FirebaseDatabase.getInstance().getReference("Room")
            .child(currentItem.roomID.toString())

    myRef.addListenerForSingleValueEvent(object : ValueEventListener {

        override fun onDataChange(snapshot: DataSnapshot) {
            // Code for showing progressDialog while uploading
            val progressDialog = ProgressDialog(context)
            progressDialog.setTitle("Uploading...")
            progressDialog.show()

            if (snapshot.exists()) {


                myRef.setValue(currentItem)
                        .addOnSuccessListener {
                            progressDialog.dismiss()
                            Log.d("Edit Room Type", "Successfully edit room description")
                            Toast.makeText(context, "Edit Success", Toast.LENGTH_SHORT).show()
                        }
                        .addOnFailureListener {
                            progressDialog.dismiss()
                            Log.d("Edit Room Type", "Fail to edit room description")
                            Toast.makeText(context, "Fail to Edit", Toast.LENGTH_SHORT).show()
                        }

            }
        }

        override fun onCancelled(error: DatabaseError) {

        }
    })

    //notifyItemRangeChanged(position, dataList.size);
}

// input filter edit text decimal digits
fun EditText.inputFilterDecimal(

        // maximum digits including point and without decimal places
        maxDigitsIncludingPoint: Int,
        maxDecimalPlaces: Int // maximum decimal places
){
    try {
        filters = arrayOf<InputFilter>(
                DecimalDigitsInputFilter(maxDigitsIncludingPoint, maxDecimalPlaces)
        )

    }catch (e: PatternSyntaxException){
        isEnabled = false
        hint = e.message
    }

}

//decimal digits input filter
class DecimalDigitsInputFilter(
        maxDigitsIncludingPoint: Int,
        maxDecimalPlaces: Int

) : InputFilter {
    private val pattern: Pattern = Pattern.compile(

            "[0-9]{0," + (maxDigitsIncludingPoint - 1) + "}+((\\.[0-9]{0,"
                    + (maxDecimalPlaces - 1) + "})?)||(\\.)?"
    )



    override fun filter(
            p0: CharSequence?, p1: Int, p2: Int, p3: Spanned?, p4: Int, p5: Int
    ): CharSequence? {
        p3?.apply {

            val matcher: Matcher = pattern.matcher(p3)
            return if (!matcher.matches()) "" else null
        }

        return null

    }

}