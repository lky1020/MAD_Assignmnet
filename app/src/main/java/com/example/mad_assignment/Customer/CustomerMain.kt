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
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.mad_assignment.Customer.ChangeLanguage.Helper.LocalHelper
import com.example.mad_assignment.Customer.Chat.messages.LatestMessages
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar


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


        //action of the customer service floating button  -- will modify later - redirect to chat bot
        val fab: FloatingActionButton = findViewById(R.id.btn_cust_service)
        fab.setOnClickListener { view ->

            val intent = Intent(this, LatestMessages::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
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
}


