package com.swipeapp.worker

import android.content.Context
import android.provider.UserDictionary
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.swipeapp.network.ResponseHandler
import com.swipeapp.network.models.AddProductRequest
import com.swipeapp.network.repository.ProductRepository
import com.swipeapp.room.SwipeDatabase
import com.swipeapp.room.dao.SyncAddProductsDao
import com.swipeapp.room.entities.SyncAddProducts

class SyncAddProductWorker(
    private val context: Context,
    workerParameters: WorkerParameters,
    private val syncAddProductsDao: SyncAddProductsDao,
    private val productRepository: ProductRepository
): CoroutineWorker(context, workerParameters) {

    private val syncAddGroupDao = SwipeDatabase.getDatabase(context).syncAddProductsDao()
    override suspend fun doWork(): Result {
        val syncAddProducts = syncAddGroupDao.getAllSyncAddProducts()

        return if(syncAddProducts.isNotEmpty()) {

            syncAddProducts.forEach { syncAddProduct ->
                productRepository.addProduct(
                    AddProductRequest(
                        productName = syncAddProduct.productName,
                        productPrice = syncAddProduct.price,
                        productType = syncAddProduct.productType,
                        productTax = syncAddProduct.tax,
                        productImages = syncAddProduct.images
                    )
                )
            }

            Result.success()
        }else {
            Result.success()
        }

    }
}