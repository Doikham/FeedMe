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
import android.os.Handler
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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_fav.*
import kotlinx.android.synthetic.main.activity_list_restaurant.*
import kotlinx.android.synthetic.main.activity_restaurant.*
import kotlinx.android.synthetic.main.fav_item.view.*
import kotlinx.android.synthetic.main.restaurant_item.view.*
import java.util.*

class FavActivity : AppCompatActivity(),  SensorEventListener {

    protected var data: ArrayList<Favourite>? = null
    val PLACE_PICKER_REQUEST = 1
    private var sensorManager: SensorManager? = null
    private var lastUpdate: Long = 0
    var gone: Boolean = false
    val user = FirebaseAuth.getInstance().currentUser
    var database = FirebaseDatabase.getInstance()
    var myRef = database.getReference()




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_restaurant)


        tbListRes.setNavigationOnClickListener{
            DataproviderFav.clearData()
            finish()

        }
        tbListRes.setNavigationIcon(R.drawable.ic_keyboard_arrow_left_black_24dp)

        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager

        lastUpdate = System.currentTimeMillis()

        var mGeoDataClient = Places.getGeoDataClient(this,null) as GeoDataClient

            val postListener = object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    // Get Post object and use the values to update the UI
                    if (dataSnapshot!!.exists()) {
                        for (i in dataSnapshot.children) {
                            if (i.key == user!!.uid) {
                                val userdata = myRef.child(user!!.uid)
                                Log.d("kkkkk", i.key)
                                userdata.addValueEventListener(object : ValueEventListener {

                                    override fun onDataChange(dataSnapshot2: DataSnapshot) {
                                        if (dataSnapshot2!!.exists()) {
                                            DataproviderFav.clearData()
                                            for (j in dataSnapshot2.children) {
                                                val datadb = j.getValue(RestaurantDB::class.java)
                                                DataproviderFav.addData(datadb!!.name, datadb!!.address, datadb!!.id, datadb!!.phonenumber, datadb!!.pricelevel, datadb!!.rating, datadb!!.lat, datadb!!.long, mGeoDataClient)
                                                Log.d("favorite", datadb.name)
                                                Log.d("favorite", DataproviderFav.getData().size.toString())


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




        data = DataproviderFav.getData()
        val restaurantArrayAdapter = RestaurantArrayAdapter(this,0, data!!)
        listr.adapter = restaurantArrayAdapter

        listr.setOnItemClickListener { adapterView, view, position, _ ->
            val restaurant = data!!.get(position)
            Log.d("supyo","$position")
            displayDetail(restaurant)

        }

    }



    private fun displayDetail(restaurant: Favourite) {
        val intent = Intent(this, RestaurantActivity::class.java)

        intent.putExtra("rname", restaurant.name)
        Log.d("Extra", restaurant.name)
        intent.putExtra("raddress", restaurant.address)
        Log.d("Extra", restaurant.address)
        intent.putExtra("rid", restaurant.id)
        Log.d("Extra", restaurant.id)
        intent.putExtra("rphonenumber", restaurant.phonenumber)
        Log.d("Extra", restaurant.phonenumber)
        intent.putExtra("rpricelevel", restaurant.pricelevel)
        Log.d("Extra", restaurant.pricelevel.toString())
        intent.putExtra("rrating", restaurant.rating)
        Log.d("Extra", restaurant.rating.toString())
        intent.putExtra("rlat", restaurant.lat)
        intent.putExtra("rlong", restaurant.long)

        DataproviderFav.clearData()

        startActivity(intent)


    }

    class RestaurantArrayAdapter(var context: Context, var resource: Int, var objects: ArrayList<Favourite>) : BaseAdapter() {

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
            Log.d("favorite",restaurant.name)

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


