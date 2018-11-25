package com.egci428.feedme

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.ContactsContract
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.google.android.gms.location.places.GeoDataClient
import com.google.android.gms.location.places.PlaceDetectionClient
import com.google.android.gms.location.places.Places
import kotlinx.android.synthetic.main.activity_list_restaurant.*
import kotlinx.android.synthetic.main.restaurant_item.view.*

class ListRestaurant : AppCompatActivity()  {

    protected var data:ArrayList<Restaurant>? = null
    val PLACE_PICKER_REQUEST = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_restaurant)

        var choice: Int = 0

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
                if(choice == 1){
                for (placeLikelihood in likelyPlaces) {
                    if (placeLikelihood.place.placeTypes.contains(restaurantnum)) {
                        Dataprovider.addData(placeLikelihood.place.name.toString(),placeLikelihood.place.address.toString(),placeLikelihood.place.id,placeLikelihood.place.phoneNumber.toString(),placeLikelihood.place.priceLevel,placeLikelihood.place.rating,placeLikelihood.place.latLng)
                    }
                }


                }
                likelyPlaces.release()
            }





        }


        //put list here; data = list.getData()
        data = Dataprovider.getData()
        val restaurantArrayAdapter = RestaurantArrayAdapter(this,0, data!!)
        listr.adapter = restaurantArrayAdapter
        listr.setOnItemClickListener { adapterView, view, position, ->
            val restaurant = data!!.get(position)
            displayDetail(restaurant,position)
        }
    }

    private fun displayDetail(restaurant: Restaurant){
        val intent = Intent(this,RestaurantActivity::class.java)
        intent.putExtra("rname",restaurant.name)
        intent.putExtra("rdes",restaurant.description)
    }

    private class RestaurantArrayAdapter(var context: Context, var resource: Int, var objects: ArrayList<Restaurant>) : BaseAdapter(){

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

            if (convertView==null){
                val layoutInflater = LayoutInflater.from(parent!!.context)
                view = layoutInflater.inflate(R.layout.restaurant_item, parent, false)
                val viewHolder = ViewHolder(view.resListDes, view.ResListName, view.resListImg)
                view.tag = viewHolder
            } else{
                view = convertView
            }
            val viewHolder = view.tag as ViewHolder
            viewHolder.restaurantName.text = restaurant.name
            viewHolder.restaurantDes.text = restaurant.description
            //val res = context.resources.getIdentifier()
            //Upper line sets res as a place where we use image
            //viewHolder.restaurantImg.setImageResource(res)
            //Upper line sets viewHolder to store image
        }
        private class ViewHolder(val restaurantName: TextView, val restaurantDes: TextView, val restaurantImg: ImageView)
    }
}