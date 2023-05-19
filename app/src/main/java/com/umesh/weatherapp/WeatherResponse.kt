package com.umesh.weatherapp

data class WeatherResponse(
    val weather: List<Weather>,
    val main: Main,
    val wind: Wind,
    val sys: Sys,
    val name: String
)

data class Weather(
    val id: Int,
    val main: String,
    val description: String,
    val icon: String,
)

data class Main(
    val temp: Double,
    val pressure: Int,
    val humidity: Int
)

data class Wind(
    val speed: Double,
    val deg: Double,
)

data class Sys(
    val country: String
)
