package com.swipeapp.network.models

import com.google.gson.annotations.SerializedName

data class AddProductResponse(
    @SerializedName("message")
    val message: String? = null,
    @SerializedName("product_details")
    val productDetails: Product? = null,
    @SerializedName("product_id")
    val productId: Int? = null,
    @SerializedName("success")
    val success: Boolean? = null
)