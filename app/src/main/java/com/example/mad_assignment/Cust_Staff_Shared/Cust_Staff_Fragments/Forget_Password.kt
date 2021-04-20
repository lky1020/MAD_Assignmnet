package com.example.mad_assignment.Cust_Staff_Shared.Cust_Staff_Fragments

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.mad_assignment.R
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import java.util.regex.Pattern

class Forget_Password: AppCompatActivity() {

    lateinit var backBtn: ImageView
    lateinit var mEmail: EditText
    lateinit var mSendMail: Button
    lateinit var mAuth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.forget_password)

        backBtn = findViewById(R.id.iv_login_backicon)
        mEmail = findViewById(R.id.edittext_get_email)
        mSendMail = findViewById(R.id.btn_send_email)

        mAuth = FirebaseAuth.getInstance()

        //back icon action
        backBtn.setOnClickListener(){
            val intent = Intent(this, Login::class.java)
            startActivity(intent)
        }

        //send email btn
        mSendMail.setOnClickListener{
            val email = mEmail.text.toString().trim()
            val emailREG = "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                    "\\@" +"[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                    "(" +"\\." +"[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +")+"
            var PATTERN: Pattern = Pattern.compile(emailREG)
            fun CharSequence.isEmail() : Boolean = PATTERN.matcher(this).find()

            if(email.isEmpty()){
                mEmail.error = "Enter Email"

            }else if(!(email.isEmail())) { //validate email format
                mEmail.error = "Invalid Email Format"
            }else{
                //proceed to send email
                forgetPasswordEmail(email)
            }
        }

    }

    private fun forgetPasswordEmail(email: String) {
        mAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener { task: Task<Void> ->
                    if(task.isSuccessful){ //task.isComplete ||
                        val loginIntent = Intent(this, Login::class.java)
                        startActivity(loginIntent)

                        Toast.makeText(applicationContext, "Please check your inbox to Reset Password and login again", Toast.LENGTH_LONG).show()

                    }else
                    {
                        Toast.makeText(applicationContext, "Invalid Email! Pls enter again", Toast.LENGTH_LONG).show()

                    }

                }
            .addOnCanceledListener {
                Toast.makeText(applicationContext, "No such user! Pls enter again", Toast.LENGTH_LONG).show()
            }



    }

}