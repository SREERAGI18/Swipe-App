package com.swipeapp.room.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "products")
data class Products(
    @ColumnInfo(name = "product_name") @PrimaryKey(autoGenerate = false) val productName: String,
    @ColumnInfo(name = "product_type") val productType: String?,
    @ColumnInfo(name = "image") val image: String?,
    @ColumnInfo(name = "price") val price: Double?,
    @ColumnInfo(name = "tax") val tax: Double?
)