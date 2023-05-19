package com.umesh.weatherapp

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApiService {
    @GET("data/2.5/weather")
    suspend fun getWeatherByCityName(
        @Query("q") cityName: String,
        @Query("appid") apiKey: String
    ): Response<WeatherResponse>

    @GET("data/2.5/weather")
    suspend fun getWeatherByLocation(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double,
        @Query("appid") apiKey: String
    ): Response<WeatherResponse>
}