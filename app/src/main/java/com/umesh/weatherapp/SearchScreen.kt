package com.umesh.weatherapp

import android.Manifest
import android.location.Location
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.*
import coil.request.CachePolicy
import coil.request.ImageRequest
import coil.transform.CircleCropTransformation
import java.math.BigDecimal
import java.math.RoundingMode
import androidx.compose.material.*
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import com.google.android.gms.location.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


@Composable
fun WeatherIcon(iconUrl: String) {
    val painter = rememberAsyncImagePainter(
        ImageRequest.Builder(LocalContext.current).data(data = iconUrl)
            .apply(fun ImageRequest.Builder.() {
                transformations(CircleCropTransformation())
                diskCachePolicy(CachePolicy.ENABLED)
            }).build()
    )

    Image(
        painter = painter,
        contentDescription = null,
        contentScale = ContentScale.Fit,
        modifier = Modifier.size(48.dp)
    )

    // Handle image load success or error
    when (val state = painter.state) {
        is AsyncImagePainter.State.Success -> {
            Log.d("WeatherApp", "Image loaded Successful")
        }
        is AsyncImagePainter.State.Error -> {
            Log.e("WeatherApp", "Image load failed")
        }
        is AsyncImagePainter.State.Loading -> {
            // Handle loading state if needed
        }
        else -> {
            // Handle other possible states or provide a default behavior
        }
    }
}

@Composable
fun SearchScreen(weatherViewModel: WeatherViewModel,
                 weatherPreferences: WeatherPreferences) {
    // State to hold the entered city name
    var cityName by remember {
        mutableStateOf(TextFieldValue(weatherPreferences.lastSearchedCity ?: ""))
    }

    val apiKey = "71f892699a2c3911f14af1ce596b38e2"

    // Location permission state
    val locationPermissionGranted = remember { mutableStateOf(false) }

    val fusedLocationClient = LocationServices.getFusedLocationProviderClient(LocalContext.current)
    val locationResultLauncher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
        if (isGranted) {
            locationPermissionGranted.value = true
            Log.d("Location", "Location permission granted")

        } else {
            Log.d("Location", "Location permission not granted")
        }
    }

    Column(Modifier.padding(16.dp)) {
        if (!locationPermissionGranted.value) {
            Button(
                onClick = {
                    locationResultLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                },
                modifier = Modifier.fillMaxWidth().align(CenterHorizontally)
            ) {
                Text("Grant Location Permission")
            }

            Spacer(modifier = Modifier.height(16.dp))

        } else {
            LaunchedEffect(Unit) {
                try {
                    withContext(Dispatchers.IO) {

                        LocationInterop.getLastLocation(fusedLocationClient, weatherViewModel.context, object : LocationInterop.LocationCallback {
                            override fun onLocationReceived(location: Location) {
                                val latitude = location.latitude
                                val longitude = location.longitude
                                weatherViewModel.searchWeatherByLocation(latitude, longitude, apiKey)
                            }

                            override fun onLocationError(exception: Exception) {
                                Log.d("Location", "Could not get location: ${exception.message}")
                            }
                        })
                    }

                } catch (exception: SecurityException) {
                    Log.e("SearchScreen", "SecurityException: ${exception.message}")
                } catch (exception: Exception) {
                    Log.e("SearchScreen", "Error getting last location: ${exception.message}")
                }
            }
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        TextField(
            value = cityName,
            onValueChange = { cityName = it },
            label = { Text("Enter city name") },
            modifier = Modifier.padding(16.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                weatherPreferences.lastSearchedCity = cityName.text
                weatherViewModel.searchWeather(cityName.text, apiKey)
            },
            modifier = Modifier.padding(16.dp)
        ) {
            Text("Search")
        }

        Spacer(modifier = Modifier.height(16.dp))

        val weatherResponse by weatherViewModel.weatherResponse.observeAsState()

        if (weatherResponse != null) {

            val weather = weatherResponse!!.weather?.get(0)
            val main = weatherResponse!!.main
            val wind = weatherResponse!!.wind
            val country = weatherResponse!!.sys.country
            val name = weatherResponse!!.name


            val temperature = kelvinToCelsius(main.temp)
            val temperatureString = "${temperature}\u00B0C"

            val weatherIconUrl = weather?.icon?.let {
                "https://openweathermap.org/img/wn/${weather.icon}@2x.png"
            }

            // Update the city name in Search box if fetched by lat-lon
            if (weatherViewModel.fetchedByLocation) {
                Text(
                    text = "Weather at your location",
                    modifier = Modifier.padding(bottom = 16.dp),
                    fontWeight = FontWeight.Bold,
                    color = Color.Red
                )

                Spacer(modifier = Modifier.height(16.dp))

            }

            Divider(color = Color.LightGray, thickness = 1.dp)

            Spacer(modifier = Modifier.height(16.dp))

            Text(text = "$name, $country")

            Row(
                modifier = Modifier.padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (weatherIconUrl != null) {
                    WeatherIcon(weatherIconUrl)
                }

                if (weather != null) {
                    Text(text = "${weather.main}", modifier = Modifier.padding(start = 8.dp))
                }
            }

            Box(
                modifier = Modifier
                    .size(width = 80.dp, height = 28.dp)
                    .background(Color.DarkGray, RoundedCornerShape(8.dp))
                    .padding(4.dp)
            ) {
                Text(
                    //Kelvin to Celsius
                    text = AnnotatedString(temperatureString),
                    modifier = Modifier.align(Center),
                    color = Color.White,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(text = "Pressure: ${main.pressure}   Humidity: ${main.humidity}")

            Spacer(modifier = Modifier.height(8.dp))

            Text(text = "Wind Speed: ${wind.speed}    Deg: ${wind.deg}")


        }
    }
}

fun kelvinToCelsius(kelvin: Double): Double {
    val celsius = kelvin - 273.15
    return BigDecimal(celsius)
        .setScale(2, RoundingMode.DOWN)
        .toDouble()
}
