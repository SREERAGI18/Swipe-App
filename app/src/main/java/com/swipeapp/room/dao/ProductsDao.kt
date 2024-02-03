package com.swipeapp.room.dao

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
}