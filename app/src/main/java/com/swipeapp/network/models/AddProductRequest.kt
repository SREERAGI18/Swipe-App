package com.swipeapp.network.models

import com.google.gson.annotations.SerializedName

data class AddProductRequest(
    @SerializedName("product_name")
    val productName:String,
    @SerializedName("product_type")
    val productType:String,
    @SerializedName("price")
    val productPrice:String,
    @SerializedName("tax")
    val productTax:String,
    @SerializedName("files")
    val productImages:List<String> = emptyList()
)
