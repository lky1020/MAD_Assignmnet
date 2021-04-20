package com.example.mad_assignment.Cust_Staff_Shared.Cust_Staff_Fragments

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.mad_assignment.Class.Staff
import com.example.mad_assignment.Class.User
import com.example.mad_assignment.CustomerMain
import com.example.mad_assignment.MainActivity
import com.example.mad_assignment.R
import com.example.mad_assignment.StaffMain
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.login.*
import java.util.regex.Pattern

//connect with login.xml
class Login: AppCompatActivity() {
    private lateinit var decorView: View
    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText

    var roleDb: String? = null

    // fetch current user information
    companion object {
        var currentUser: User? = null

        //for static purpose - to store email & password later
        var PREFS_NAME: String? = "PrefsFile"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login)

        //For status & navigation bar
        decorView = window.decorView
        decorView.systemUiVisibility = hideSystemBars()

        //variable
        etEmail = findViewById(R.id.edittext_login_email)
        etPassword = findViewById(R.id.edittext_login_password)
        var showPsdIv: ImageView = findViewById(R.id.iv_login_hidePassword)
        var btnLogin: Button = findViewById(R.id.btn_login)

        var chkRmbMe: CheckBox = findViewById(R.id.checkBox_rmbMe)
        var preferences: SharedPreferences = getSharedPreferences("chkBox", MODE_PRIVATE)
        var chkBox: String? = preferences.getString("chkRmbMe", "")

        //detect if checkbox is true, then direct login
        if (chkBox.equals("true")) {
            fetchCurrentUserInfo()

        } else if (chkBox.equals("false")) {
            //get the email & password that the user want to rmb
            getPreferencesData()

            Toast.makeText(this, "Please Log in to proceed", Toast.LENGTH_SHORT).show()
        }


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
            } else if (!validateEmailFormat(email)) { //set email format and validate
                etEmail.error = "Invalid Email!"
            } else if (password.isEmpty()) {
                etPassword.error = "Enter Password"
            } else { //both field filled

                //store remembered email & password into the PREFS_NAME
                var preferencesText = getSharedPreferences(PREFS_NAME, MODE_PRIVATE)

                if(chkRmbMe.isChecked){
                    var editor: SharedPreferences.Editor = preferencesText.edit()
                    editor.putString("pref_name", email)
                    editor.putString("pref_pass", password)
                    editor.apply()


                }else{
                    preferencesText.edit().clear().apply()
                }

                //proceed to login
                performLoginSuccessful(email, password)
            }

        }

        //forget password
        val tv_forgetPasword: TextView = findViewById(R.id.tv_forgetPasword)
        tv_forgetPasword.setOnClickListener {
            val forgetIntent = Intent(applicationContext, Forget_Password::class.java)
            startActivity(forgetIntent)

        }

        //remember me
        chkRmbMe.setOnCheckedChangeListener { _, isChecked ->

            if (isChecked) {

                var preferences: SharedPreferences = getSharedPreferences("chkBox", MODE_PRIVATE)
                var editor: SharedPreferences.Editor = preferences.edit()
                editor.putString("chkRmbMe", "true")
                editor.apply()
                Toast.makeText(this, "Checked", Toast.LENGTH_SHORT).show()

            } else if (!(isChecked)) {

                var preferences: SharedPreferences = getSharedPreferences("chkBox", MODE_PRIVATE)
                var editor: SharedPreferences.Editor = preferences.edit()
                editor.putString("chkRmbMe", "false")
                editor.apply()
                Toast.makeText(this, "Unchecked", Toast.LENGTH_SHORT).show()
            }


        }
    }

    //get the data (email & password) that store in the PREFS_NAME
    private fun getPreferencesData(){
        var sharePref:SharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE)
        if(sharePref.contains("pref_name")){
            val name: String? = sharePref.getString("pref_name", "not found.")
            etEmail.setText(name)

            if(sharePref.contains("pref_pass")){
                val pass: String? = sharePref.getString("pref_pass", "not found.")
                etPassword.setText(pass)
            }
        }
    }


    //Login Validation in Firebase Auth
    private fun performLoginSuccessful(email:String, password:String){
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (!it.isSuccessful) return@addOnCompleteListener

                //Fetch the info of the current login customer
                fetchCurrentUserInfo()

                Log.d("Login", "Successfully logged in: ${it.result?.user?.uid}")
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
                roleDb = currentUser?.role


                if(currentUser!!.password != etPassword.text.toString() && etPassword.text.toString() != ""){
                    //change password
                    val user = User(currentUser!!.name, currentUser!!.uid, etPassword.text.toString(), currentUser!!.phoneNum, currentUser!!.email, currentUser!!.role, currentUser!!.img)
                    ref.setValue(user)
                }

                //redirect to specific role's main page
                Log.d("Login","Login as Role $roleDb")
                redirectToRoleMainPage(currentUser!!, roleDb!!)
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })

    }

    //redirect to specific role's main page
    private fun redirectToRoleMainPage(currentUser: User, roleDb:String){
        //redirect to different page
        when (roleDb) {
            "Member" -> {
                Log.d("Login","Login as Member")
                //proceed to cust main page
                val intentCustMain = Intent(this, CustomerMain::class.java)
                startActivity(intentCustMain)
            }
            "Staff" -> {
                Log.d("Login","Login as Staff")

                //Update staff status to online
                updateStaffStatus(currentUser)

                //proceed to staff main page
                val intentStaffMain = Intent(this, StaffMain::class.java)
                intentStaffMain.putExtra("Role", "Staff")
                //intentStaffMain.putExtra("email",  edittext_login_email.text.toString())
                //intentStaffMain.putExtra("user", currentUser)
                startActivity(intentStaffMain)
            }
            else -> {
                Log.d("Login","Login as Manager")
                //proceed to manager main page
                val intentManagerMain = Intent(this, StaffMain::class.java)
                intentManagerMain.putExtra("Role", "Manager")
                //intentManagerMain.putExtra("email", edittext_login_email.text.toString())
                startActivity(intentManagerMain)
            }
        }

        Toast.makeText(this, "Log in to ${currentUser!!.name} account", Toast.LENGTH_SHORT).show()
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

    private fun updateStaffStatus(currentUser: User){

        val query: Query = FirebaseDatabase.getInstance().getReference("Staff")
                .orderByChild("uid")
                .equalTo(currentUser.uid)

        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){

                    for(i in snapshot.children){
                        val staff = i.getValue(Staff::class.java)

                        if (staff != null) {
                            if(staff.uid == currentUser.uid){
                                val updateStaffStatus = Staff(currentUser.name, staff.id, staff.email, staff.password, staff.phoneNum, staff.img, staff.role,
                                        "Online", staff.accessRoom, staff.accessServicesFacilities,
                                        staff.accessHousekeeping, staff.accessCheckInOut, staff.uid)

                                snapshot.ref.child("${staff.name} - ${staff.uid}").setValue(updateStaffStatus)
                            }
                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }
}

