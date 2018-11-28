package com.egci428.feedme

import android.graphics.Bitmap
import com.google.android.gms.location.places.GeoDataClient
import com.google.android.gms.location.places.PlacePhotoResponse
import com.google.android.gms.maps.model.LatLng


object Dataprovider {

    private val data = ArrayList<Restaurant>()
    fun getData(): ArrayList<Restaurant> {

        return data
    }

    fun addData(name: String, address: String, id: String, phonenumber: String, pricelevel: Int, rating: Float, latlng: LatLng, mGeoDataClient: GeoDataClient ){

        data.add(Restaurant(name,address,id,phonenumber, pricelevel, rating, latlng, mGeoDataClient))
    }

    fun clearData(){
        data.clear()
    }
}