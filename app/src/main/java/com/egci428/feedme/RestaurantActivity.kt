package com.egci428.feedme

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
//import android.support.test.orchestrator.junit.BundleJUnitUtils.getResult
import com.google.android.gms.location.places.PlacePhotoResponse
import com.google.android.gms.tasks.Task
import android.support.annotation.NonNull
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.location.places.ui.PlacePicker.getAttributions
import com.google.android.gms.location.places.PlacePhotoMetadata
import com.google.android.gms.location.places.PlacePhotoMetadataBuffer
import com.google.android.gms.location.places.PlacePhotoMetadataResponse





class RestaurantActivity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_restaurant)
        //supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        var mGeoDataClient2 = Places.getGeoDataClient(this,null) as GeoDataClient

        // Construct a PlaceDetectionClient.
        var mPlaceDetectionClient = Places.getPlaceDetectionClient(this, null) as PlaceDetectionClient


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

        val latlng = intent.getFloatArrayExtra("rlatlng")
        //resLatlng.text = latlng

        val id = intent.getStringExtra("rid")

        /*val img = intent.getIntExtra("rimg", 0)
        resImg.setImageResource(img)*/
        getPhotos(id, mGeoDataClient2)
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


    private fun getPhotos(placeId: String, mGeoDataClient2: GeoDataClient) {
        val photoMetadataResponse = mGeoDataClient2.getPlacePhotos(placeId)
        photoMetadataResponse.addOnCompleteListener { task ->
            // Get the list of photos.
            val photos = task.result
            // Get the PlacePhotoMetadataBuffer (metadata for all of the photos).
            val photoMetadataBuffer = photos.photoMetadata
            // Get the first photo in the list.
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
    }
}
