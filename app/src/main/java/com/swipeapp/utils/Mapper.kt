package com.swipeapp.utils

import com.swipeapp.network.models.Product
import com.swipeapp.room.entities.Products

fun Product.toDBProducts():Products? {
    return productName?.let { Products(it, productType, image, price, tax) }
}

fun List<Product>.toDBProductsList():List<Products?> {
    return map { it.toDBProducts() }
}

fun Products.toNetworkProduct():Product {
    return Product(productName, productType, image, price, tax)
}