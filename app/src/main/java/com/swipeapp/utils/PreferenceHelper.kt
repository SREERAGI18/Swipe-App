package com.swipeapp.utils

import android.content.Context

class PreferenceHelper(context: Context) {

    private val preferences by lazy { context.getSharedPreferences(context.packageName, Context.MODE_PRIVATE) }


}