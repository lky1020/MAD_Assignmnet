package com.example.mad_assignment

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {

    //private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.login)
        //setContentView(R.layout.cust_nav)

        //temporary LOGIN detection
        //customer menu navigation
        val btnCustLogin: Button = findViewById(R.id.btnCustLogin)
        btnCustLogin.setOnClickListener(){
            val intent = Intent(this@MainActivity, CustomerMain::class.java)
            startActivity(intent)
        }

        //staff menu navigation
        val btnStaffLogin: Button = findViewById(R.id.btnStaffLogin)
        btnStaffLogin.setOnClickListener(){
            val intent = Intent(this@MainActivity, StaffMain::class.java)
            startActivity(intent)
        }

        /*val database = FirebaseDatabase.getInstance()
        val myRef = database.reference.child("User")

        val btnCreate: Button = findViewById(R.id.btn_createUser)

        btnCreate.setOnClickListener(){
            val user = User("testing1", "testing1")
            myRef.push().setValue(user)
        }

        var getData = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                var sb = StringBuilder();

                for(c in snapshot.children){
                    var name = c.child("username").getValue()
                    sb.append("${name}\n")
                }

                val tvTest: TextView = findViewById(R.id.tvTest)
                tvTest.setText(sb)
            }

            override fun onCancelled(error: DatabaseError) {

            }

        }

        val btnGet: Button = findViewById(R.id.btn_getUser)

        btnGet.setOnClickListener(){
            myRef.addValueEventListener(getData)
            myRef.addListenerForSingleValueEvent(getData)
        }*/

    }

}