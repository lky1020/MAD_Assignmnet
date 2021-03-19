package com.example.mad_assignment

import android.os.Bundle
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

//this StaffMain is to enter & run the staff main page --- will call by Login.kt
class StaffMain: AppCompatActivity() {
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {

        //run staff_nav.xml, the main activity of the customer pages
        super.onCreate(savedInstanceState)
        setContentView(R.layout.staff_nav)

        //get the value pass from previous page
        var staffPos: String? = intent.getStringExtra("StaffPosition")

        val toolbar: Toolbar = findViewById(R.id.toolbar_staff)
        setSupportActionBar(toolbar)

        //action of the customer service floating button  -- will modify later - redirect to chat bot
        val fab: FloatingActionButton = findViewById(R.id.btn_cust_service_staff)
        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
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
        if(staffPos == "manager"){
            navView.getMenu().findItem(R.id.nav_report).setVisible(true);
        }else{ //staffPos == "staff"
            navView.getMenu().findItem(R.id.nav_report).setVisible(false);
        }
    }
}