package com.egci428.feedme

import com.google.android.gms.location.places.GeoDataClient
import com.google.android.gms.maps.model.LatLng

object DataproviderFav {
    private val data = ArrayList<Favourite>()
    fun getData(): ArrayList<Favourite> {

        return data
    }

    fun addData(name: String, address: String, id: String, phonenumber: String, pricelevel: Int, rating: Float, lat: Double,long: Double,mGeoDataClient: GeoDataClient){

        data.add(Favourite(name,address,id,phonenumber, pricelevel, rating, lat, long,mGeoDataClient))
    }

    fun clearData(){
        data.clear()
    }
}
