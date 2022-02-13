package com.example.androidservicetest.utils

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

object Permissions {

    private const val TAG = "Permissions"
    const val LOCATION_PERMISSION_REQUEST_CODE = 100
    const val BLUETOOTH_PERMISSIONS_REQUEST_CODE = 101

    fun hasLocationPermission(context: Context): Boolean {
        val permission: String = if (hasAndroidQ()) {
            Log.d(TAG, "Application is running on android 10 (API 29) or higher")
            Manifest.permission.ACCESS_FINE_LOCATION
        } else {
            Log.d(TAG, "Application is running on android 9 (API 28) or lower")
            Manifest.permission.ACCESS_COARSE_LOCATION
        }
        return hasPermission(context, permission)
    }

    fun requestLocationPermission(activity: Activity) {
        val permissions = if (hasAndroidQ()) {
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION)
        } else {
            arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION)
        }
        ActivityCompat.requestPermissions(activity, permissions, LOCATION_PERMISSION_REQUEST_CODE)
    }

    fun getRequiredLocationPermission(): String {
        val permission = if (hasAndroidQ()) {
            Manifest.permission.ACCESS_FINE_LOCATION
        } else {
            Manifest.permission.ACCESS_COARSE_LOCATION
        }
        return permission
    }

    @RequiresApi(Build.VERSION_CODES.S)
    fun hasBluetoothPermissions(context: Context): Boolean {
        return hasBluetoothScanPermission(context) && hasBluetoothConnectPermission(context)
    }

    @RequiresApi(Build.VERSION_CODES.S)
    private fun hasBluetoothScanPermission(context: Context): Boolean {
        return hasPermission(context, Manifest.permission.BLUETOOTH_SCAN)
    }

    @RequiresApi(Build.VERSION_CODES.S)
    private fun hasBluetoothConnectPermission(context: Context): Boolean {
        return hasPermission(context, Manifest.permission.BLUETOOTH_CONNECT)
    }

    @RequiresApi(Build.VERSION_CODES.S)
    fun requestBluetoothPermissions(activity: Activity) {
        ActivityCompat.requestPermissions(
            activity,
            arrayOf(Manifest.permission.BLUETOOTH_SCAN, Manifest.permission.BLUETOOTH_CONNECT),
            BLUETOOTH_PERMISSIONS_REQUEST_CODE
        )
    }

    private fun hasPermission(context: Context, permission: String): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            permission
        ) == PackageManager.PERMISSION_GRANTED
    }

    fun hasAndroidS(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.S
    }

    private fun hasAndroidQ(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q
    }

    fun hasAndroidO(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.O
    }
}