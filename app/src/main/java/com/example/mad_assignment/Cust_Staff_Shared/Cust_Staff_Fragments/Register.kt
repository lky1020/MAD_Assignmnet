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
import java.util.regex.Pattern
import java.util.regex.Pattern.compile

//connect with register.xml
class Register: AppCompatActivity() {

    lateinit var mRegisterName:EditText
    lateinit var mRegisterUserid:EditText
    lateinit var mRegisterPassword1:EditText
    lateinit var mRegisterPassword2:EditText
    lateinit var mRegisterPhoneNum:EditText
    lateinit var mRegisterEmail: EditText
    lateinit var btnSubmitRegister: Button
    lateinit var mRegisterProgressBar: ProgressBar

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
        mRegisterPassword1  =findViewById(R.id.edittext_register_password1)
        mRegisterPassword2  =findViewById(R.id.edittext_register_password2)
        mRegisterPhoneNum =findViewById(R.id.edittext_register_phonenum)
        mRegisterEmail =findViewById(R.id.edittext_register_email)
        btnSubmitRegister =findViewById(R.id.btn_register_submit)

        mRegisterProgressBar = findViewById(R.id.register_progressBar)

        //submit register info
        btnSubmitRegister.setOnClickListener(){
           val name = mRegisterName.text.toString().trim()
           val userid = mRegisterUserid.text.toString().trim()
           val password = mRegisterPassword1.text.toString().trim()
           val phoneNum = mRegisterPhoneNum.text.toString().trim()
           val email = mRegisterEmail.text.toString().trim()

            var allFieldFilled = true
            //validation for each field
            if (TextUtils.isEmpty(name)){
                mRegisterName.error = "Enter Name"
                allFieldFilled = false
            }
            if (TextUtils.isEmpty(userid)){
                mRegisterUserid.error = "Enter User ID"
                allFieldFilled = false
            }

            //validation for user password
            if (TextUtils.isEmpty(password)){
                mRegisterPassword1.error = "Enter Password"
                allFieldFilled = false
            }else if(mRegisterPassword1.text.toString() != mRegisterPassword2.text.toString()){
                mRegisterPassword2.error = "Invalid Password"
                allFieldFilled = false
            }

            //validation for phone number
            if (TextUtils.isEmpty(phoneNum)){
                mRegisterPhoneNum.error = "Enter Phone Number"
                allFieldFilled = false
            }else{
                //set phone num format and validate
                validatePhoneNumFormat(phoneNum)
            }

            //validation for email
            if (TextUtils.isEmpty(email)){
                mRegisterEmail.error = "Enter Email"
                allFieldFilled = false
            }else{
                //set email format and validate
                validateEmailFormat(email)

            }

            //if all validate
            if(allFieldFilled){
                //pass register info to firebase function
                createUser(name, userid, password, phoneNum, email)
            }

        }


    }

    //pass new data to firebase
    fun createUser(name:String, userid:String, password:String, phoneNum:String,email:String){

        val database: FirebaseDatabase = FirebaseDatabase.getInstance()
        val userRef: DatabaseReference = database.getReference("User")

        userRef.child(phoneNum).child("Name").setValue(name)
        userRef.child(phoneNum).child("Password").setValue(password)
        userRef.child(phoneNum).child("Email").setValue(email)
        userRef.child(phoneNum).child("UserID").setValue(userid)

        //redirect to login page
        val intent = Intent(this, Login::class.java)
        startActivity(intent)
    }

    //Phone Number Format
    private fun validatePhoneNumFormat(phoneNum:String){
        val phoneREG = "^[0-9]{10,11}$"
        var PATTERN: Pattern = Pattern.compile(phoneREG)
        fun CharSequence.isPhoneNumber() : Boolean = PATTERN.matcher(this).find()

        //validate phone num format
        if(!(phoneNum.isPhoneNumber())){
            if(phoneNum.contains('-')){
                mRegisterPhoneNum.error = "Phone number do not contains '-'"
            }else{
                mRegisterPhoneNum.error = "Invalid Phone Number"
            }

        }
    }

    private fun validateEmailFormat(email:String){
        val emailREG = "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                "\\@" +"[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                "(" +"\\." +"[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +")+"
        var PATTERN: Pattern = Pattern.compile(emailREG)
        fun CharSequence.isEmail() : Boolean = PATTERN.matcher(this).find()

        //validate email format
        if(!(email.isEmail())){
            mRegisterEmail.error = "Invalid Email"
        }
    }


}