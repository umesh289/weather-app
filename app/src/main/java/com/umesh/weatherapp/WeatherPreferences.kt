package com.umesh.weatherapp

import android.content.Context
import androidx.core.content.edit
import androidx.preference.PreferenceManager

class WeatherPreferences(context: Context) {
    private val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)

    var lastSearchedCity: String?
        get() = sharedPreferences.getString(KEY_LAST_SEARCHED_CITY, null)
        set(value) {
            sharedPreferences.edit {
                putString(KEY_LAST_SEARCHED_CITY, value)
            }
        }

    companion object {
        private const val KEY_LAST_SEARCHED_CITY = "last_searched_city"
    }
}
