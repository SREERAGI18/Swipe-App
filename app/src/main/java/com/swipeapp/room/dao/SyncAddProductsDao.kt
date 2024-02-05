package com.swipeapp.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.swipeapp.room.entities.Products
import com.swipeapp.room.entities.SyncAddProducts

@Dao
interface SyncAddProductsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllSyncAddProducts(productList:List<SyncAddProducts?>):List<Long>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSyncAddProduct(product:SyncAddProducts?):Long

    @Query("SELECT * FROM sync_add_products")
    suspend fun getAllSyncAddProducts(): List<SyncAddProducts>

    @Query("DELETE FROM sync_add_products")
    suspend fun deleteAllSyncAddProducts()
}