package com.azure.minitwitter.common

import android.content.Context
import android.content.SharedPreferences

class SharedPreferencesManager{

    companion object{
        private const val APP_SETTINGS_FILE = "APP_SETTINGS"

        private val sharedPreferences: SharedPreferences
            get() = MyApp.context!!.getSharedPreferences(
                    APP_SETTINGS_FILE, Context.MODE_PRIVATE
            )

        fun setSomeStringValue(dataLabel: String, dataValue: String){
            val editor: SharedPreferences.Editor = sharedPreferences.edit()
            editor.putString(dataLabel,dataValue)
            editor.commit()
        }

        fun setSomeBooleanValue(dataLabel: String, dataValue: Boolean){
            val editor: SharedPreferences.Editor = sharedPreferences.edit()
            editor.putBoolean(dataLabel,dataValue)
            editor.commit()
        }

        fun getSomeStringValue(dataLabel: String): String =
                sharedPreferences.getString(dataLabel, null)

        fun getSomeBooleanValue(dataLabel: String): Boolean =
                sharedPreferences.getBoolean(dataLabel, false)
    }
}