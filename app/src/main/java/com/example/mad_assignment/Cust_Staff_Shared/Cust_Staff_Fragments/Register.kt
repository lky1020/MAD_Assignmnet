package com.example.mad_assignment.Cust_Staff_Shared.Cust_Staff_Fragments

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import com.example.mad_assignment.Class.User
import com.example.mad_assignment.MainActivity
import com.example.mad_assignment.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

//connect with register.xml
class Register: AppCompatActivity() {

    lateinit var mRegisterName:EditText
    lateinit var mRegisterUserid:EditText
    lateinit var mRegisterPassword:EditText
    lateinit var mRegisterGender:EditText //temp
    lateinit var mRegisterPhoneNum:EditText
    lateinit var mRegisterEmail: EditText
    lateinit var btnSubmitRegister: Button
    lateinit var mRegisterProgressBar: ProgressBar

    lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.register)

        //back icon action
        val backIcon:ImageView = findViewById(R.id.iv_register_backicon)
        backIcon.setOnClickListener(){
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        //pass data to firebase
        mRegisterName =findViewById(R.id.edittext_register_name)
        mRegisterUserid =findViewById(R.id.edittext_register_userid)
        mRegisterPassword  =findViewById(R.id.edittext_register_password2)
        mRegisterGender = findViewById(R.id.temp_gender)  ///temp
        mRegisterPhoneNum =findViewById(R.id.edittext_register_phonenum)
        mRegisterEmail =findViewById(R.id.edittext_register_email)
        btnSubmitRegister =findViewById(R.id.btn_register_submit)

        mRegisterProgressBar = findViewById(R.id.register_progressBar)

        //submit register info
        btnSubmitRegister.setOnClickListener(){
           val name = mRegisterName.text.toString().trim()
           val userid = mRegisterUserid.text.toString().trim()
           val password = mRegisterPassword.text.toString().trim()
           val gender = mRegisterGender.text.toString().trim()
           val phoneNum = mRegisterPhoneNum.text.toString().trim()
           val email = mRegisterEmail.text.toString().trim()

            //validate them
            if (TextUtils.isEmpty(name)){
                mRegisterName.error = "Enter Name"
            }
            if (TextUtils.isEmpty(userid)){
                mRegisterUserid.error = "Enter User ID"
            }
            if (TextUtils.isEmpty(password)){
                mRegisterPassword.error = "Enter Password"
            }
            if (TextUtils.isEmpty(gender)){
                mRegisterGender.error = "Select Gender"
            }
            if (TextUtils.isEmpty(phoneNum)){
                mRegisterPhoneNum.error = "Enter Phone Number"
            }
            if (TextUtils.isEmpty(email)){
                mRegisterEmail.error = "Enter Email"
            }

            //pass register info to firebase function
            createUser(name, userid, password,gender, phoneNum,email)
        }


    }

    fun createUser(name:String, userid:String, password:String,gender:String, phoneNum:String,email:String){

       //add database then use it and push to firebase
        // Write a message to the database
        val database: FirebaseDatabase = FirebaseDatabase.getInstance()
        val userRef: DatabaseReference = database.getReference("User")

        userRef.child(userid).child("Name").setValue(name)
        userRef.child(userid).child("Password").setValue(password)
        userRef.child(userid).child("Gender").setValue(gender)
        userRef.child(userid).child("PhoneNum").setValue(phoneNum)
        userRef.child(userid).child("Email").setValue(email)



    }

}