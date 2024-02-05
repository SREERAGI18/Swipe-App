package com.swipeapp.room.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "sync_add_products")
data class SyncAddProducts(
    @ColumnInfo(name = "product_name") @PrimaryKey(autoGenerate = false) val productName: String,
    @ColumnInfo(name = "product_type") val productType: String,
    @ColumnInfo(name = "image") val image: String?,
    @ColumnInfo(name = "price") val price: String,
    @ColumnInfo(name = "tax") val tax: String,
    @ColumnInfo(name = "images") val images: List<String> = emptyList(),
)
