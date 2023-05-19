package com.umesh.weatherapp

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {
    private lateinit var weatherViewModel: WeatherViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.openweathermap.org/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val weatherApiService : WeatherApiService = retrofit.create(WeatherApiService::class.java)

        val weatherRepository = WeatherRepository(weatherApiService)
        val viewModelFactory = WeatherViewModelFactory(this, weatherRepository)
        weatherViewModel = ViewModelProvider(this, viewModelFactory).get(WeatherViewModel::class.java)

        setContent {
            WeatherApp(weatherViewModel)
        }
    }
}
