package com.swipeapp.network.repository

import com.swipeapp.network.ApiService
import com.swipeapp.network.models.Product
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow


interface ProductRepository {

    suspend fun getProducts(): Flow<List<Product>>

}

class ProductRepositoryImpl(private val retrofitService:ApiService) : ProductRepository {

    override suspend fun getProducts(): Flow<List<Product>> = flow {
        emit(retrofitService.getProducts())
    }
}