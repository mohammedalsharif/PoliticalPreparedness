package com.example.android.politicalpreparedness.util

import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat

object LocationUtils {
     fun isPermissionGranted(context:Context): Boolean {
        return !(ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
    }

}