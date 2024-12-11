package com.example.locationprovider

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.widget.Button
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat

class MainActivity : AppCompatActivity() {
    private var locationManager: LocationManager? = null
    private var locationTextView: TextView? = null
    private var selectedProvider = LocationManager.GPS_PROVIDER

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        locationTextView = findViewById(R.id.locationTextView)
        val providerGroup = findViewById<RadioGroup>(R.id.providerGroup)

        locationManager = getSystemService(LOCATION_SERVICE) as LocationManager

        // Handle radio button selection
        providerGroup.setOnCheckedChangeListener { group: RadioGroup?, checkedId: Int ->
            if (checkedId == R.id.radioGps) {
                selectedProvider = LocationManager.GPS_PROVIDER
            } else if (checkedId == R.id.radioNetwork) {
                selectedProvider = LocationManager.NETWORK_PROVIDER
            }
            requestLocationUpdates()
        }

        // Request location updates initially
        requestLocationUpdates()

        var refreshButton = findViewById<Button>(R.id.refreshButton)

        refreshButton.setOnClickListener {
            Toast.makeText(this, "Refreshing Location...", Toast.LENGTH_SHORT).show()
            requestLocationUpdates()
        }

    }

    private fun requestLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // Request permissions
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                LOCATION_PERMISSION_REQUEST_CODE
            )
            return
        }

        locationManager!!.requestLocationUpdates(
            selectedProvider,
            1000,
            1f,
            object : LocationListener {
                override fun onLocationChanged(location: Location) {
                    locationTextView!!.text =
                        "Lat: " + location.latitude + ", Lng: " + location.longitude
                }

                override fun onProviderDisabled(provider: String) {
                    Toast.makeText(this@MainActivity, "$provider is disabled.", Toast.LENGTH_SHORT)
                        .show()
                }

                override fun onProviderEnabled(provider: String) {
                    Toast.makeText(this@MainActivity, "$provider is enabled.", Toast.LENGTH_SHORT)
                        .show()
                }
            })
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                requestLocationUpdates()
            } else {
                Toast.makeText(this, "Location permission denied.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 100
    }
}