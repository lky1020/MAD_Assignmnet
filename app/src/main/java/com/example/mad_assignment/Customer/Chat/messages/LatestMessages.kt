package com.example.mad_assignment.Customer.Chat.messages

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.example.mad_assignment.R
import androidx.recyclerview.widget.DividerItemDecoration
import com.example.mad_assignment.Class.User
import com.example.mad_assignment.Cust_Staff_Shared.Cust_Staff_Fragments.Register
import com.example.mad_assignment.Customer.Chat.models.ChatMessage
import com.example.mad_assignment.Customer.Chat.views.LatestMessageRow
import com.example.mad_assignment.CustomerMain
import com.example.mad_assignment.StaffMain
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_latest_messages.*

class LatestMessages : AppCompatActivity() {

    companion object {
        var currentUser: User? = null
        val TAG = "LatestMessages"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_latest_messages)

        recyclerview_latest_messages.adapter = adapter
        recyclerview_latest_messages.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))

        // set item click listener on your adapter
        adapter.setOnItemClickListener { item, view ->
            Log.d(TAG, "123")
            val intent = Intent(this, ChatLog::class.java)

            val row = item as LatestMessageRow
            intent.putExtra(NewMessage.USER_KEY, row.chatPartnerUser)
            startActivity(intent)
        }

        fetchCurrentUser()


        exit_Btn.setOnClickListener {

            if(currentUser?.role == "Staff"){
                val intentStaffMain = Intent(this, StaffMain::class.java)
                intentStaffMain.putExtra("Role", "Staff")
                startActivity(intentStaffMain)
            }
            else if(currentUser?.role == "Manager"){
                val intentManagerMain = Intent(this, StaffMain::class.java)
                intentManagerMain.putExtra("Role", "Manager")
                startActivity(intentManagerMain)
            }
            else{
                val intentCustMain = Intent(this, CustomerMain::class.java)
                startActivity(intentCustMain)
            }

        }

        messageUs_Btn.setOnClickListener{
            Log.d("LatestMessages", "MessageBtn clicked")
            val intent = Intent(this, NewMessage::class.java)
            startActivity(intent)
        }

        ListenForLatestMessages()


        verifyUserIsLoggedIn()
    }

    private fun verifyUserIsLoggedIn() {
        val uid = FirebaseAuth.getInstance().uid
        if (uid == null) {
            val intent = Intent(this, Register::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }
    }

    val latestMessagesMap = HashMap<String, ChatMessage>()

    private fun refreshRecyclerViewMessages() {
        adapter.clear()
        latestMessagesMap.values.forEach {
            adapter.add(LatestMessageRow(it))
        }
    }

    private fun ListenForLatestMessages(){
        val fromId = FirebaseAuth.getInstance().uid
        Log.d("LatestMessages", "Latest Message $fromId ")
        val ref = FirebaseDatabase.getInstance().getReference("/chats/latest-messages/$fromId")
        ref.addChildEventListener(object: ChildEventListener {
            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                val chatMessage = p0.getValue(ChatMessage::class.java) ?: return
                latestMessagesMap[p0.key!!] = chatMessage
                refreshRecyclerViewMessages()
            }

            override fun onChildChanged(p0: DataSnapshot, p1: String?) {
                val chatMessage = p0.getValue(ChatMessage::class.java) ?: return
                latestMessagesMap[p0.key!!] = chatMessage
                refreshRecyclerViewMessages()
            }

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {

            }
            override fun onChildRemoved(p0: DataSnapshot) {

            }
            override fun onCancelled(p0: DatabaseError) {

            }
        })
    }


    val adapter = GroupAdapter<ViewHolder>()


    private fun fetchCurrentUser(){
        val uid = FirebaseAuth.getInstance().uid
        val ref = FirebaseDatabase.getInstance().getReference("/User/$uid")
        ref.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                currentUser = snapshot.getValue(User::class.java)
                Log.d(TAG, "Current User ${currentUser?.name}")
                Log.d(TAG, "Current User ${currentUser?.role}")
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_new_message -> {
                val intent = Intent(this, NewMessage::class.java)
                startActivity(intent)
            }
            R.id.menu_sign_out -> {

                val intent = Intent(this, CustomerMain::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }
        }

        return super.onOptionsItemSelected(item)
    }

}