package com.umesh.weatherapp

import android.content.Context
import android.util.Log
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class WeatherViewModel(public val context: Context, private val weatherRepository: WeatherRepository) : ViewModel() {
    private val _weatherResponse = MutableLiveData<WeatherResponse>()
    val weatherResponse: LiveData<WeatherResponse> = _weatherResponse
    var fetchedByLocation : Boolean = false

    fun searchWeather(cityName: String, apiKey: String) {
        CoroutineScope(Dispatchers.Main).launch {
            fetchedByLocation = false
            val weather = weatherRepository.getWeatherByCityName(cityName, apiKey)
            _weatherResponse.value = weather
        }
    }

    fun searchWeatherByLocation(latitude: Double, longitude: Double, apiKey: String) {
        viewModelScope.launch {
            try {
                fetchedByLocation = true
                val weather = weatherRepository.getWeatherByLocation(latitude, longitude, apiKey)
                _weatherResponse.value = weather
            } catch (exception: Exception) {
                // Handle the exception or display an error message
                Log.e("WeatherViewModel", "Error getting weather by location: ${exception.message}")
            }
        }
    }
}

