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
import com.swipeapp.utils.isOnline
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class MainVM(
    private val productRepository: ProductRepository
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
                addProductResponse.emit(
                    ResponseHandler.Error(message = context.getString(R.string.no_internet_msg))
                )
            }
        }
    }
}