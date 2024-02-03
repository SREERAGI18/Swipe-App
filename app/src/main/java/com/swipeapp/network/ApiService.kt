package com.swipeapp.network

import com.swipeapp.network.models.Product
import retrofit2.Response
import retrofit2.http.GET

interface ApiService {

    @GET("get")
    suspend fun getProducts():Response<List<Product>>
}

