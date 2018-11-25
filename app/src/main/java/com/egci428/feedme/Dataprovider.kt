package com.egci428.feedme

import com.google.android.gms.maps.model.LatLng


object Dataprovider {

    private val data = ArrayList<Restaurant>()
    fun getData(): ArrayList<Restaurant> {
        fun addData(name: String, address: String, id: String, phonenumber: String, pricelevel: Int, rating: Float, latlng: LatLng ){

            data.add(Restaurant(name,address,id,phonenumber, pricelevel, rating, latlng))
        }
        return data
    }

    fun addData(name: String, address: String, id: String, phonenumber: String, pricelevel: Int, rating: Float, latlng: LatLng ){

        data.add(Restaurant(name,address,id,phonenumber, pricelevel, rating, latlng))
    }
}