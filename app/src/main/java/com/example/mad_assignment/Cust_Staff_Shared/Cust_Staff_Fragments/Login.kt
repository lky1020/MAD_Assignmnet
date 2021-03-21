package com.example.mad_assignment.Cust_Staff_Shared.Cust_Staff_Fragments

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.example.mad_assignment.Class.User
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

    //temperary variable -- need REMOVE after use firebase
    val role:String? = "manager"
    internal var user: User? = null  // declare user object outside onCreate Method

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login)

        //variable
        var loginFail = false
        val userId: EditText = findViewById(R.id.edittext_login_userid)
        val showPsdIv: ImageView = findViewById(R.id.iv_login_hidePassword)
        val password: EditText = findViewById(R.id.edittext_login_password)
        val btnLogin: Button = findViewById(R.id.btn_login)

        //if show or hide Password icon is clicked
        showPsdIv.setOnClickListener(){
            showorHideLoginPassword(showPsdIv, password)
        }


        //check if userid and password is filled
        if(userId != null && password != null){
            //get data from database
            loginGetDataFirebase()
            //compare if true


        }else{
            if (TextUtils.isEmpty(userId.text.toString())){
                userId.error = "Enter User ID"
            }else{
                password.error = "Enter Password"
            }
            loginFail = true
        }

        //if login fail then remain in same page
        if(!loginFail){
            //login btn
            btnLogin.setOnClickListener() {

                //compare is the role is staff, member or manager
                val intent = Intent(this, CustomerMain::class.java)
                startActivity(intent)
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
    private fun showorHideLoginPassword(showPsdIv: ImageView, password: EditText ){
        //password1 icon function
        if(password.transformationMethod == HideReturnsTransformationMethod.getInstance()){
            password.transformationMethod = PasswordTransformationMethod.getInstance()
            showPsdIv.setImageResource(R.drawable.ic_hide_psw)
        }else{
            password.transformationMethod = HideReturnsTransformationMethod.getInstance()
            showPsdIv.setImageResource(R.drawable.ic_show_psw)
        }
    }

    //get data from firebase

    private fun loginGetDataFirebase(){

        //NEED GET DATA FROM FIREBASE
    }

}