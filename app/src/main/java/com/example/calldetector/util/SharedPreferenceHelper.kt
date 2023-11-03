package com.example.calldetector.util

import android.content.Context
import android.content.SharedPreferences

class SharedPreferenceHelper(private val context:Context)
{
    private val sharedPreferences:SharedPreferences=
        context.getSharedPreferences(MY_PREFERENCE, Context.MODE_PRIVATE)

    fun isEnabled():Boolean
    {
        return sharedPreferences.getBoolean(TOGGLE_ENABLED,false)
    }

    fun makeEnable(boolean:Boolean)
    {
        val editor=sharedPreferences.edit()
        editor.putBoolean(TOGGLE_ENABLED,boolean)
        editor.apply()
    }

    fun getStatus():String?
    {
        return sharedPreferences.getString(MY_STATUS,"Someone is calling")
    }

    fun setStatus(status:String)
    {
        val editor=sharedPreferences.edit()
        editor.putString(MY_STATUS,status)
        editor.apply()
    }

}