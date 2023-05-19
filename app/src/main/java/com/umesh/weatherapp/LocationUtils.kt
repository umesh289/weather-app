package com.umesh.weatherapp

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.util.Log
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

suspend fun FusedLocationProviderClient.awaitLastLocation(context: Context): Location? =
    suspendCancellableCoroutine { continuation ->
        val permission = Manifest.permission.ACCESS_FINE_LOCATION

        if (ActivityCompat.checkSelfPermission(
                context,
                permission
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            continuation.resume(null)
            Log.d("Location", "Location permission not granted")
        } else {
            lastLocation
                .addOnSuccessListener { location ->
                    continuation.resume(location)
                }
                .addOnFailureListener { exception ->
                    continuation.resume(null)
                    Log.e("SearchScreen", "Error getting last location: ${exception.message}")
                }
        }
    }
