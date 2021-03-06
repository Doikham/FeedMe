package com.egci428.feedme

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
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
import android.net.Uri
//import android.support.test.orchestrator.junit.BundleJUnitUtils.getResult
import com.google.android.gms.location.places.PlacePhotoResponse
import com.google.android.gms.tasks.Task
import android.support.annotation.NonNull
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.ActionBar
import android.support.v7.widget.Toolbar
import android.util.Log
import android.widget.Toast
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
import com.google.firebase.database.*

class RestaurantActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private var locationManager: LocationManager? = null
    private var listener: LocationListener? = null

    val user = FirebaseAuth.getInstance().currentUser
    //need to set to false
    var setFav: Boolean = false
    var database = FirebaseDatabase.getInstance()
    var myRef = database.getReference()


//Start Restaurant Activity
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_restaurant)

        tbRes.setNavigationOnClickListener{
            finish()
        }
        tbRes.setNavigationIcon(R.drawable.ic_keyboard_arrow_left_black_24dp)

        Log.d("kkkkk","Started")

        var mGeoDataClient2 = Places.getGeoDataClient(this,null) as GeoDataClient

        // Construct a PlaceDetectionClient.
        var mPlaceDetectionClient = Places.getPlaceDetectionClient(this, null) as PlaceDetectionClient


        //Get value from intent
        val name = intent.getStringExtra("rname")
        val address = intent.getStringExtra("raddress")
        val phone = intent.getStringExtra("rphonenumber")
        val price = intent.getIntExtra("rpricelevel",0)
        val rating = intent.getFloatExtra("rrating",0.0F)
        val lat = intent.getDoubleExtra("rlat",0.0)
        val long = intent.getDoubleExtra("rlong",0.0)
        val id = intent.getStringExtra("rid")


    //Call functionality
        callBtn.setOnClickListener {

            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CALL_PHONE)
                    != PackageManager.PERMISSION_GRANTED) {
                // Permission is not granted

                //Toast.makeText(this, "Please enable location services in order to continue",Toast.LENGTH_SHORT).show()
                val permissions = arrayOf(android.Manifest.permission.CALL_PHONE)
                ActivityCompat.requestPermissions(this, permissions,0)

                recreate()

            }
            else {
                val intent = Intent(Intent.ACTION_CALL)
                intent.data = Uri.parse("tel:$phone")
                startActivity(intent)
            }
        }

    //Find in DB
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Get Post object and use the values to update the UI
                if(dataSnapshot!!.exists()) {
                    for (i in dataSnapshot.children) {
                        if (i.key == user!!.uid) {
                            val userdata = myRef.child(user!!.uid)
                            Log.d("kkkkk",i.key)
                            userdata.addValueEventListener(object: ValueEventListener{

                                override fun onDataChange(dataSnapshot2: DataSnapshot) {
                                    if(dataSnapshot2!!.exists()) {
                                        for (j in dataSnapshot2.children) {
                                            if(j.key == id){
                                                setFav = true
                                                favButton.setBackgroundResource(R.drawable.ic_favorite_black_24dp)
                                                Log.d("kkkkk","Favorite de")


                                                break
                                            }
                                            else{
                                                setFav = false
                                                Log.d("kkkkk","TestF")
                                            }
                                        }
                                    }


                                }
                                override fun onCancelled(databaseError2: DatabaseError) {}
                            }
                            )
                        }
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                //Log.w(TAG, "loadPost:onCancelled", databaseError.toException())
                // ...
            }
        }
        myRef.addValueEventListener(postListener)


        //Set or remove Favorite
        favButton.setOnClickListener {
            Log.d("kkkkk","OUT")
            if (setFav == true){
                setFav = false
                favButton.setBackgroundResource(R.drawable.ic_favorite_border_black_24dp)
                delFavorite(id)
                Log.d("kkkkk","TestF")
            }
            else if (setFav == false){

                setFav = true
                setFavorite(name,address,id,phone,price,rating,lat,long)
                favButton.setBackgroundResource(R.drawable.ic_favorite_black_24dp)
                Log.d("kkkkk","Test")
            }
            Log.d("kkkkk",setFav.toString())
        }


    //Set resources on page
        resName.text = "Name: $name"
        resAdd.text = "Address: $address"
        resPhone.text = "Phone: $phone"

        if(price == -1)
        {
            resPrice.text = "Price: No information"
        }
        else if(price == 1)
        {
            resPrice.text = "Price: Cheap"
        }
        else if(price == 2)
        {
            resPrice.text = "Price: Moderate"
        }
        else if(price == 3)
        {
            resPrice.text = "Price: Expensive"
        }
        else if(price == 4)
        {
            resPrice.text = "Price: Very Expensive"
        }

        resRating.text = "Rating: ${rating.toString()}"



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
        //Set up Map
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
    fun delFavorite(id: String) {
        val info = myRef.child(user!!.uid).child(id).setValue(null)
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }
}
