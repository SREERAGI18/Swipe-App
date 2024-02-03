package com.swipeapp

import android.app.Application
import android.os.Build
import androidx.appcompat.app.AppCompatDelegate
import com.swipeapp.di.networkModule
import com.swipeapp.utils.Logger
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidFileProperties
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext.startKoin

class SwipeApp: Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            // Log Koin into Android logger
            androidLogger()
            // Reference Android context
            androidContext(this@SwipeApp)
            // Load modules
            modules(networkModule)
            androidFileProperties()
        }

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