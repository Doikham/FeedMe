package com.egci428.feedme

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.location.LocationManager
import android.os.Bundle
import android.provider.ContactsContract
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.ActionBar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.google.android.gms.location.places.GeoDataClient
import com.google.android.gms.location.places.PlaceDetectionClient
import com.google.android.gms.location.places.Places
import com.google.android.gms.maps.model.LatLng
import kotlinx.android.synthetic.main.activity_list_restaurant.*
import kotlinx.android.synthetic.main.restaurant_item.view.*
import java.util.*

class ListRestaurant : AppCompatActivity(), SensorEventListener {

    protected var data:ArrayList<Restaurant>? = null
    val PLACE_PICKER_REQUEST = 1
    private var sensorManager: SensorManager? = null
    private var lastUpdate: Long = 0
    private var color = false
    var gone: Boolean = false
    var exist: Boolean = false



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_restaurant)

        tbListRes.setNavigationOnClickListener{
            Dataprovider.clearData()
            finish()
        }
        tbListRes.setNavigationIcon(R.drawable.ic_keyboard_arrow_left_black_24dp)

        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager

        lastUpdate = System.currentTimeMillis()

        var choose:Int = intent.getIntExtra("choice",0)


        var mGeoDataClient = Places.getGeoDataClient(this,null) as GeoDataClient


        // Construct a PlaceDetectionClient.
        var mPlaceDetectionClient = Places.getPlaceDetectionClient(this, null) as PlaceDetectionClient




        // restaurant is 79
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted

            //Toast.makeText(this, "Please enable location services in order to continue",Toast.LENGTH_SHORT).show()
            val permissions = arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION)
            ActivityCompat.requestPermissions(this, permissions,0)

            recreate()

        }
        else {
            val restaurantnum = 79
            val cafenum = 15
            val delnum = 60
            val tanum = 61
            val placeResult = mPlaceDetectionClient.getCurrentPlace(null)

            placeResult.addOnCompleteListener { task ->
                val likelyPlaces = task.result
                when (choose) {
                    1 -> for (placeLikelihood in likelyPlaces) {
                        if (placeLikelihood.place.placeTypes.contains(restaurantnum)) {
                            Dataprovider.addData(placeLikelihood.place.name.toString(),placeLikelihood.place.address.toString(),placeLikelihood.place.id,placeLikelihood.place.phoneNumber.toString(),placeLikelihood.place.priceLevel,placeLikelihood.place.rating,placeLikelihood.place.latLng,mGeoDataClient)
                            Log.d("supyo","Geo added successfully")
                        }
                    }
                    2 -> for (placeLikelihood in likelyPlaces) {
                        if (placeLikelihood.place.placeTypes.contains(cafenum)) {
                            Dataprovider.addData(placeLikelihood.place.name.toString(),placeLikelihood.place.address.toString(),placeLikelihood.place.id,placeLikelihood.place.phoneNumber.toString(),placeLikelihood.place.priceLevel,placeLikelihood.place.rating,placeLikelihood.place.latLng,mGeoDataClient)
                        }
                    }
                    3 -> for (placeLikelihood in likelyPlaces) {
                        if (placeLikelihood.place.placeTypes.contains(delnum)) {
                            Dataprovider.addData(placeLikelihood.place.name.toString(),placeLikelihood.place.address.toString(),placeLikelihood.place.id, placeLikelihood.place.phoneNumber.toString(), placeLikelihood.place.priceLevel, placeLikelihood.place.rating, placeLikelihood.place.latLng,mGeoDataClient)
                        }
                    }
                    4 -> for (placeLikelihood in likelyPlaces) {
                        if (placeLikelihood.place.placeTypes.contains(tanum)) {
                            Dataprovider.addData(placeLikelihood.place.name.toString(),placeLikelihood.place.address.toString(),placeLikelihood.place.id, placeLikelihood.place.phoneNumber.toString(), placeLikelihood.place.priceLevel, placeLikelihood.place.rating, placeLikelihood.place.latLng,mGeoDataClient)
                        }
                    }
                }
                likelyPlaces.release()


                        //here
                //put list here; data = list.getData()

                //here
                //put list here; data = list.getData()
                data = Dataprovider.getData()
                val restaurantArrayAdapter = RestaurantArrayAdapter(this,0, data!!)
                listr.adapter = restaurantArrayAdapter

                listr.setOnItemClickListener { adapterView, view, position, _ ->
                    val restaurant = data!!.get(position)
                    Log.d("supyo","$position")
                    displayDetail(restaurant)

                }

                if(restaurantArrayAdapter.count != 0) {
                    exist = true

                }else{
                    val intent = Intent(this,EmptyList::class.java)
                    startActivity(intent)
                    finish()
                }

            }

        }

    }

    private fun displayDetail(restaurant: Restaurant){
        val intent = Intent(this,RestaurantActivity::class.java)

        intent.putExtra("rname",restaurant.name)
        Log.d("Extra",restaurant.name)
        intent.putExtra("raddress",restaurant.address)
        Log.d("Extra",restaurant.address)
        intent.putExtra("rid",restaurant.id)
        Log.d("Extra",restaurant.id)
        intent.putExtra("rphonenumber",restaurant.phonenumber)
        Log.d("Extra",restaurant.phonenumber)
        intent.putExtra("rpricelevel",restaurant.pricelevel)
        Log.d("Extra",restaurant.pricelevel.toString())
        intent.putExtra("rrating",restaurant.rating)
        Log.d("Extra",restaurant.rating.toString())
        Log.d("lattt",restaurant.latlng.toString())
        intent.putExtra("rlat",restaurant.latlng.latitude)
        Log.d("lattt",restaurant.latlng.latitude.toString())
        intent.putExtra("rlong",restaurant.latlng.longitude)
        Log.d("lattt",restaurant.latlng.longitude.toString())


        startActivity(intent)


    }

    class RestaurantArrayAdapter(var context: Context, var resource: Int, var objects: ArrayList<Restaurant>) : BaseAdapter() {

        override fun getCount(): Int {
            return objects.size
        }

        override fun getItem(position: Int): Any {
            return objects[position]
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            val restaurant = objects[position]
            val view: View

            if (convertView == null) {
                val layoutInflater = LayoutInflater.from(parent!!.context)
                view = layoutInflater.inflate(R.layout.restaurant_item, parent, false)
                val viewHolder = ViewHolder(view.ResListName, view.resListImg)
                view.tag = viewHolder
            } else {
                view = convertView
            }
            val viewHolder = view.tag as ViewHolder
            viewHolder.restaurantName.text = restaurant.name

            val photoMetadataResponse = restaurant.mGeoDataClient.getPlacePhotos(restaurant.id)
            photoMetadataResponse.addOnCompleteListener { task ->
                // Get the list of photos.
                val photos = task.result
                // Get the PlacePhotoMetadataBuffer (metadata for all of the photos).
                val photoMetadataBuffer = photos.photoMetadata
                if(photoMetadataBuffer.count() != 0) {
                // Get the first photo in the list.
                val photoMetadata = photoMetadataBuffer.get(0)
                // Get the attribution text.
                val attribution = photoMetadata.attributions
                // Get a full-size bitmap for the photo.

                    val photoResponse = restaurant.mGeoDataClient.getPhoto(photoMetadata)
                    photoResponse.addOnCompleteListener { pic ->
                        val photo = pic.result
                        val bitmap = photo.bitmap
                        viewHolder.restaurantImg.setImageBitmap(bitmap)
                    }
                }
                photoMetadataBuffer.release()

            }

            return view
        }
        private class ViewHolder(val restaurantName: TextView, val restaurantImg: ImageView)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        Dataprovider.clearData()

    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event!!.sensor.type == Sensor.TYPE_ACCELEROMETER){
            getAccelerometer(event)
        }
    }
    private fun getAccelerometer(event: SensorEvent){
        val values = event.values

        val x = values[0]
        val y = values[1]
        val z = values[2]

        val accel = (x*x + y*y + z*z)/(SensorManager.GRAVITY_EARTH*SensorManager.GRAVITY_EARTH)

        val actualTime = System.currentTimeMillis()
        if(accel >= 10){
            if((actualTime - lastUpdate)< 200){
                return
            }


            if(gone == false) {

                Log.d("kkkkk","Inside")

                if(exist == true){
                    val size = RestaurantArrayAdapter(this, 0, data!!).count
                    val nextInt = Random().nextInt(size)
                    val restaurant = data!!.get(nextInt)
                    displayDetail(restaurant)
                    Log.d("supyo", "Activity Launched $count")
                    //Toast.makeText(this, "Shake de + $size + $nextInt", Toast.LENGTH_SHORT).show()
                    gone = true
                }

            }


        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {

    }


    override fun onResume() {
        super.onResume()
        sensorManager!!.registerListener(this,sensorManager!!.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL)
        gone = false

    }

    override fun onPause() {
        super.onPause()
        sensorManager!!.unregisterListener(this)
    }

}

