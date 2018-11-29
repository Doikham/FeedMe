package com.egci428.feedme

import com.google.android.gms.location.places.GeoDataClient

class Favourite (val name: String,val address: String, val id: String, val phonenumber: String, val pricelevel: Int, val rating: Float, val lat: Double, val long: Double, val mGeoDataClient: GeoDataClient) {
        override fun toString(): String {
                return super.toString()
        }
}