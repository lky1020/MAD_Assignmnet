package com.example.mad_assignment

import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val createUserBtn: Button = findViewById(R.id.btn_createUser)

        createUserBtn.setOnClickListener {
            val username = "lky1020"
            val password = "lky1020"

            createUser(username, password)
        }
    }

    private fun createUser(username: String, password: String){
        val db = FirebaseFirestore.getInstance()

        val user: MutableMap<String, Any> = HashMap()
        user["username"] = username
        user["password"] = password

        db.collection("users").document(username)
                .set(user)
                .addOnSuccessListener {
                    Toast.makeText(this, "Added Successfully", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener() {
                    Toast.makeText(this, "Added Unsuccessfully", Toast.LENGTH_SHORT).show()
                }

    }
}