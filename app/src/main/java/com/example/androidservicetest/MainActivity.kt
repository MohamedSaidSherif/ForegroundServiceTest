package com.example.androidservicetest

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.androidservicetest.utils.Permissions
import com.example.androidservicetest.utils.isServiceRunning

class MainActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "MainActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate")
        setContentView(R.layout.activity_main)
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart")
        if (!isServiceRunning(this, MyForegroundService::class.java)) {
           startMyForegroundService()
        }
    }

    override fun onPause() {
        Log.d(TAG, "onPause")
        super.onPause()
    }

    override fun onStop() {
        Log.d(TAG, "onStop")
        super.onStop()
    }

    override fun onDestroy() {
        Log.d(TAG, "onDestroy")
        super.onDestroy()
    }

    private fun startMyForegroundService() {
        Log.d(TAG, "Starting My Foreground Service")
        Intent(this, MyForegroundService::class.java).also { intent ->
            if (Permissions.hasAndroidO()) {
                Log.d(TAG, "Running on android O (26) or higher")
                startForegroundService(intent)
            } else {
                Log.d(TAG, "Running on android lower than O (26)")
                startService(intent)
            }
        }
    }
}