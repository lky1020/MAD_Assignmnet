package com.example.mad_assignment.Customer.Chat.messages

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.mad_assignment.Class.User
import com.example.mad_assignment.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_new_message.*
import kotlinx.android.synthetic.main.user_row_new_message.view.*

class NewMessage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_message)

        fetchCurrentUser()
        fetchUsers()

        backPrebtn.setOnClickListener{
            val intent = Intent(this, LatestMessages::class.java)
            startActivity(intent)
        }
    }

    companion object {
        val USER_KEY = "USER_KEY"
        var currentUser: User? = null
    }

    private fun fetchUsers(){
        val ref = FirebaseDatabase.getInstance().getReference("/User")
        ref.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onCancelled(error: DatabaseError) {

            }

            override fun onDataChange(snapshot: DataSnapshot) {
                val adapter = GroupAdapter<ViewHolder>()


                if(currentUser!!.role == "Staff"){

                    for (postSnapshot in snapshot.children) {
                        val allUser: User? = postSnapshot.getValue(User::class.java)
                        if(allUser != null && allUser.role == "Member")
                            adapter.add(UserItem(allUser))
                    }
                }
                else{

                    for (postSnapshot in snapshot.children) {
                        val allUser: User? = postSnapshot.getValue(User::class.java)
                        if(allUser != null && allUser.role == "Staff")
                            adapter.add(UserItem(allUser))
                    }
                }



                adapter.setOnItemClickListener { item, view ->

                    val userItem = item as UserItem
                    val intent = Intent (view.context, ChatLog::class.java)
                    intent.putExtra(USER_KEY, userItem.user)
                    Log.d("NewMessage", userItem.user.toString())
                    startActivity(intent)

                    finish()
                }

                recyclerview_newmessage.adapter = adapter
            }


        })


    }

    private fun fetchCurrentUser(){
        val uid = FirebaseAuth.getInstance().uid
        val ref = FirebaseDatabase.getInstance().getReference("/User/$uid")
        ref.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                currentUser = snapshot.getValue(User::class.java)
                Log.d(LatestMessages.TAG, "Current User ${currentUser?.name}")
                Log.d(LatestMessages.TAG, "Current User ${currentUser?.role}")
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }
}

class UserItem(val user: User): Item<ViewHolder>(){

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.username_testview_new_message.text = user.name

        Picasso.get().load(user.img).into(viewHolder.itemView.imageview_new_message)
    }
    override fun getLayout(): Int {
        return R.layout.user_row_new_message
    }
}