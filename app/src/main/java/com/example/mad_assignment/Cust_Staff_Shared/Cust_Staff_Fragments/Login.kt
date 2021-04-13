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
import java.util.regex.Pattern

//connect with login.xml
class Login: AppCompatActivity() {
    private lateinit var decorView: View
    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText

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
        etEmail= findViewById(R.id.edittext_login_email)
        etPassword = findViewById(R.id.edittext_login_password)
        var showPsdIv: ImageView = findViewById(R.id.iv_login_hidePassword)
        var btnLogin: Button = findViewById(R.id.btn_login)


        //if show or hide Password icon is clicked
        showPsdIv.setOnClickListener {
            showorHideLoginPassword(showPsdIv, etPassword)
        }

        //back main page icon
        loginBackIcon()


        //login btn
        btnLogin.setOnClickListener {

            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString().trim()

            //check if userid and password is filled
            if (email.isEmpty()) {
                etEmail.error = "Enter User Email"
            }else if(!validateEmailFormat(email)){ //set email format and validate
                    etEmail.error = "Invalid Email!"
            }
            else if (password.isEmpty()) {
                etPassword.error = "Enter Password"
            } else { //both field filled
                performLoginSuccessful()
            }
        }

    }

    //Login Validation in Firebase Auth
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
                Toast.makeText(this, "Login Successfully", Toast.LENGTH_LONG).show()
            }
            .addOnFailureListener {
                //Use Default Error Message Generate by Firebase Auth
                Toast.makeText(this, "Failed to log in: ${it.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun fetchCurrentUserInfo(){
        val uid = FirebaseAuth.getInstance().uid
        val ref = FirebaseDatabase.getInstance().getReference("/User/$uid")

        ref.addListenerForSingleValueEvent(object: ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                currentUser = snapshot.getValue(User::class.java)
               // phoneNumberDb = currentUser?.phoneNum
              //  userDb = currentUser?.name
              //  emailDb = currentUser?.email
                roleDb = currentUser?.role

                //redirect to specific role's main page
                Log.d("Login","Login as Role $roleDb")
                redirectToRoleMainPage(roleDb!!)
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    //redirect to specific role's main page
    private fun redirectToRoleMainPage(roleDb:String){
        //redirect to different page
        when (roleDb) {
            "Member" -> {
                Log.d("Login","Login as Member")
                //proceed to cust main page
                val intentCustMain = Intent(this, CustomerMain::class.java)
                intentCustMain.putExtra("email", edittext_login_email.text.toString())
                startActivity(intentCustMain)
            }
            "Staff" -> {
                Log.d("Login","Login as Staff")
                //proceed to staff main page
                val intentStaffMain = Intent(this, StaffMain::class.java)
                intentStaffMain.putExtra("Role", "Staff")
                intentStaffMain.putExtra("email",  edittext_login_email.text.toString())
                startActivity(intentStaffMain)
            }
            else -> {
                Log.d("Login","Login as Manager")
                //proceed to manager main page
                val intentManagerMain = Intent(this, StaffMain::class.java)
                intentManagerMain.putExtra("Role", "Manager")
                intentManagerMain.putExtra("email", edittext_login_email.text.toString())
                startActivity(intentManagerMain)
            }
        }
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
            return false
        }
        return true
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

