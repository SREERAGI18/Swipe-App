package com.swipeapp.screens.productlist

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.swipeapp.R
import com.swipeapp.databinding.ItemProductBinding
import com.swipeapp.room.entities.Products


class ProductListAdapter:RecyclerView.Adapter<ProductListAdapter.ProductVH>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductVH {
        return ProductVH(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.item_product, parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ProductVH, position: Int) {
        holder.bind(diff.currentList[position])
    }

    override fun getItemCount(): Int = diff.currentList.size

    fun updateProducts(newList:List<Products?>) {
        diff.submitList(newList)
    }

    private val productDiffUtil = object : DiffUtil.ItemCallback<Products>(){
        override fun areItemsTheSame(oldItem: Products, newItem: Products): Boolean {
            return oldItem.productName == newItem.productName
        }

        override fun areContentsTheSame(oldItem: Products, newItem: Products): Boolean {
            return oldItem == newItem
        }
    }

    private val diff = AsyncListDiffer(this, productDiffUtil)


    class ProductVH(private val binding:ItemProductBinding):RecyclerView.ViewHolder(binding.root) {
        fun bind(products: Products) {
            val options: RequestOptions = RequestOptions()
                .centerCrop()
                .placeholder(R.mipmap.no_image)
                .error(R.mipmap.no_image)

            Glide.with(itemView.context)
                .load(products.image)
                .apply(options)
                .into(binding.productImage)

            binding.product = products
        }
    }
}