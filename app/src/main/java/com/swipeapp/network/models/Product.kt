package com.swipeapp.network.models

import com.google.gson.annotations.SerializedName

data class Product(
    @SerializedName("product_name")
    val productName: String?,
    @SerializedName("product_type")
    val productType: String?,
    @SerializedName("image")
    val image: String?,
    @SerializedName("price")
    val price: Double?,
    @SerializedName("tax")
    val tax: Double?
)