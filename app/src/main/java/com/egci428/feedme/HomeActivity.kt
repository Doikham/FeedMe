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
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.egci428.feedme.R.id.*
import com.google.firebase.auth.FirebaseAuth
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.activity_info.*
import kotlinx.android.synthetic.main.nav_header.*

class HomeActivity : AppCompatActivity()  {

    private lateinit var mDrawerLayout: DrawerLayout
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        auth = FirebaseAuth.getInstance()

        val user = auth.currentUser
        user?.let {
            // Name, email address, and profile photo Url
            val name = user.displayName
            val email = user.email
            val photoUrl = user.photoUrl
            val telephone = user.phoneNumber
            // Check if user's email is verified
            val emailVerified = user.isEmailVerified
            // The user's ID, unique to the Firebase project. Do NOT use this value to
            // authenticate with your backend server, if you have one. Use
            // FirebaseUser.getToken() instead.
            val uid = user.uid
            //profileImg.setImageURI(photoUrl)
        }

        val toolbar: Toolbar = findViewById(R.id.tbHome)
        setSupportActionBar(toolbar)
        val actionbar: ActionBar? = supportActionBar
        actionbar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.ic_menu)
            //setLogo(R.drawable.ic_logo_circle)
        }

        val userHelp: String = ("Choose the category below to start searching for nearby restaurant")
        userMsg.text = userHelp

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
        val hView =  navigationView.inflateHeaderView(R.layout.nav_header)
        //val uimg:ImageView = hView.findViewById(R.id.userImgNav)
        val uuser:TextView = hView.findViewById(R.id.usernameNav)
        Picasso.with(this).load(user!!.photoUrl).into(userImgNav)
        uuser.text = user!!.displayName
        
        navigationView.setNavigationItemSelectedListener { menuItem ->
            // set item as selected to persist highlight
            when(menuItem.itemId){
                R.id.logoutDraw -> {
                    auth.signOut()
                    val user = auth.currentUser
                    if (user == null) {
                        val intent = Intent(this, LoginActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                }
                R.id.settingDraw -> {
                    val intent = Intent(this, SettingActivity::class.java)
                    startActivity(intent)
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
