package com.egci428.feedme

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBar
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.egci428.feedme.R.id.*
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity()  {

    private lateinit var mDrawerLayout: DrawerLayout
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)


        auth = FirebaseAuth.getInstance()

        val toolbar: Toolbar = findViewById(R.id.tbHome)
        setSupportActionBar(toolbar)
        val actionbar: ActionBar? = supportActionBar
        actionbar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.ic_menu)
            //setLogo(R.drawable.ic_logo_circle)
        }

        Restaurant.setOnClickListener {
            val intent = Intent(this, ListRestaurant::class.java )
            intent.putExtra("choice",1)
            startActivity(intent)

        }

        Cafe.setOnClickListener {
            val intent = Intent(this, ListRestaurant::class.java )
            intent.putExtra("choice",2)
            startActivity(intent)

        }

        Delivery.setOnClickListener {
            val intent = Intent(this, ListRestaurant::class.java )
            intent.putExtra("choice",3)
            startActivity(intent)

        }

        Takeaway.setOnClickListener {
            val intent = Intent(this, ListRestaurant::class.java )
            intent.putExtra("choice",4)
            startActivity(intent)

        }

        mDrawerLayout = findViewById(R.id.drawer_layout)
        val navigationView: NavigationView = findViewById(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener { menuItem ->
            // set item as selected to persist highlight
            when(menuItem.itemId){
                R.id.logoutDraw -> {
                    auth.signOut()
                    val user = auth.currentUser
                    if(user == null){
                        val intent = Intent(this,LoginActivity::class.java)
                        startActivity(intent)
                        finish()
                    }

                }
            }
            // close drawer when item is tapped
            mDrawerLayout.closeDrawers()

            // Add code here to update the UI based on the item selected
            // For example, swap UI fragments here
            true

        }



        mDrawerLayout.addDrawerListener(
                object : DrawerLayout.DrawerListener {
                    override fun onDrawerSlide(drawerView: View, slideOffset: Float) {
                        // Respond when the drawer's position changes
                    }

                    override fun onDrawerOpened(drawerView: View) {
                        // Respond when the drawer is opened
                    }

                    override fun onDrawerClosed(drawerView: View) {
                        // Respond when the drawer is closed
                    }

                    override fun onDrawerStateChanged(newState: Int) {
                        // Respond when the drawer motion state changes
                    }
                }
        )
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                mDrawerLayout.openDrawer(GravityCompat.START)
                true
            }
            else -> super.onOptionsItemSelected(item)
            }

    }




}
