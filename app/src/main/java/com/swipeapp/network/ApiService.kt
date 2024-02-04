package com.swipeapp.network

import com.swipeapp.network.models.AddProductResponse
import com.swipeapp.network.models.Product
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {

    @GET("get")
    suspend fun getProducts():Response<List<Product>>

    @POST("add")
    suspend fun addProduct(@Body requestBody: RequestBody):Response<AddProductResponse>
}

