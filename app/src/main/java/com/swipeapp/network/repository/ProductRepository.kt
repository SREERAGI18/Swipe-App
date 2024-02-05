package com.swipeapp.network.repository

import com.swipeapp.network.ApiService
import com.swipeapp.network.ResponseHandler
import com.swipeapp.network.models.AddProductRequest
import com.swipeapp.network.models.AddProductResponse
import com.swipeapp.network.models.Product
import com.swipeapp.room.entities.Products
import com.swipeapp.utils.toDBProductsList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File


interface ProductRepository {

    suspend fun getProducts(): Flow<ResponseHandler<List<Products?>>>

    suspend fun addProduct(addProductRequest: AddProductRequest): Flow<ResponseHandler<AddProductResponse>>

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

    override suspend fun addProduct(addProductRequest: AddProductRequest): Flow<ResponseHandler<AddProductResponse>> = flow {

        val builder: MultipartBody.Builder = MultipartBody.Builder().setType(MultipartBody.FORM)

        // add image files if available
        addProductRequest.productImages.forEach { filePath ->
            val image = File(filePath)
            if (image.exists()) {
                builder.addFormDataPart("files[]", image.name, image.asRequestBody(MultipartBody.FORM))
            }
        }

        builder.addFormDataPart("product_name", addProductRequest.productName)
        builder.addFormDataPart("product_type", addProductRequest.productType)
        builder.addFormDataPart("price", addProductRequest.productPrice)
        builder.addFormDataPart("tax", addProductRequest.productTax)

        val requestBody = builder.build()

        try {
            retrofitService.addProduct(requestBody).let { response ->
                if(response.isSuccessful) {
                    response.body()?.let { productList ->
                        emit(ResponseHandler.Success(productList))
                    }
                }else{
                    emit(
                        ResponseHandler.Error(
                            message = "Error in add product",
                            data = null
                        )
                    )
                }
            }
        }catch (e:Exception) {
            e.printStackTrace()
            emit(
                ResponseHandler.Error(
                    message = "Error in add product",
                    data = null
                )
            )
        }
    }
}