package com.aman.bootcamp

import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.core.content.ContextCompat
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.maps.*
import java.util.jar.Manifest
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import java.util.*


class MapActivity : AppCompatActivity(), OnMapReadyCallback{


    lateinit var mapView: SupportMapFragment
    lateinit var map: GoogleMap
    lateinit var mGoogleApiClient: GoogleApiClient
    var istraffic: Boolean = false
    var requestCode: Int = 101


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        mapView = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapView?.getMapAsync(this)

    }

    private fun checkPermissions() {
        if(ContextCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED)
        {
            requestPermissions(arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),requestCode )
        }else{
            getLocation()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when(requestCode){
            this.requestCode ->
                if (grantResults.size > 0 ){
                    if(grantResults.get(0) ==PackageManager.PERMISSION_GRANTED){
                        getLocation()
                    }else{
                        requestPermissions(arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),requestCode )
                    }
                }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    private fun getLocation() {
        map.isMyLocationEnabled = true
    }

    override fun onMapReady(googleMap: GoogleMap?) {
        map = googleMap!!
        map.uiSettings.isMapToolbarEnabled = false
        map.uiSettings.isIndoorLevelPickerEnabled = false
        checkPermissions()

        val latitude = 31.3249033
        val longitude = 75.5725334
        val zoomLevel = 15f // to see the streets

        val homeLatLng = LatLng(latitude, longitude)
        setMarker(homeLatLng)
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(homeLatLng, zoomLevel))
        setMapLongClick(map)
    }

    private fun setMarker(latLng: LatLng) {
        val snippet = String.format(
            Locale.getDefault(),
            "Lat: %1$.5f, Long: %2$.5f",
            latLng.latitude,
            latLng.longitude
        )
        map.addMarker(MarkerOptions()
            .position(latLng)
            .title(resources.getString(R.string.pin_info))
            .snippet(snippet))
    }

    private fun setMapLongClick(map: GoogleMap) {
        map.setOnMapLongClickListener {latLng ->
            val markerLatLong = LatLng(latLng.latitude, latLng.longitude)
            setMarker(markerLatLong)
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.normal_map -> {
            map.mapType = GoogleMap.MAP_TYPE_NORMAL
            true
        }
        R.id.hybrid_map -> {
            map.mapType = GoogleMap.MAP_TYPE_HYBRID
            true
        }
        R.id.satellite_map -> {
            map.mapType = GoogleMap.MAP_TYPE_SATELLITE
            true
        }
        R.id.traffic_map -> {
            if(istraffic)
            {
                map.isTrafficEnabled = false
                istraffic = false
            }else{
                map.isTrafficEnabled = true
                istraffic = true
            }
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this, SplashActivity::class.java)
        finish()
        startActivity(intent)
    }
}
