package com.umesh.weatherapp

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class WeatherViewModelFactory(private val context: Context, private val weatherRepository: WeatherRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(WeatherViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return WeatherViewModel(context, weatherRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}