package com.trolla.healthsdk.feature_productslist.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.trolla.healthsdk.databinding.ItemProductBinding
import com.trolla.healthsdk.databinding.ProductsListFragmentBinding
import com.trolla.healthsdk.feature_productslist.data.ModelProduct

class ProductListAdapter(private val list: List<ModelProduct>) :
    RecyclerView.Adapter<ProductListAdapter.ProductListViewHolder>() {

    private lateinit var binding: ItemProductBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductListViewHolder {
        binding = ItemProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProductListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProductListViewHolder, position: Int) {
        val largeNews = list[position]
        holder.bind(largeNews)
    }

    override fun getItemCount(): Int = list.size

    class ProductListViewHolder(
        private val binding: ItemProductBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(largeNews: ModelProduct) {
            binding.item = largeNews
        }
    }
}