package com.example.mad_assignment.Cust_Staff_Shared.Cust_Staff_Fragments

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.mad_assignment.Class.User
import com.example.mad_assignment.Customer.Cust_Staff_Fragments.profile.ProfileFragment
import com.example.mad_assignment.CustomerMain
import com.example.mad_assignment.R
import com.example.mad_assignment.StaffMain
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

//NEED GET DATA FROM FIREBASE & compare, then decide which main page to go

//connect with login.xml
class Login: AppCompatActivity() {

    //temporary variable -- need REMOVE after use firebase
    val role: String? = "manager"

    //variable
    private lateinit var auth: FirebaseAuth
    internal var user: User? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login)

        //variable
        auth = FirebaseAuth.getInstance()
        var loginFail = false
        val userId: EditText = findViewById(R.id.edittext_login_userid)
        val showPsdIv: ImageView = findViewById(R.id.iv_login_hidePassword)
        val password: EditText = findViewById(R.id.edittext_login_password)
        val btnLogin: Button = findViewById(R.id.btn_login)

        //if show or hide Password icon is clicked
        showPsdIv.setOnClickListener() {
            showorHideLoginPassword(showPsdIv, password)
        }


        //login btn
        btnLogin.setOnClickListener() {

            //check if userid and password is filled
            if (userId.text.toString().isEmpty()) {
                userId.error = "Enter User ID"
            } else if (password.text.toString().isEmpty()) {
                password.error = "Enter Password"
            } else { //both field filled

                //GET DATA FROM FIREBASE
                val database = FirebaseDatabase.getInstance()
                val myRef = database.getReference("User")

                var getData = object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {

                        //get all data for compare
                        for (c in snapshot.children) {

                            //compare the userid
                            if (c.child("UserID").value.toString() == userId.text.toString()) {
                                var phoneNumber = c.key
                                var name = c.child("Name").getValue()
                                var email = c.child("Email").getValue()
                                var role = c.child("Role").getValue()
                            }
                        }

                    }

                    override fun onCancelled(error: DatabaseError) {

                    }
                }
                myRef.addValueEventListener(getData)
                myRef.addListenerForSingleValueEvent(getData)

                print("DEBUG MY reference addListenerForSingleValueEvent>>>>>>>>>>>>>>>>>>>> $myRef")

                print("DEBUG Get Data>>>>>>>>>>>>>>>>>> $getData")
                //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! CANNOT SCAN IF USER IS AUTHORIZED !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!//
                //ACTION IF USER FOUND
                if (myRef!=null) {

                    //user found, navigate to CustomerMain.kt
                    val intent = Intent(this, CustomerMain::class.java)
                    startActivity(intent)
                } else {
                    Toast.makeText(this, "Invalid User", Toast.LENGTH_LONG).show()
                }
            }
        }


        //will proceed to CustomerMain.kt (will enter customer menu navigation)
        //click 'btnStafftLogin' will proceed to StaffMain.kt (will enter staff menu navigation)
        /* val btnStaffLogin: Button = findViewById(R.id.btnStaffLogin)
        btnStaffLogin.setOnClickListener() {
            val intent = Intent(this, StaffMain::class.java)
            intent.putExtra("StaffPosition", role)
            startActivity(intent)
        }

                 */


    }

    //show/hide password icon function
    private fun showorHideLoginPassword(showPsdIv: ImageView, password: EditText) {
        //password1 icon function
        if (password.transformationMethod == HideReturnsTransformationMethod.getInstance()) {
            password.transformationMethod = PasswordTransformationMethod.getInstance()
            showPsdIv.setImageResource(R.drawable.ic_hide_psw)
        } else {
            password.transformationMethod = HideReturnsTransformationMethod.getInstance()
            showPsdIv.setImageResource(R.drawable.ic_show_psw)
        }
    }
}

