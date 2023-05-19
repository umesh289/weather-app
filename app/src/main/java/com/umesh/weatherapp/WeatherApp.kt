package com.umesh.weatherapp

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun WeatherApp(weatherViewModel: WeatherViewModel) {
    val navController = rememberNavController()
    lateinit var weatherPreferences: WeatherPreferences
    val context = LocalContext.current

    NavHost(
        navController = navController,
        startDestination = "search"
    ) {

        weatherPreferences = WeatherPreferences(context)

        composable("search") {
            SearchScreen(weatherViewModel,
                weatherPreferences = weatherPreferences
            )
        }
    }
}
