package com.example.mad_assignment

import android.content.Intent
import android.os.Bundle
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
import com.example.mad_assignment.Class.Staff
import com.example.mad_assignment.Customer.Chat.messages.LatestMessages
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView

//this StaffMain is to enter & run the staff main page --- will call by Login.kt
class StaffMain: AppCompatActivity() {
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var decorView: View

    override fun onCreate(savedInstanceState: Bundle?) {
        //For status & navigation bar
        decorView = window.decorView
        decorView.systemUiVisibility = hideSystemBars()
        //Remove status bar shadow in Navigation View
        window.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)

        // run staff_nav.xml, the main activity of the customer pages
        super.onCreate(savedInstanceState)
        setContentView(R.layout.staff_nav)

        //Use to check permission
        val staffInfo: Staff? = intent.getParcelableExtra("staff")

        // get the value pass from login
        var staffPos: String? = intent.getStringExtra("Role")

        val toolbar: Toolbar = findViewById(R.id.toolbar_staff)
        setSupportActionBar(toolbar)

        //action of the customer service floating button  -- will modify later - redirect to chat bot
        val fab: FloatingActionButton = findViewById(R.id.btn_cust_service_staff)
        fab.setOnClickListener { view ->
            val intent = Intent(this, LatestMessages::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            Toast.makeText(this, "Welcome to Our Chat Room", Toast.LENGTH_LONG).show()
        }

        // get the whole menu drawer id from staff_nav.xml
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout_staff)
        val navView: NavigationView = findViewById(R.id.nav_view_staff)
        val navController = findNavController(R.id.nav_host_fragment_staff)


        if (staffPos != null) {
            detectStaffPosition(staffPos, navView)
        }

        // Passing each menu ID as a set of Ids because each menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home_staff,
                R.id.nav_report,
                R.id.nav_TransHis_staff,
                R.id.nav_profile,
                R.id.nav_logout
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_staff)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    //if is manager then display report menu option
    private fun detectStaffPosition(staffPos:String, navView: NavigationView){
        navView.menu.findItem(R.id.nav_report).isVisible = staffPos == "Manager"
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