package com.egci428.feedme

import android.graphics.Bitmap
import android.graphics.Picture
import android.net.Uri
import com.google.android.gms.location.places.GeoDataClient
import com.google.android.gms.location.places.PlacePhotoResponse
import com.google.android.gms.maps.model.LatLng

class Restaurant(val name: String,val address: String, val id: String, val phonenumber: String, val pricelevel: Int, val rating: Float, val latlng: LatLng, val mGeoDataClient: GeoDataClient) {
    
    override fun toString(): String {
        return super.toString()
    }



}