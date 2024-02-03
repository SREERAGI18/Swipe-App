package com.swipeapp.screens.productlist

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.swipeapp.R
import com.swipeapp.network.ResponseHandler
import com.swipeapp.network.repository.ProductRepository
import com.swipeapp.room.dao.ProductsDao
import com.swipeapp.room.entities.Products
import com.swipeapp.utils.isOnline
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class ProductListVM(
    private val productRepository: ProductRepository,
    private val productsDao: ProductsDao
):ViewModel() {

    val productList = MutableStateFlow<ResponseHandler<List<Products?>>>(ResponseHandler.Idle("Idle State"))

    fun getProductList(context: Context) {
        viewModelScope.launch(Dispatchers.IO) {

            productList.emit(ResponseHandler.Loading())

            val products = productsDao.getAllProducts()

            if(products.isEmpty()) {
                if(context.isOnline()) {
                    productRepository.getProducts().collect {
                        productList.emit(it)

                        it.data?.let { products ->
                            productsDao.insertAllProducts(products)
                        }
                    }
                }else{
                    productList.emit(
                        ResponseHandler.Error(message = context.getString(R.string.no_internet_msg))
                    )
                }
            }else{
                productList.emit(ResponseHandler.Success(products))
            }
        }
    }
}