package com.example.mad_assignment.Cust_Staff_Shared.Cust_Staff_Fragments

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.mad_assignment.Class.User
import com.example.mad_assignment.MainActivity
import com.example.mad_assignment.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import java.util.*
import java.util.regex.Pattern

//connect with register.xml
class Register: AppCompatActivity() {

    private lateinit var decorView: View
    lateinit var mRegisterName:EditText
    lateinit var mRegisterPassword1:EditText
    lateinit var mRegisterPassword2:EditText
    lateinit var mRegisterPhoneNum:EditText
    lateinit var mRegisterEmail: EditText
    lateinit var btnSubmitRegister: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.register)

        //For status & navigation bar
        decorView = window.decorView
        decorView.systemUiVisibility = hideSystemBars()

        //back icon action
        val backIcon:ImageView = findViewById(R.id.iv_register_backicon)
        backIcon.setOnClickListener(){
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        //pass data to firebase
        mRegisterName =findViewById(R.id.edittext_register_name)
        mRegisterPassword1  =findViewById(R.id.edittext_register_password1)
        mRegisterPassword2  =findViewById(R.id.edittext_register_password2)
        mRegisterPhoneNum =findViewById(R.id.edittext_register_phonenum)
        mRegisterEmail =findViewById(R.id.edittext_register_email)
        btnSubmitRegister =findViewById(R.id.btn_register_submit)

        //showPassword icon onclick listener
        showorHidePassword()

        //submit register info
        btnSubmitRegister.setOnClickListener(){
           val name = mRegisterName.text.toString().trim()
           val password = mRegisterPassword1.text.toString().trim()
           val phoneNum = mRegisterPhoneNum.text.toString().trim()
           val email = mRegisterEmail.text.toString().trim()

            var allFieldFilled = true
            //validation for each field

            //validation for user name
            if (TextUtils.isEmpty(name)){
                mRegisterName.error = "Enter Name"
                allFieldFilled = false
            }else{
                //set email format and validate
                if(!validateNameFormat(name)){
                    allFieldFilled = false
                }
            }

            //validation for email
            if (TextUtils.isEmpty(email)){
                mRegisterEmail.error = "Enter Email"
                allFieldFilled = false
            }else{
                //set email format and validate
                if(!validateEmailFormat(email)){
                    allFieldFilled = false
                }
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
                if(!validatePhoneNumFormat(phoneNum)){
                    allFieldFilled = false
                }
            }


            //if all validate
            if(allFieldFilled){

                FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener{
                        if(!it.isSuccessful) return@addOnCompleteListener
                        Log.d("Register", "Successfully created user with uid: ${it.result?.user?.uid}")

                        //pass register info to firebase function
                        createUser(name, password, phoneNum, email)

                        //display successful message
                        Toast.makeText(this, "Register Successfully", Toast.LENGTH_LONG).show()
                        //redirect to login page
                        val intent = Intent(this, Login::class.java)
                        startActivity(intent)
                    }
                    .addOnFailureListener{
                        Log.d("Register", "Failed to create user: ${it.message}")
                        Toast.makeText(this, "Failed to create user: ${it.message}", Toast.LENGTH_SHORT).show()
                    }

            }
        }

    }

    //pass new data to firebase
    fun createUser(name:String, password:String, phoneNum:String,email:String){
        val uid = FirebaseAuth.getInstance().uid ?: ""

        //add uid
        val userRef = FirebaseDatabase.getInstance().getReference("/User/$uid")

        val role = "Member"
        val imgURL = "https://firebasestorage.googleapis.com/v0/b/quadcorehms-5b4ed.appspot.com/o/User%2F69q2IG8FZGOAqYTtwmsdZWgqSav1%2F25d59638-bd26-4584-94af-d30214cd0300?alt=media&token=33b8fb71-d66b-4c2d-8549-e613537b7f53"
        val user = User (name,uid,password,phoneNum,email,role, imgURL)


        userRef.setValue(user)

    }

    //Name Format
    private fun validateNameFormat(name:String):Boolean{
        val nameREG = "^([a-zA-Z /.])*$"
        var PATTERN: Pattern = Pattern.compile(nameREG)
        fun CharSequence.isName() : Boolean = PATTERN.matcher(this).find()

        //validate phone num format
        if(!(name.isName())) {
            mRegisterName.error = "Invalid Name Format"
            return false
        }
        return true
    }

    //Phone Number Format
    private fun validatePhoneNumFormat(phoneNum:String):Boolean{
        val phoneREG = "^[0-9]{10,11}$"
        var PATTERN: Pattern = Pattern.compile(phoneREG)
        fun CharSequence.isPhoneNumber() : Boolean = PATTERN.matcher(this).find()

        //validate phone num format
        if(!(phoneNum.isPhoneNumber())){
            if(phoneNum.contains('-')){
                mRegisterPhoneNum.error = "Phone number do not contains '-'"
                return false
            }else{
                mRegisterPhoneNum.error = "Invalid Phone Number"
                return false
            }

        }
        return true
    }

    // validate email function
    private fun validateEmailFormat(email:String):Boolean{
        val emailREG = "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                "\\@" +"[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                "(" +"\\." +"[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +")+"
        var PATTERN: Pattern = Pattern.compile(emailREG)
        fun CharSequence.isEmail() : Boolean = PATTERN.matcher(this).find()

        //validate email format
        if(!(email.isEmail())){
            mRegisterEmail.error = "Invalid Email"
            return false
        }
        return true
    }

    // show password icon function
    private fun showorHidePassword(){

        val showPsdIv1: ImageView = findViewById(R.id.iv_password1_hidePassword)
        val showPsdIv2: ImageView = findViewById(R.id.iv_password2_hidePassword)
        val password1:EditText = findViewById(R.id.edittext_register_password1)
        val password2:EditText = findViewById(R.id.edittext_register_password2)

        //password1 icon function
        showPsdIv1.setOnClickListener(){
            if(password1.transformationMethod == HideReturnsTransformationMethod.getInstance()){
                password1.transformationMethod = PasswordTransformationMethod.getInstance()
                showPsdIv1.setImageResource(R.drawable.ic_hide_psw)
            }else{
                password1.transformationMethod = HideReturnsTransformationMethod.getInstance()
                showPsdIv1.setImageResource(R.drawable.ic_show_psw)
            }
        }

        //password2 icon function
        showPsdIv2.setOnClickListener(){
            if(password2.transformationMethod == HideReturnsTransformationMethod.getInstance()){
                password2.transformationMethod = PasswordTransformationMethod.getInstance()
                showPsdIv2.setImageResource(R.drawable.ic_hide_psw)
            }else{
                password2.transformationMethod = HideReturnsTransformationMethod.getInstance()
                showPsdIv2.setImageResource(R.drawable.ic_show_psw)
            }
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