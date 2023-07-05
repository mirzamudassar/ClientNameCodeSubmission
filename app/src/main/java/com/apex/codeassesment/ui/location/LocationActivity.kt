package com.apex.codeassesment.ui.location

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.apex.codeassesment.R
import com.apex.codeassesment.databinding.ActivityLocationBinding


// TODO (Optional Bonus 8 points): Calculate distance between 2 coordinates using phone's location
class LocationActivity : AppCompatActivity() {
    private lateinit var locationManager: LocationManager
    private lateinit var binding: ActivityLocationBinding
    private lateinit var latitudeRandomUser: String
    private lateinit var longitudeRandomUser: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLocationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        latitudeRandomUser = intent.getStringExtra("user-latitude-key")!!
        longitudeRandomUser = intent.getStringExtra("user-longitude-key")!!

        binding.locationRandomUser.text =
            getString(R.string.location_random_user, latitudeRandomUser, longitudeRandomUser)
        locationManager = getSystemService(LOCATION_SERVICE) as LocationManager

        binding.locationCalculateButton.setOnClickListener {
            calculateDistance()
            Toast.makeText(
                this,
                "TODO (8): Bonus - Calculate distance between 2 coordinates using phone's location",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
//done
    private fun calculateDistance() {
        val hasLocationPermission = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        if (hasLocationPermission) {
            val locationListener = object : LocationListener {
                override fun onLocationChanged(location: Location) {
                    val userLocation = Location("")
                    userLocation.latitude = latitudeRandomUser.toDouble()
                    userLocation.longitude = longitudeRandomUser.toDouble()

                    val currentLocation = Location("")
                    currentLocation.latitude = location.latitude
                    currentLocation.longitude = location.longitude

                    val distance = userLocation.distanceTo(currentLocation)
                    Toast.makeText(
                        this@LocationActivity,
                        "Distance: $distance meters",
                        Toast.LENGTH_SHORT
                    ).show()

                    locationManager.removeUpdates(this)
                }

                override fun onProviderDisabled(provider: String) {}

                override fun onProviderEnabled(provider: String) {}

                override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {}
            }

            locationManager.requestSingleUpdate(
                LocationManager.GPS_PROVIDER,
                locationListener,
                null
            )
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
        }
    }

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 100
    }
}
