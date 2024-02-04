package com.swipeapp.screens.addproduct

import android.content.Context
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.swipeapp.R

class ProductTypeAdapter(
    context: Context
) : ArrayAdapter<ProductType>(context, 0, ProductType.values()) {
    val layoutInflater: LayoutInflater = LayoutInflater.from(context)
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: layoutInflater.inflate(R.layout.item_product_drop_down, parent, false)
        getItem(position)?.let { productType ->
            setItem(view, productType)
        }
        return view
    }
    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view: View
        if (position == 0) {
            view = layoutInflater.inflate(R.layout.item_product_drop_down, parent, false)
            view.findViewById<TextView>(R.id.productType).apply {
                text = "Select Product Type"
                convertView?.context?.let { context ->
                    setTextColor(ContextCompat.getColor(context, R.color.light_grey))
                }
            }
            view.setOnClickListener {
                val root = parent.rootView
                root.dispatchKeyEvent(KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_BACK))
                root.dispatchKeyEvent(KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_BACK))
            }
        } else {
            view = layoutInflater.inflate(R.layout.item_product_drop_down, parent, false)
            getItem(position)?.let { productType ->
                setItem(view, productType)
            }
        }
        return view
    }

    override fun getItem(position: Int): ProductType? {
        if (position == 0) {
            return null
        }
        return super.getItem(position - 1)
    }

    override fun getCount() = super.getCount() + 1
    override fun isEnabled(position: Int) = position != 0
    private fun setItem(view: View, productType: ProductType) {
        val txProductType = view.findViewById<TextView>(R.id.productType)
        txProductType.text = productType.type
    }
}