package com.swipeapp.worker

import android.content.Context
import android.provider.UserDictionary
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.swipeapp.di.provideApiService
import com.swipeapp.di.provideOkHttpClient
import com.swipeapp.di.provideRetrofit
import com.swipeapp.network.ResponseHandler
import com.swipeapp.network.models.AddProductRequest
import com.swipeapp.network.repository.ProductRepository
import com.swipeapp.network.repository.ProductRepositoryImpl
import com.swipeapp.room.SwipeDatabase
import com.swipeapp.room.dao.SyncAddProductsDao
import com.swipeapp.room.entities.SyncAddProducts
import com.swipeapp.utils.Logger

class SyncAddProductWorker(
    private val context: Context,
    workerParameters: WorkerParameters
): CoroutineWorker(context, workerParameters) {

    override suspend fun doWork(): Result {
        val syncAddProductsDao: SyncAddProductsDao = SwipeDatabase.getDatabase(context).syncAddProductsDao()
        val apiService = provideApiService(provideRetrofit(provideOkHttpClient()))
        val productRepository = ProductRepositoryImpl(apiService)

        val syncAddProducts = syncAddProductsDao.getAllSyncAddProducts()

        return if(syncAddProducts.isNotEmpty()) {

            syncAddProducts.forEach { syncAddProduct ->
                Logger.levelError("Add Product Sync", syncAddProduct.toString())
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

            syncAddProductsDao.deleteAllSyncAddProducts()

            Result.success()
        }else {
            Result.success()
        }

    }
}