package com.swipeapp

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.appcompat.app.AppCompatDelegate
import com.swipeapp.di.networkModule
import com.swipeapp.utils.Constants
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
        val channel = NotificationChannel(
            Constants.NOTIFICATION_CHANNEL_ID,
            "Swipe App",
            NotificationManager.IMPORTANCE_HIGH
        )
        channel.description = "To get notifications on successful product add"

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
        notificationManager.cancelAll()
    }

    override fun onTerminate() {
        super.onTerminate()
        Logger.levelError("Swipe App", "App Terminated")
    }
}