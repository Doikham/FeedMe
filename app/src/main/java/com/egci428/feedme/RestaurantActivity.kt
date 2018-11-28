package com.egci428.feedme

import android.content.Context
import android.content.pm.PackageManager
import android.content.pm.PackageManager.*
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v7.app.ActionBar
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.activity_restaurant.*
import java.text.NumberFormat

class RestaurantActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private var locationManager: LocationManager? = null
    private var listener: LocationListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_restaurant)
        //supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        val toolbar: Toolbar = findViewById(R.id.tbRes)
        setSupportActionBar(toolbar)
        val actionbar: ActionBar? = supportActionBar
        actionbar?.apply {
            setDisplayHomeAsUpEnabled(true)
        }

        val name = intent.getStringExtra("rname")
        resName.text = name

        val address = intent.getStringExtra("raddress")
        resAdd.text = address

        val phone = intent.getStringExtra("rphonenumber")
        resPhone.text = phone

        val price = intent.getIntExtra("rpricelevel",0)
        resPrice.text = price.toString()

        val rating = intent.getFloatExtra("rrating",0.0F)
        resRating.text = rating.toString()

        val lat = intent.getFloatExtra("rlat",0.0F)
        val long = intent.getFloatExtra("rlong",0.0F)
        resLatlng.text = lat.toString()+","+long.toString()

        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

        listener = object: LocationListener{
            override fun onLocationChanged(location: Location) {

            }

            override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {

            }

            override fun onProviderEnabled(provider: String?) {

            }

            override fun onProviderDisabled(provider: String?) {

            }
        }
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        showBtn.setOnClickListener {
                val latLng = LatLng(lat.toString().toDouble(),long.toString().toDouble())
                val markerOptions = MarkerOptions().position(latLng)
                mMap.addMarker(markerOptions)
                mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng))
            }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_detail, menu)
        return true
    }

//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        val id = item.getItemId()
//        if (id == android.R.id.home) {
//            finish()
//        }
//        return super.onOptionsItemSelected(item)
//    }
}
