package com.swipeapp.screens

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.swipeapp.R
import com.swipeapp.network.ResponseHandler
import com.swipeapp.network.models.AddProductRequest
import com.swipeapp.network.models.AddProductResponse
import com.swipeapp.network.repository.ProductRepository
import com.swipeapp.room.dao.ProductsDao
import com.swipeapp.room.dao.SyncAddProductsDao
import com.swipeapp.room.entities.Products
import com.swipeapp.room.entities.SyncAddProducts
import com.swipeapp.utils.isOnline
import com.swipeapp.worker.SyncAddProductReq
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class MainVM(
    private val productRepository: ProductRepository,
    private val syncAddProductsDao: SyncAddProductsDao,
    private val productsDao: ProductsDao
): ViewModel() {

    val addProductResponse = MutableStateFlow<ResponseHandler<AddProductResponse>>(ResponseHandler.Idle("Idle State"))
    fun addProduct(context: Context, addProductRequest: AddProductRequest) {
        viewModelScope.launch(Dispatchers.IO) {

            addProductResponse.emit(ResponseHandler.Loading())

            if(context.isOnline()) {
                productRepository.addProduct(addProductRequest).collect {
                    addProductResponse.emit(it)
                }
            }else{
                // try catch to avoid exception thrown due to type casting
                try {
                    productsDao.insertProduct(
                        Products(
                            productName = addProductRequest.productName,
                            productType = addProductRequest.productType,
                            image = if(addProductRequest.productImages.isNotEmpty()) addProductRequest.productImages[0] else null,
                            price = addProductRequest.productPrice.toDouble(),
                            tax = addProductRequest.productTax.toDouble()
                        )
                    )

                    syncAddProductsDao.insertSyncAddProduct(
                        SyncAddProducts(
                            productName = addProductRequest.productName,
                            productType = addProductRequest.productType,
                            image = if(addProductRequest.productImages.isNotEmpty()) addProductRequest.productImages[0] else null,
                            price = addProductRequest.productPrice,
                            tax = addProductRequest.productTax
                        )
                    )
                    SyncAddProductReq(context).addSyncQueue()

                }catch (e:Exception) {
                    e.printStackTrace()
                }
                addProductResponse.emit(
                    ResponseHandler.Success(
                        AddProductResponse(
                            message = "Product added for sync. Data will be synced on next connection to internet"
                        )
                    )
                )
            }
        }
    }
}