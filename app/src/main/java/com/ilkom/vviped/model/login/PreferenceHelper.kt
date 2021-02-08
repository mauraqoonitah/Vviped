package com.ilkom.vviped.model.login

import android.content.Context
import android.content.SharedPreferences

class PreferenceHelper (context: Context) {

    private val PREFS_NAME = "loginPref"
    private var sharedPref: SharedPreferences
    var editor: SharedPreferences.Editor

    init{
        sharedPref = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        editor = sharedPref.edit()
    }

    fun put(key: String, value: String){
        editor.putString(key, value)
            .apply()
    }
    fun put(key: String, value: Int){
        editor.putInt(key, value)
            .apply()
    }
    fun getString(key: String) : String? {
        return sharedPref.getString(key.toString(), null)
    }
    fun getInt(key: String) : Int? {
        return sharedPref.getInt(key, -1)
    }
    fun put(key: String, value: Boolean){
        editor.putBoolean(key, value)
            .apply()
    }
    fun getBoolean(key: String) : Boolean? {
        return sharedPref.getBoolean(key, false)
    }
    fun clear(){
        val editor: SharedPreferences.Editor = sharedPref.edit()
        editor.clear()
        editor.apply()

    }
}