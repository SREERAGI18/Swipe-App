package com.swipeapp.network.models

import com.google.gson.annotations.SerializedName

data class Product(
    val image: String?,
    val price: Double?,
    @SerializedName("product_name")
    val productName: String?,
    @SerializedName("product_type")
    val productType: String?,
    val tax: Double?
)