# weather-app

This app retrieves weather for the city provided by openweathermap.org. Also if user gives location access gets weather for lat-lon coordinates by default. 
Saves the city in shared preferences and loads last entered city.

Features and Technologies used:

- Shows current weather for the provided city or retieves based on GPS coordinates if users provided locatio access
- Kotlin and Java
- MVVM
- UI in Jetpack Compose along with Navigation View
- Retrofit2 for Network Calls
- Coroutines
- LiveData and ViewModel
- Image fetching using Coil with Caching enabled
- Load Last city searched using Shared Preferences
- Unit Tests added for one class
- On start app asks the User for location access, If the user gives permission to access the location, 
  then retrieve weather data by default
