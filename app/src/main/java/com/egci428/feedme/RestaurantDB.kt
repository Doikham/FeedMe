package com.egci428.feedme

import com.google.android.gms.location.places.GeoDataClient


class RestaurantDB(val name: String,val address: String, val id: String, val phonenumber: String, val pricelevel: Int, val rating: Float, val lat: Double, val long: Double) {
    constructor(): this("","","","",0,0.0F,0.0,0.0)
}