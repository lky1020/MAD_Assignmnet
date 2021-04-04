package com.example.mad_assignment.Cust_Staff_Shared.Cust_Staff_Fragments

import android.content.Intent
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.mad_assignment.Class.User
import com.example.mad_assignment.Customer.Chat.messages.LatestMessages
import com.example.mad_assignment.CustomerMain
import com.example.mad_assignment.MainActivity
import com.example.mad_assignment.R
import com.example.mad_assignment.StaffMain
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.login.*

//connect with login.xml
class Login: AppCompatActivity() {
    private lateinit var decorView: View

    var phoneNumberDb: String? = null
    var userDb: String? = null
    var emailDb: String? = null
    var roleDb: String? = null

    //Change by Joan Hau for fetch current user information
    companion object {
        var currentUser: User? = null

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login)

        //For status & navigation bar
        decorView = window.decorView
        decorView.systemUiVisibility = hideSystemBars()


        //variable
        val email: EditText = findViewById(R.id.edittext_login_email)
        val showPsdIv: ImageView = findViewById(R.id.iv_login_hidePassword)
        val password: EditText = findViewById(R.id.edittext_login_password)
        val btnLogin: Button = findViewById(R.id.btn_login)



        //if show or hide Password icon is clicked
        showPsdIv.setOnClickListener {
            showorHideLoginPassword(showPsdIv, password)
        }


        //back main page icon
        loginBackIcon()

        //login btn
        btnLogin.setOnClickListener {

            //check if userid and password is filled
            if (edittext_login_email.text.toString().isEmpty()) {
                email.error = "Enter User Email"
            } else if (edittext_login_password.text.toString().isEmpty()) {
                password.error = "Enter Password"
            } else { //both field filled
                performLoginSuccessful()


//
//                //GET DATA FROM FIREBASE
//                val database = FirebaseDatabase.getInstance()
//                val myRef = database.getReference("User")
//                //intent for customer
//                val intentCustMain = Intent(this, CustomerMain::class.java)
//                intentCustMain.putExtra("email", email.text.toString())
//
//                //intent for staff
//                val intentStaffMain = Intent(this, StaffMain::class.java)
//                intentStaffMain.putExtra("Role", "Staff")
//                intentStaffMain.putExtra("email",  email.text.toString())
//
//                //intent for manager
//                val intentManagerMain = Intent(this, StaffMain::class.java)
//                intentManagerMain.putExtra("Role", "Manager")
//                intentManagerMain.putExtra("email", email.text.toString())

//                var getData = object : ValueEventListener {
//                    override fun onDataChange(snapshot: DataSnapshot) {
//                        var userFound = false
//                        val email = email.text.toString()
//                        val password = password.text.toString()
//                        //get all data for compare
//                        for (c in snapshot.children) {
//
//                            phoneNumberDb = c.child("phoneNum").value.toString()
//                            userDb = c.child("name").value.toString()
//                            emailDb = c.child("email").value.toString()
//                            roleDb = c.child("role").value.toString()
//
//
//                            //compare the user id
////                            if (c.child("userid").value.toString() == userId.text.toString() && c.child("password").value.toString() == password.text.toString() ) {
////                                var phoneNumber = c.child("phoneNum").value
////                                var user = c.child("name").value
////                                var email = c.child("email").value
////                                var role = c.child("role").value
////                                userFound = true
////
////                                //display message for authorized user
////                                Toast_LoginSuccess()
////
////                                //redirect to different page
////                                if(role == "Member"){
////                                    //proceed to cust main page
////                                    startActivity(intentCustMain)
////                                }else if(role == "Staff"){
////                                    //proceed to staff main page
////                                    startActivity(intentStaffMain)
////                                }else{
////                                    //proceed to manager main page
////                                    startActivity(intentManagerMain)
////                                }
////
////                            }
//                        }
//
////                        //display message for unauthorized user
////                        if(!userFound){
////                            Toast_LoginFail()
////                        }
//                    }
//
//                    override fun onCancelled(error: DatabaseError) {
//
//                    }
//
//                }
                //myRef.addValueEventListener(getData)

            }
        }

    }

    private fun fetchCurrentUserInfo(){
        val uid = FirebaseAuth.getInstance().uid
        val ref = FirebaseDatabase.getInstance().getReference("/User/$uid")

        val intentCustMain = Intent(this, CustomerMain::class.java)
        intentCustMain.putExtra("email", edittext_login_email.text.toString())

        //intent for staff
        val intentStaffMain = Intent(this, StaffMain::class.java)
        intentStaffMain.putExtra("Role", "Staff")
        intentStaffMain.putExtra("email",  edittext_login_email.text.toString())

        //intent for manager
        val intentManagerMain = Intent(this, StaffMain::class.java)
        intentManagerMain.putExtra("Role", "Manager")
        intentManagerMain.putExtra("email", edittext_login_email.text.toString())

        ref.addListenerForSingleValueEvent(object: ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                currentUser = snapshot.getValue(User::class.java)
                phoneNumberDb = currentUser?.phoneNum
                userDb = currentUser?.name
                emailDb = currentUser?.email
                roleDb = currentUser?.role


                //redirect to different page
                if(roleDb == "Member"){
                    Log.d("Login","Login as Role $roleDb")
                    Log.d("Login","Login as Role $roleDb")
                    Log.d("Login","Login as Member")
                    //proceed to cust main page
                    startActivity(intentCustMain)
                }else if(roleDb == "Staff"){
                    Log.d("Login","Login as Role $roleDb")
                    Log.d("Login","Login as Staff")
                    //proceed to staff main page
                    startActivity(intentStaffMain)
                }else{
                    Log.d("Login","Login as Role $roleDb")
                    Log.d("Login","Login as Manager")
                    //proceed to manager main page
                    startActivity(intentManagerMain)
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }

    //Change by Joan for Firebase Auth (Change user id login --> email login) {Remarks for notice}
    private fun performLoginSuccessful(){
        val email = edittext_login_email.text.toString()
        val password = edittext_login_password.text.toString()

        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                .addOnCompleteListener {
                    if (!it.isSuccessful) return@addOnCompleteListener

                    Log.d("Login", "Successfully logged in: ${it.result?.user?.uid}")

                    //Fetch the info of the current login customer
                    fetchCurrentUserInfo()
                    //display message for authorized user
                    Toast_LoginSuccess()
                }
                .addOnFailureListener {
                    //Use Default Error Message Generate by Firebase Auth
                    Toast.makeText(this, "Failed to log in: ${it.message}", Toast.LENGTH_SHORT).show()
                }
    }
    // message for login fail
//    private fun Toast_LoginFail(){
//        Toast.makeText(this, "Invalid User", Toast.LENGTH_LONG).show()
//    }

    // message for login success
    private fun Toast_LoginSuccess(){
        Toast.makeText(this, "Login Successfully", Toast.LENGTH_LONG).show()
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

    private fun loginBackIcon(){
        val backIv: ImageView = findViewById(R.id.iv_login_backicon)
        backIv.setOnClickListener(){
            val intentMain = Intent(this, MainActivity::class.java)
            startActivity(intentMain)
        }

    }

    //hide system bars
    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)

        if(hasFocus){
            //Hide the status & navigation bar
            decorView.systemUiVisibility = hideSystemBars()
        }
    }

    private fun hideSystemBars(): Int{
        return View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                View.SYSTEM_UI_FLAG_FULLSCREEN or
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
    }
}

