package com.swipeapp.utils

import android.util.Log
import com.swipeapp.BuildConfig

object Logger {

    fun levelError(tag:String, message:String) {
        if(BuildConfig.DEBUG) {
            Log.e(tag, message)
        }
    }

    fun levelInfo(tag:String, message:String) {
        if(BuildConfig.DEBUG) {
            Log.i(tag, message)
        }
    }

    fun levelVerbose(tag:String, message:String) {
        if(BuildConfig.DEBUG) {
            Log.v(tag, message)
        }
    }

    fun levelWarn(tag:String, message:String) {
        if(BuildConfig.DEBUG) {
            Log.w(tag, message)
        }
    }
}