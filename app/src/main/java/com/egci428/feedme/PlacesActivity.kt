package com.egci428.feedme


import android.app.Activity
import android.os.Bundle
import android.support.v4.app.FragmentActivity
import com.google.android.gms.location.places.ui.PlacePicker
import android.widget.Toast
import android.content.Intent
import android.content.pm.PackageManager
import com.google.android.gms.location.places.*
import com.google.android.gms.location.places.PlaceLikelihood
import com.google.android.gms.location.places.PlaceLikelihoodBufferResponse
import com.google.android.gms.tasks.Task
import android.support.annotation.NonNull
import android.support.v4.content.ContextCompat
import android.util.Log
import com.google.android.gms.tasks.OnCompleteListener
import java.util.jar.Manifest


class PlacesActivity : FragmentActivity() {

    val PLACE_PICKER_REQUEST = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_places)

        // Construct a GeoDataClient.
       var mGeoDataClient = Places.getGeoDataClient(this,null) as GeoDataClient

        // Construct a PlaceDetectionClient.
        var mPlaceDetectionClient = Places.getPlaceDetectionClient(this, null) as PlaceDetectionClient

        // TODO: Start using the Places API.

        // restaurant is 79
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
        }
        else {
            val restaurantnum = 79
            val cafenum = 15
            val delnum = 60
            val tanum = 61
            val placeResult = mPlaceDetectionClient.getCurrentPlace(null)
            placeResult.addOnCompleteListener { task ->
                val likelyPlaces = task.result
                for (placeLikelihood in likelyPlaces) {
                    if(placeLikelihood.place.placeTypes.contains(restaurantnum)){
                    Log.i("YO1", String.format("Place '%s' is type '%s' has likelihood: %g",
                            placeLikelihood.place.name,
                            "Restaurant",
                            placeLikelihood.likelihood))
                    }

                    if(placeLikelihood.place.placeTypes.contains(cafenum)){
                        Log.i("YO1", String.format("Place '%s' is type '%s' has likelihood: %g",
                                placeLikelihood.place.name,
                                "Cafe",
                                placeLikelihood.likelihood))
                    }

                    if(placeLikelihood.place.placeTypes.contains(delnum)){
                        Log.i("YO1", String.format("Place '%s' is type '%s' has likelihood: %g",
                                placeLikelihood.place.name,
                                "Delivery",
                                placeLikelihood.likelihood))
                    }

                    if(placeLikelihood.place.placeTypes.contains(tanum)){
                        Log.i("YO1", String.format("Place '%s' is type '%s' has likelihood: %g",
                                placeLikelihood.place.name,
                                "Takeaway",
                                placeLikelihood.likelihood))
                    }
                }
                likelyPlaces.release()
            }





        }


//        val builder = PlacePicker.IntentBuilder()
//
//        startActivityForResult(builder.build(this), PLACE_PICKER_REQUEST)




    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {
                val place = PlacePicker.getPlace(data!!, this)
                val toastMsg = String.format("Place: %s", place.name)
                Toast.makeText(this, toastMsg, Toast.LENGTH_LONG).show()
            }
        }
    }
}