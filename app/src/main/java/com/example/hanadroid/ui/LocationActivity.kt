package com.example.hanadroid.ui

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.hanadroid.R
import com.example.hanadroid.util.createNotificationChannel
import com.example.hanadroid.util.hideKeyboard
import com.google.android.gms.location.LocationServices

class LocationActivity : AppCompatActivity() {

    private val requestLocationPermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) createNotificationChannel(this)
            else Toast.makeText(this, "No Location Permission", Toast.LENGTH_LONG)
        }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_location)

        captureLocation()
    }

    private fun captureLocation() {
        // Create a FusedLocationProviderClient
        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        // Request the last known location
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                PERMISSIONS_REQUEST_CODE
            )
        } else {
            fusedLocationClient.lastLocation
                .addOnSuccessListener { location: Location? ->
                    location?.let {
                        // Handle the location data
                        val latitude = it.latitude
                        val longitude = it.longitude
                        // Display or use the latitude and longitude
                        showLocationOnMap(latitude, longitude)
                    }
                }
                .addOnFailureListener { e: Exception ->
                    // Handle any errors that may occur
                    e.printStackTrace()
                }
        }
    }

    private fun showLocationOnMap(latitude: Double, longitude: Double) {

    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    fun requestLocationPermission() {
        val permission = Manifest.permission.ACCESS_FINE_LOCATION
        when {
            ContextCompat.checkSelfPermission(
                this,
                permission
            ) == PackageManager.PERMISSION_GRANTED -> {
                // make your action here
            }

            shouldShowRequestPermissionRationale(permission) -> {
                // permission denied permanently
            }

            else -> {
                requestLocationPermission.launch(permission)
            }
        }
    }

    companion object {
        const val PERMISSIONS_REQUEST_CODE = 2
    }

}