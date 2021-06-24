package com.pascal.weatherapp.ui.location

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.*
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.pascal.weatherapp.R
import com.pascal.weatherapp.data.model.City
import com.pascal.weatherapp.databinding.LocationActivityBinding
import java.io.IOException

class LocationActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: LocationActivityBinding

    @Volatile
    lateinit var latestCity: City

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = LocationActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        initView()
    }

    private fun initView() {
        initFab()
        initSearchByAddress()
    }

    private fun initFab() {
        binding.myLocationFab.setOnClickListener {
            checkPermission()
        }
    }

    private fun initSearchByAddress() {
        binding.buttonSearch.setOnClickListener {
            val geoCoder = Geocoder(it.context)
            val searchText = binding.edittextSearch.text.toString()
            Thread {
                try {
                    val addresses = geoCoder.getFromLocationName(searchText, 1)
                    if (addresses.size > 0) {
                        goToAddress(addresses, it)
                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }.start()
        }
    }

    private fun goToAddress(
        addresses: MutableList<Address>,
        view: View,
    ) {
        val latLng = LatLng(
            addresses[0].latitude,
            addresses[0].longitude
        )
        view.post {
            mMap.clear()
            mMap.addMarker(MarkerOptions().position(latLng))
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12f))
            val location = Location(LocationManager.GPS_PROVIDER).apply {
                latitude = latLng.latitude
                longitude = latLng.longitude
            }
            getAddressAsync(this@LocationActivity, location)
        }
    }

    @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS", "SameParameterValue")
    private fun setMarker(
        location: LatLng,
        searchText: String,
        resourceId: Int
    ): Marker {
        return mMap.addMarker(
            MarkerOptions()
                .position(location)
                .title(searchText)
                .icon(BitmapDescriptorFactory.fromResource(resourceId))
        )
    }

    private fun checkPermission() {
        this.let {
            when {
                ContextCompat.checkSelfPermission(it, Manifest.permission.ACCESS_FINE_LOCATION) ==
                        PackageManager.PERMISSION_GRANTED -> {
                    getLocation()
                }
                shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION) -> {
                    showRationaleDialog()
                }
                else -> {
                    requestPermission()
                }
            }
        }
    }

    private fun showRationaleDialog() {
        this.let {
            AlertDialog.Builder(it)
                .setTitle(getString(R.string.dialog_rationale_title))
                .setMessage(getString(R.string.dialog_rationale_message))
                .setPositiveButton(getString(R.string.dialog_rationale_give_access)) { _, _ ->
                    requestPermission()
                }
                .setNegativeButton(getString(R.string.dialog_rationale_decline)) { dialog, _ -> dialog.dismiss() }
                .create()
                .show()
        }
    }

    private fun requestPermission() {
        requestPermissions(
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
            LOCATION_REQUEST_CODE
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>, grantResults: IntArray
    ) {
        checkPermissionsResult(requestCode, grantResults)
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    private fun checkPermissionsResult(requestCode: Int, grantResults: IntArray) {
        when (requestCode) {
            LOCATION_REQUEST_CODE -> {
                var grantedPermissions = 0
                if ((grantResults.isNotEmpty())) {
                    for (i in grantResults) {
                        if (i == PackageManager.PERMISSION_GRANTED) {
                            grantedPermissions++
                        }
                    }
                    if (grantResults.size == grantedPermissions) {
                        getLocation()
                    } else {
                        showDialog(
                            getString(R.string.dialog_title_no_gps),
                            getString(R.string.dialog_message_no_gps)
                        )
                    }
                } else {
                    showDialog(
                        getString(R.string.dialog_title_no_gps),
                        getString(R.string.dialog_message_no_gps)
                    )
                }
                return
            }
        }
    }

    private fun showDialog(title: String, message: String) {
        this.let {
            AlertDialog.Builder(it)
                .setTitle(title)
                .setMessage(message)
                .setNegativeButton(getString(R.string.dialog_button_close)) { dialog, _ -> dialog.dismiss() }
                .create()
                .show()
        }
    }

    private fun getLocation() {
        this.let { context ->
            if (ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                val locationManager =
                    context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

                if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    val provider = locationManager.getProvider(LocationManager.GPS_PROVIDER)
                    provider?.let {
                        locationManager.requestLocationUpdates(
                            LocationManager.GPS_PROVIDER,
                            REFRESH_PERIOD,
                            MINIMAL_DISTANCE,
                            onLocationListener
                        )
                    }
                } else {
                    val location =
                        locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                    if (location == null) {
                        showDialog(
                            getString(R.string.dialog_title_gps_turned_off),
                            getString(R.string.dialog_message_last_location_unknown)
                        )
                    } else {
                        getAddressAsync(context, location)
                        showDialog(
                            getString(R.string.dialog_title_gps_turned_off),
                            getString(R.string.dialog_message_last_known_location)
                        )
                    }
                }
            } else {
                showRationaleDialog()
            }
        }
    }

    private fun getAddressAsync(
        context: Context,
        location: Location
    ) {
        val geoCoder = Geocoder(context)
        Thread {
            try {
                val addresses = geoCoder.getFromLocation(
                    location.latitude,
                    location.longitude,
                    1
                )
                binding.root.post {
                    val address = addresses[0].getAddressLine(0)
                    latestCity = City(
                        city = address,
//                        with(addresses[0]) {
//                            when {
//                                subAdminArea != null -> subAdminArea
//                                adminArea != null -> adminArea
//                                else -> countryName
//                            }
//                        },
                        lat = location.latitude,
                        lon = location.longitude
                    )
                    showAddressDialog(address)
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }.start()
    }

    private fun showAddressDialog(address: String) {
        this.let {
            AlertDialog.Builder(it)
                .setTitle(getString(R.string.dialog_address_title))
                .setMessage(address)
                .setPositiveButton(getString(R.string.dialog_address_get_weather)) { _, _ ->
                    val data = Intent().apply {
                        putExtra(RESULT_CITY, latestCity)
                    }
                    if (parent == null) {
                        setResult(Activity.RESULT_OK, data)
                    } else {
                        parent.setResult(Activity.RESULT_OK, data)
                    }
                    finish()
                }
                .setNegativeButton(getString(R.string.dialog_button_close)) { dialog, _ -> dialog.dismiss() }
                .create()
                .show()
        }
    }

    private val onLocationListener = object : LocationListener {
        override fun onLocationChanged(location: Location) {
            getAddressAsync(this@LocationActivity, location)
        }

        override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {}
        override fun onProviderEnabled(provider: String) {}
        override fun onProviderDisabled(provider: String) {}
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        val moscow = LatLng(55.75, 37.61)
        mMap.addMarker(MarkerOptions().position(moscow).title("Marker in Moscow"))
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(moscow, 12f))
        mMap.setOnMapLongClickListener { latLng ->
            val location = Location(LocationManager.GPS_PROVIDER).apply {
                latitude = latLng.latitude
                longitude = latLng.longitude
            }
            mMap.clear()
            mMap.addMarker(MarkerOptions().position(latLng))
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12f))
            getAddressAsync(this, location)
        }
    }

    companion object {
        private const val REFRESH_PERIOD = 60000L
        private const val MINIMAL_DISTANCE = 100f
        private const val LOCATION_REQUEST_CODE = 1002

        const val RESULT_CITY = "result_city"
    }
}