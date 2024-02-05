package com.swipeapp.screens.addproduct

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.swipeapp.R
import org.jetbrains.annotations.Nullable


class ProductTypeAdapter(
    context: Context
) : ArrayAdapter<ProductType?>(context, 0, ProductType.values()) {

    // when drop down not expanded
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(parent.context).inflate(R.layout.item_product_drop_down, parent, false)
        getItem(position)?.let { productType ->
            initView(position, view)
        }
        return view
    }

    // expanded drop down view
    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(parent.context).inflate(R.layout.item_product_drop_down, parent, false)
        getItem(position)?.let { productType ->
            initView(position, view)
        }
        return view
    }

    private fun initView(
        position: Int, convertView: View
    ): View {
        val textViewName = convertView.findViewById<TextView>(R.id.productType)
        val currentItem: ProductType? = getItem(position)

        if (currentItem != null) {
            textViewName.text = currentItem.type
        }
        return convertView
    }
}