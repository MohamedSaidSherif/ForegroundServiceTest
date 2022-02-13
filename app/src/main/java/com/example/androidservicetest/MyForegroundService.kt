package com.example.androidservicetest

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.os.IBinder
import android.os.SystemClock
import android.util.Log
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.app.NotificationCompat
import androidx.core.graphics.drawable.toBitmap
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.TimeUnit


class MyForegroundService : Service() {

    companion object {
        private const val TAG = "MyForegroundService"
        private const val CHANNEL_ID = "Test_Channel_ID"
        private const val NOTIFICATION_ID = 150
    }

    private var counter = 0
    var backgroundExecutor: ScheduledExecutorService = Executors.newSingleThreadScheduledExecutor()
    private lateinit var periodicTask: Runnable

    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "onCreate")
        periodicTask = Runnable {
            counter++
            Log.d(TAG, "Counter = $counter")
        }
        createNotificationChannel()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d(TAG, "onStartCommand -> startId=$startId")
        generateForegroundNotification()
        backgroundExecutor.scheduleAtFixedRate(periodicTask, 0, 10, TimeUnit.SECONDS)
        return START_STICKY
    }

    override fun onBind(intent: Intent): IBinder? {
        Log.d(TAG, "onBind")
        return null
    }

    override fun onDestroy() {
        Log.d(TAG, "onDestroy")
    }

    private fun generateForegroundNotification() {
        val bitmap = AppCompatResources.getDrawable(
            this, R.drawable.ic_baseline_notifications_24)?.toBitmap()
        val uri: Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_baseline_notifications_24)
            .setContentTitle("Health Tracking Service")
            .setContentText("This service is used for sending body reading data every 5 minutes")
            .setLargeIcon(bitmap)
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText("This service is used for sending body reading data every 5 minutes")
            )
//            .setStyle(
//                NotificationCompat.BigPictureStyle().bigPicture(bitmap).bigLargeIcon(null)
//            )
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setSound(uri)
        startForeground(NOTIFICATION_ID, builder.build())
    }

    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.channel_name)
            val descriptionText = getString(R.string.channel_description)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            channel.enableLights(true)
            // Register the channel with the system
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}