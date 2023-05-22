package com.umesh.weatherapp;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;

import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.tasks.Task;

public class LocationInterop {

    public interface LocationCallback {
        void onLocationReceived(Location location);
        void onLocationError(Exception exception);
    }

    public static void getLastLocation(FusedLocationProviderClient fusedLocationClient, Context context, LocationCallback callback) {
        if (fusedLocationClient == null) {
            // Handle the case where fusedLocationClient is null
            callback.onLocationReceived(null);
            return;
        }

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Handle the case where permission is not granted
            callback.onLocationReceived(null);
            return;
        }

        Task<Location> locationTask = fusedLocationClient.getLastLocation();
        if (locationTask == null) {
            return;
        }

        locationTask
                .addOnSuccessListener(location -> {
                    if (location != null) {
                        callback.onLocationReceived(location);
                    } else {
                        callback.onLocationError(new Exception("Could not get location"));
                    }
                })
                .addOnFailureListener(e -> callback.onLocationError(e));
    }
}
