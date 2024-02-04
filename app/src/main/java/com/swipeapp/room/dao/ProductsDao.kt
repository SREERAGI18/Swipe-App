package com.swipeapp.room.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.swipeapp.room.entities.Products

@Dao
interface ProductsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllProducts(productList:List<Products?>):List<Long>

    @Query("SELECT * FROM products ORDER BY product_name ASC")
    suspend fun getAllProducts(): List<Products>

    @Query("SELECT * FROM products WHERE product_name LIKE :searchQuery ORDER BY product_name ASC")
    suspend fun searchProduct(searchQuery:String): List<Products>
}