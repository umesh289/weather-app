package com.umesh.weatherapp

import android.util.Log
import retrofit2.Response

class WeatherRepository(private val weatherApiService: WeatherApiService) {
    suspend fun getWeatherByCityName(cityName: String, apiKey: String): WeatherResponse? {
        Log.d("WeatherRepository", "Fetching weather data for $cityName")

        var response : Response<WeatherResponse>? = null

        try {
            response = weatherApiService.getWeatherByCityName(cityName, apiKey)

        } catch (e : Exception) {
            Log.e("WeatherRepository", e.toString())
        }
        if (response != null && response.isSuccessful) {
            Log.d("WeatherRepository", "Received Response $response")

            Log.d("WeatherRepository", "Received Response ${response.body()}")

            val weatherResponse: WeatherResponse? = response.body()

            Log.d("WeatherRepository", "Received Response ${weatherResponse}")
            Log.d("WeatherRepository", "weather ${weatherResponse?.weather}")
            Log.d("WeatherRepository", "main ${weatherResponse?.main}")
            Log.d("WeatherRepository", "wind ${weatherResponse?.wind}")

            return weatherResponse!!
        } else {
            if (response != null) {
                Log.d("WeatherRepository", "Received unsuccessful response")
                Log.d("WeatherRepository", "${response.code()}")
                Log.d("WeatherRepository", "${response.message()}")
            }
        }
        return null
    }

    suspend fun getWeatherByLocation(latitude: Double, longitude: Double, apiKey: String): WeatherResponse? {
        val response = weatherApiService.getWeatherByLocation(latitude, longitude, apiKey)

        if (response.isSuccessful) {
            Log.d("WeatherRepository", "Received Response $response")

            Log.d("WeatherRepository", "Received Response ${response.body()}")

            val weatherResponse: WeatherResponse? = response.body()

            Log.d("WeatherRepository", "Received Response ${weatherResponse}")
            Log.d("WeatherRepository", "weather ${weatherResponse?.weather}")
            Log.d("WeatherRepository", "main ${weatherResponse?.main}")
            Log.d("WeatherRepository", "wind ${weatherResponse?.wind}")

            return weatherResponse!!
        } else {
            Log.d("WeatherRepository", "Received unsuccessful response")
            Log.d("WeatherRepository", "${response.code()}")
            Log.d("WeatherRepository", "${response.message()}")
        }

        return null

    }
}
