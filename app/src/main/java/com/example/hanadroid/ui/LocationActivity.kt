package com.example.hanadroid.ui

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.hanadroid.databinding.ActivityLocationBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.location.LocationRequest as LocationRequest1

class LocationActivity : AppCompatActivity(), OnMapReadyCallback {

    private var _binding: ActivityLocationBinding? = null
    val binding get() = _binding!!

    private lateinit var googleMap: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient

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

//        binding.mapView.apply {
//            onCreate(savedInstanceState)
//            getMapAsync(this@LocationActivity)
//        }
        // Initialize the FusedLocationProviderClient
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        binding.btnSearch.setOnClickListener { onSearchClicked() }
    }

    override fun onResume() {
        super.onResume()
        // binding.mapView.onResume()
    }

    override fun onMapReady(gMap: GoogleMap) {
        // googleMap = gMap
        // checkLocationPermission()
    }

    override fun onPause() {
        super.onPause()
        // binding.mapView.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        // binding.mapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        // binding.mapView.onLowMemory()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_LOCATION_PERMISSION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, get the location
                onSearchClicked()
            } else {
                Toast.makeText(this, "Location permission denied", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun onSearchClicked() {
        // Check for permissions before requesting location updates
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED ||
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
                REQUEST_LOCATION_PERMISSION
            )
            return
        }
        captureLocation()
    }

    @SuppressLint("MissingPermission")
    private fun captureLocation() {
        // Permissions are granted -- capture last known location and show it on a Map
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location ->
                if (location != null) {
                    // Use the current location
                    binding.latLng.text =
                        "Lat ${location.latitude}, Long ${location.longitude}"
                } else {
                    // Request a new location update
                    fusedLocationClient.requestLocationUpdates(
                        LocationRequest1.create(),
                        object : LocationCallback() {
                            override fun onLocationResult(locationResult: LocationResult) {
                                for (newLocation in locationResult.locations) {
                                    binding.latLng.text =
                                        "Lat ${newLocation.latitude}, Long ${newLocation.longitude}"
                                    // Stop location updates
                                    fusedLocationClient.removeLocationUpdates(this)
                                }
                            }
                        },
                        null
                    )
                }
            }
            .addOnFailureListener { e ->
                showToast("Failed to get location: ${e.message}")
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
            // capture location if permission is already granted
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

    companion object {
        private const val REQUEST_LOCATION_PERMISSION = 1
    }

}
