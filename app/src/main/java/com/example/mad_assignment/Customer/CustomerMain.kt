package com.example.mad_assignment

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar


//this CustomerMain is to enter & run the customer main page ---- will call by Login.kt
class CustomerMain: AppCompatActivity() {
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var decorView: View

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

        //change language onclick
        val changeLanguage:TextView = findViewById(R.id.testTV_changelanguage)
        changeLanguage.setOnClickListener(){
            val tvTest:TextView = findViewById(R.id.tvTest)
            if(tvTest.text == "Change English"){

                tvTest.text = "Change Japanese"
            }else{
                tvTest.text = "Change English"
            }

        }

        //action of the customer service floating button  -- will modify later - redirect to chat bot
        val fab: FloatingActionButton = findViewById(R.id.btn_cust_service)
        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
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
/*
    //change menu setting icon
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.change_language_setting, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val eng_set:MenuItem = findViewById(R.id.english_settings)
        while(item == eng_set){
            val tvTest:TextView = findViewById(R.id.tvTest)
            tvTest.text = "Change English"
        }
        return super.onOptionsItemSelected(item)
    }

 */

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
}
