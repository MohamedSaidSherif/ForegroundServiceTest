package com.example.androidservicetest.utils

import android.app.ActivityManager
import android.content.Context

fun isServiceRunning(context: Context, serviceClass: Class<*>): Boolean {
    try {
        val manager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        for (service in manager.getRunningServices(
            Int.MAX_VALUE
        )) {
            if (serviceClass.name == service.service.className) {
                return true
            }
        }
    } catch (e: Exception) {
        return false
    }
    return false
}