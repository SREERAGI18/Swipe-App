package com.swipeapp.network.repository

import android.content.Context
import com.swipeapp.network.RetrofitProvider


class NetworkRepository(context: Context) {
    private val retrofitServices = RetrofitProvider.getService(context)
    
    private val MESSAGE = "message"

}