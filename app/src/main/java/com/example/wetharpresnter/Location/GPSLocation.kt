package com.example.wetharpresnter.Location

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Looper
import android.os.RemoteException
import android.provider.Settings
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import java.io.IOException


class GPSLocation (var context: Context) {
     var mutable= MutableLiveData<Pair<String,String>>()
     var mFusedLocationProviderClient: FusedLocationProviderClient
    init {
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)

    }


    @SuppressLint("MissingPermission")
     fun getLastLocation() {
        if (checkPermission()) {
            if (isLocatetionEnabled()) {
                requestNewLocationData()


            } else {
                Toast.makeText(context, "enable permission", Toast.LENGTH_LONG)
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                context.startActivity(intent)

            }
        } else {
            requestPermission()
        }

    }

     fun checkPermission(): Boolean {
        return ActivityCompat.checkSelfPermission(
            context, android.Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(
                    context, android.Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED

    }

     fun requestPermission() {
        ActivityCompat.requestPermissions(
            context as Activity, arrayOf(
                android.Manifest.permission.ACCESS_COARSE_LOCATION,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ), 50
        )
    }

    private fun isLocatetionEnabled(): Boolean {
        val locationManager: LocationManager =
            (context as Activity). getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }

    @SuppressLint("MissingPermission")
    private fun requestNewLocationData() {
        val mLocationRequest = com.google.android.gms.location.LocationRequest()
        mLocationRequest.setPriority(com.google.android.gms.location.LocationRequest.PRIORITY_HIGH_ACCURACY)
        mLocationRequest.setInterval(0)
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)
        mFusedLocationProviderClient.requestLocationUpdates(
            mLocationRequest,
            mLocationCallback,
            Looper.myLooper()
        )


    }

    private val mLocationCallback: LocationCallback = object : LocationCallback() {
        override fun onLocationResult(p0: LocationResult?) {
            val mylastLocaction: Location = p0!!.lastLocation

            mutable.postValue(Pair(mylastLocaction.longitude.toString(),mylastLocaction.latitude.toString()))
            try {
                var geocoder = Geocoder(context)
                var list : List<Address> = geocoder.getFromLocation(mylastLocaction.latitude,mylastLocaction.longitude,1) as List<Address>

            }  catch (e: IOException) {
        } catch (e: RemoteException) {

        }
              stopSearch()
        }

    }

    private fun stopSearch() {

        mFusedLocationProviderClient.removeLocationUpdates(mLocationCallback)
    }

}