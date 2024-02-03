package com.swipeapp.network.repository

import com.swipeapp.network.ApiService
import com.swipeapp.network.ResponseHandler
import com.swipeapp.network.models.Product
import com.swipeapp.room.entities.Products
import com.swipeapp.utils.toDBProductsList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow


interface ProductRepository {

    suspend fun getProducts(): Flow<ResponseHandler<List<Products?>>>

}

class ProductRepositoryImpl(private val retrofitService:ApiService) : ProductRepository {

    override suspend fun getProducts(): Flow<ResponseHandler<List<Products?>>> = flow {

        try {
            retrofitService.getProducts().let { response ->
                if(response.isSuccessful) {
                    response.body()?.let { productList ->
                        emit(ResponseHandler.Success(productList.toDBProductsList()))
                    }
                }else{
                    emit(
                        ResponseHandler.Error(
                            message = "Error in get products",
                            data = null
                        )
                    )
                }
            }
        }catch (e:Exception) {
            e.printStackTrace()
            emit(
                ResponseHandler.Error(
                    message = "Error in get products",
                    data = null
                )
            )
        }
    }
}