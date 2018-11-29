package com.egci428.feedme

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.google.android.gms.location.places.GeoDataClient
import com.google.android.gms.location.places.PlaceDetectionClient
import com.google.android.gms.location.places.Places
import kotlinx.android.synthetic.main.activity_restaurant.*
import java.text.NumberFormat
import android.provider.MediaStore.Images.Media.getBitmap
import android.graphics.Bitmap
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
//import android.support.test.orchestrator.junit.BundleJUnitUtils.getResult
import com.google.android.gms.location.places.PlacePhotoResponse
import com.google.android.gms.tasks.Task
import android.support.annotation.NonNull
import android.support.v7.app.ActionBar
import android.support.v7.widget.Toolbar
import android.util.Log
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.location.places.ui.PlacePicker.getAttributions
import com.google.android.gms.location.places.PlacePhotoMetadata
import com.google.android.gms.location.places.PlacePhotoMetadataBuffer
import com.google.android.gms.location.places.PlacePhotoMetadataResponse
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class RestaurantActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private var locationManager: LocationManager? = null
    private var listener: LocationListener? = null

    val user = FirebaseAuth.getInstance().currentUser
    //need to set to false
    var setFav = false
    var database = FirebaseDatabase.getInstance()
    var myRef = database.getReference()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_restaurant)

        val toolbar: Toolbar = findViewById(R.id.tbRes)
        setSupportActionBar(toolbar)
        val actionbar: ActionBar? = supportActionBar
        actionbar?.apply {
            setDisplayHomeAsUpEnabled(true)
        }

        Log.d("kkkkk","Started")

        var mGeoDataClient2 = Places.getGeoDataClient(this,null) as GeoDataClient

        // Construct a PlaceDetectionClient.
        var mPlaceDetectionClient = Places.getPlaceDetectionClient(this, null) as PlaceDetectionClient




        val name = intent.getStringExtra("rname")
        val address = intent.getStringExtra("raddress")
        val phone = intent.getStringExtra("rphonenumber")
        val price = intent.getIntExtra("rpricelevel",0)
        val rating = intent.getFloatExtra("rrating",0.0F)
        val lat = intent.getDoubleExtra("rlat",0.0)
        val long = intent.getDoubleExtra("rlong",0.0)
        val id = intent.getStringExtra("rid")

    //Set resources on page
        resName.text = name
        resAdd.text = address
        resPhone.text = phone
        resPrice.text = price.toString()
        resRating.text = rating.toString()
        resLat.text = lat.toString()
        resLong.text = long.toString()

        if(setFav == true)
        {
            setFavorite(name,address,id,phone,price,rating,lat,long)
        }


        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

        listener = object: LocationListener {
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
            val latLng = LatLng(lat,long)
            val markerOptions = MarkerOptions().position(latLng)
            mMap.addMarker(markerOptions)
            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng))
        }

        /*val img = intent.getIntExtra("rimg", 0)
        resImg.setImageResource(img)*/
        getPhotos(id, mGeoDataClient2)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_detail, menu)
        return true
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
    }
//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        val id = item.getItemId()
//        if (id == android.R.id.home) {
//            finish()
//        }
//        return super.onOptionsItemSelected(item)
//    }


    private fun getPhotos(placeId: String, mGeoDataClient2: GeoDataClient) {
        val photoMetadataResponse = mGeoDataClient2.getPlacePhotos(placeId)
        photoMetadataResponse.addOnCompleteListener { task ->
            // Get the list of photos.
            val photos = task.result
            // Get the PlacePhotoMetadataBuffer (metadata for all of the photos).
            val photoMetadataBuffer = photos.photoMetadata
            // Get the first photo in the list.
            if(photoMetadataBuffer.count() != 0) {
                val photoMetadata = photoMetadataBuffer.get(0)
                // Get the attribution text.
                val attribution = photoMetadata.attributions
                // Get a full-size bitmap for the photo.
                val photoResponse = mGeoDataClient2.getPhoto(photoMetadata)
                photoResponse.addOnCompleteListener { pic ->
                    val photo = pic.result
                    val bitmap = photo.bitmap
                    resImg.setImageBitmap(bitmap)

                }
            }
            photoMetadataBuffer.release()
        }
    }

    fun setFavorite(name: String,address: String,id: String,phone: String, price: Int, rating: Float, lat: Double, long: Double){

        val uid = user!!.uid
        Log.d("kkkkk",uid)
        val restaurant = RestaurantDB(name,address,id,phone,price,rating,lat,long)

        myRef.child(uid).child(id).setValue(restaurant)


    }
}
