package com.swipeapp

import android.app.Application
import android.os.Build
import androidx.appcompat.app.AppCompatDelegate
import com.swipeapp.utils.Logger

class SwipeApp: Application() {

    override fun onCreate() {
        super.onCreate()

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            val channel = NotificationChannel(
//                ReminderNotificationManager.REMINDERS_CHANNEL_ID,
//                "Reminders",
//                NotificationManager.IMPORTANCE_HIGH
//            )
//            channel.description = "To get notifications about your study schedule"
//
//            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//            notificationManager.createNotificationChannel(channel)
//            notificationManager.cancelAll()
        }
    }

    override fun onTerminate() {
        super.onTerminate()
        Logger.levelError("Swipe App", "App Terminated")
    }
}