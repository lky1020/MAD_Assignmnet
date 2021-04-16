package com.example.mad_assignment.Cust_Staff_Shared.Cust_Staff_Fragments

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Layout
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.mad_assignment.Class.User
import com.example.mad_assignment.R
import com.google.android.material.appbar.AppBarLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import java.util.*


//belongs to fragment_profile.xml
class ProfileFragment : Fragment() {

    lateinit var iv_profile: CircleImageView
    lateinit var user_name:TextView
    lateinit var user_email:TextView
    lateinit var user_password:TextView
    lateinit var user_phoneNum:TextView

    var selectPhotoUri:Uri? = null

    companion object {
        var currentUser: User? = null
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        //get current user data
        fetchCurrentUser()

        val root = inflater.inflate(R.layout.fragment_profile, container, false)

        //variable initialization
        var btn_select_photo: Button = root.findViewById(R.id.btn_select_photo)
        iv_profile = root.findViewById(R.id.iv_profile)
        user_name = root.findViewById(R.id.tv_user_name)
        user_email = root.findViewById(R.id.tv_user_email)
        user_password = root.findViewById(R.id.tv_user_password)
        user_phoneNum = root.findViewById(R.id.tv_user_phoneNum)


        //add photo
        btn_select_photo.setOnClickListener(){
            Log.d("ProfileFragment", "Try to show photo selector")

            //get into gallery
            val intentAction = Intent(Intent.ACTION_PICK)
            intentAction.type = "image/*"
            startActivityForResult(intentAction, 0)
        }




        return root
    }

    //select photo in phone
     override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        //proceed and check what the selected image was...
        if (requestCode == 0 && resultCode == Activity.RESULT_OK && data != null) {
            Log.d("ProfileFragment", "Profile image is selected")
            selectPhotoUri = data.data

            //upload the data img into Firebase
            uploadImgToFirebaseStorage(selectPhotoUri!!)
        }
    }


    //upload img to firebase storage and firebase
    private fun uploadImgToFirebaseStorage(imguri: Uri) {
        if (imguri == null) return

        val uid = FirebaseAuth.getInstance().uid ?: ""
        val filename = UUID.randomUUID().toString()
        val ref = FirebaseStorage.getInstance().getReference("/User/$uid/$filename")

         ref.putFile(imguri!!)
                .addOnSuccessListener {
                    Log.d("PROFILE FRAGMENT", "Successfully uploaded image: ${it.metadata?.path}")

                    ref.downloadUrl.addOnSuccessListener {
                        Log.d("PROFILE FRAGMENT", "File Location: $it")
                        Toast.makeText(context, "Your photo is uploaded successfully", Toast.LENGTH_LONG).show()
                        saveUserToFirebaseDB(it.toString())
                    }
                }
                .addOnFailureListener {
                    Log.d("PROFILE FRAGMENT", "Failed to upload image to storage: ${it.message}")
                    Toast.makeText(context, "Your photo is fail to upload", Toast.LENGTH_LONG)
                        .show()
                }
    }

    //upload selected img with all data to firebase db
    private fun saveUserToFirebaseDB(imgurl: String){
        val uid = FirebaseAuth.getInstance().uid ?: ""
        val ref = FirebaseDatabase.getInstance().getReference("/User/$uid")
        val user = User(currentUser!!.name, currentUser!!.uid, currentUser!!.password, currentUser!!.phoneNum, currentUser!!.email, currentUser!!.role, imgurl)

        ref.setValue(user)
                .addOnSuccessListener {
                    Log.d("PROFILE FRAGMENT", "Finally we saved the user to Firebase Database")
                }
                .addOnFailureListener {
                    Log.d("PROFILE FRAGMENT", "Failed to set value to database: ${it.message}")
                }
    }

    //get info from firebase
    private fun fetchCurrentUser(){
        val uid = FirebaseAuth.getInstance().uid
        val ref = FirebaseDatabase.getInstance().getReference("/User/$uid")
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                currentUser = snapshot.getValue(User::class.java)

                user_name.text = currentUser!!.name
                user_email.text = currentUser!!.email
                user_password.text = currentUser!!.password
                user_phoneNum.text = currentUser!!.phoneNum

                if (currentUser!!.img != null) {
                    Picasso.get().load(currentUser!!.img).into(iv_profile)
                } else {
                    iv_profile.setImageResource(R.drawable.ic_profile)
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }



}
