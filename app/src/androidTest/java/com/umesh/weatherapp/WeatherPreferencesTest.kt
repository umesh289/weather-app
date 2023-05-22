package com.umesh.weatherapp

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import androidx.test.core.app.ApplicationProvider
import androidx.test.runner.AndroidJUnit4
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations

@RunWith(AndroidJUnit4::class)
class WeatherPreferencesTest {
    @Mock
    private lateinit var sharedPreferences: SharedPreferences

    @Mock
    private lateinit var mockEditor: SharedPreferences.Editor
    
    private lateinit var weatherPreferences: WeatherPreferences

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        val context = ApplicationProvider.getApplicationContext<Context>()
        weatherPreferences = WeatherPreferences(context)
        `when`(sharedPreferences.edit()).thenReturn(mockEditor)

        weatherPreferences.sharedPreferences = sharedPreferences
    }

    @After
    fun tearDown() {
        // Reset the shared preferences to avoid interfering with other tests
        weatherPreferences.sharedPreferences = null
    }

    @Test
    fun testGetLastSearchedCity() {
        val expectedCity = "London"
        `when`(sharedPreferences.getString(WeatherPreferences.KEY_LAST_SEARCHED_CITY, null))
            .thenReturn(expectedCity)

        val lastSearchedCity = weatherPreferences.lastSearchedCity

        assert(lastSearchedCity == expectedCity)
    }

    @Test
    fun testSetLastSearchedCity() {
        val city = "Paris"

        weatherPreferences.lastSearchedCity = city

        // Verify that the shared preferences editor was called with the correct values
        verify(sharedPreferences.edit()).putString(WeatherPreferences.KEY_LAST_SEARCHED_CITY, city)
        verify(sharedPreferences.edit()).apply()
    }
}
