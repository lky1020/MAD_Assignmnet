package com.example.mad_assignment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.FragmentController
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.mad_assignment.Customer.ChangeLanguage.Helper.LocalHelper
import com.example.mad_assignment.Customer.Chat.SendNotification.FirebaseService
import com.example.mad_assignment.Customer.Chat.SendNotification.NotificationData
import com.example.mad_assignment.Customer.Chat.SendNotification.PushNotification
import com.example.mad_assignment.Customer.Chat.SendNotification.RetrofitInstance
import com.example.mad_assignment.Customer.Chat.messages.LatestMessages
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.messaging.FirebaseMessaging
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

const val TOPIC = "/topics/myTopic"

//this CustomerMain is to enter & run the customer main page ---- will call by Login.kt
class CustomerMain: AppCompatActivity() {
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var decorView: View


    //To set the base language of the system
    override fun attachBaseContext(newBase: Context?) {
      super.attachBaseContext(LocalHelper.setLocale(newBase!!))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        //For status & navigation bar
        decorView = window.decorView
        decorView.systemUiVisibility = hideSystemBars()
        //Remove status bar shadow in Navigation View
        window.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)

        //run cust_nav.xml, the main activity of the customer pages
        super.onCreate(savedInstanceState)
        setContentView(R.layout.cust_nav)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        var recipientToken = ""

        FirebaseService.sharedPref = getSharedPreferences("sharedPref", Context.MODE_PRIVATE)
        FirebaseInstanceId.getInstance().instanceId.addOnSuccessListener {
            FirebaseService.token = it.token
            recipientToken = it.token
        }
        FirebaseMessaging.getInstance().subscribeToTopic(TOPIC)
        //action of the customer service floating button  -- will modify later - redirect to chat bot
        val fab: FloatingActionButton = findViewById(R.id.btn_cust_service)
        fab.setOnClickListener { view ->

            val title = "New Message"
            val message = "Please Check For Latest Message"
            if(title.isNotEmpty() && message.isNotEmpty() && recipientToken.isNotEmpty()) {
                PushNotification(
                    NotificationData(title, message),
                    recipientToken
                ).also {
                    sendNotification(it)
                }
            }

            val intent = Intent(this, LatestMessages::class.java)
            startActivity(intent)
            Toast.makeText(this, "Welcome to Our Chat Room", Toast.LENGTH_LONG).show()
        }

        // get the whole menu drawer id from cust_nav.xml
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout_cust)
        val navView: NavigationView = findViewById(R.id.nav_view)

        // get the located fragment part from cust_app_bar_main
        val navController = findNavController(R.id.nav_host_fragment)


        // Passing each menu ID as a set of Ids because each menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
                setOf(
                        R.id.nav_home_cust,
                        R.id.nav_facility,
                        R.id.nav_housekeeping,
                        R.id.nav_TransHis_cust,
                        R.id.nav_profile,
                        R.id.nav_logout
                ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

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

    //Create Change Language Options
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.language_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when(item?.itemId){
            R.id.language_en ->
            {
                LocalHelper.setNewLocale(this,"en")
                recreate()
                Toast.makeText(this, "Successfully Change to English", Toast.LENGTH_LONG).show()
            }
            R.id.language_zh ->
            {
                LocalHelper.setNewLocale(this,"zh")
                recreate()
                Toast.makeText(this, "Successfully Change to Chinese", Toast.LENGTH_LONG).show()
            }
            R.id.language_ja ->
            {
                LocalHelper.setNewLocale(this,"ja")
                recreate()
                Toast.makeText(this, "Successfully Change to Japanese", Toast.LENGTH_LONG).show()
            }
        }

        return super.onOptionsItemSelected(item)
    }

    private fun sendNotification(notification: PushNotification) = CoroutineScope(Dispatchers.IO).launch {
        try {
            val response = RetrofitInstance.api.postNotification(notification)
            if(response.isSuccessful) {
                Log.d("SendChat", "Response: ${Gson().toJson(response)}")
            } else {
                Log.e("SendChat", response.errorBody().toString())
            }
        } catch(e: Exception) {
            Log.e("SendChat", e.toString())
        }
    }
}


