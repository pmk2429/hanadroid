package com.example.hanadroid.ui

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.hanadroid.databinding.ActivityLocationBinding
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class LocationActivity : AppCompatActivity(), OnMapReadyCallback {

    private var _binding: ActivityLocationBinding? = null
    val binding get() = _binding!!

    private lateinit var googleMap: GoogleMap

    private val requestFineAndCoarseLocationPermissions =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            if (permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true &&
                permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true
            ) {
                showToast("Location permissions granted")
                // Example: Get last known location
                captureLocation()
            } else {
                showToast("Location permissions denied")
            }
        }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityLocationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.mapView.apply {
            onCreate(savedInstanceState)
            getMapAsync(this@LocationActivity)
        }
    }

    override fun onResume() {
        super.onResume()
        binding.mapView.onResume()
    }

    override fun onMapReady(gMap: GoogleMap) {
        googleMap = gMap
        checkLocationPermission()
    }

    @SuppressLint("MissingPermission")
    private fun captureLocation() {
        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        // Permissions are granted -- capture last known location and show it on a Map
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                location?.let {
                    val latitude = it.latitude
                    val longitude = it.longitude
                    showLocationOnMap(latitude, longitude)
                }
            }
            .addOnFailureListener { e: Exception ->
                e.printStackTrace()
            }
    }

    private fun showLocationOnMap(latitude: Double, longitude: Double) {
        // Add a marker on the map
        val location = LatLng(latitude, longitude)
        googleMap.addMarker(MarkerOptions().position(location).title("Last Known Location"))
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15f))
    }

    private fun checkLocationPermission() {
        // Check if the app has location permissions
        val fineLocationGranted =
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED

        val coarseLocationGranted =
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED

        if (fineLocationGranted && coarseLocationGranted) {
            showToast("Location permissions already granted")
            captureLocation()
        } else {
            // Request both fine and coarse location permissions
            requestFineAndCoarseLocationPermissions.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private val requestLocationPermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) showToast("Location permission granted")
            else Toast.makeText(this, "No Location Permission", Toast.LENGTH_LONG)
        }

    private fun checkAndRequestFineLocation() {
        val permission = Manifest.permission.ACCESS_FINE_LOCATION
        when {
            ContextCompat.checkSelfPermission(
                this,
                permission
            ) == PackageManager.PERMISSION_GRANTED -> {
                showToast("Location permission granted")
                captureLocation()
            }

            shouldShowRequestPermissionRationale(permission) -> {
                // permission denied permanently
            }

            else -> {
                requestLocationPermission.launch(permission)
            }
        }
    }

}
