package com.egci428.feedme

import android.content.Context

class SharedPreference(context: Context) {
    val PREFERENCE_NAME = "SharedPreference"
    val PREFERENCE_PASSWORD_ENABLED = "isPasswordEnabled"
    val PREFERENCE_PIN_VALUE = "PinValue"
    val password: String? = null

    val preference = context.getSharedPreferences(PREFERENCE_NAME,Context.MODE_PRIVATE)

    fun getPasswordEnabled() : Boolean{
        return preference.getBoolean(PREFERENCE_PASSWORD_ENABLED,false)

    }

    fun setPasswordEnabled(boolean: Boolean){
        val editor = preference.edit()
        editor.putBoolean(PREFERENCE_PASSWORD_ENABLED,boolean)
        editor.apply()

    }

    fun getPinValue() : String{
        return preference.getString(PREFERENCE_PIN_VALUE,"1234")

    }

    fun setPinValue(string: String){
        val editor = preference.edit()
        editor.putString(PREFERENCE_PIN_VALUE,string)
        editor.apply()
    }
}