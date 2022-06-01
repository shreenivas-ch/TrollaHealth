package com.trolla.healthsdk.feature_productslist.presentation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.trolla.healthsdk.R
import com.trolla.healthsdk.databinding.ProductsListFragmentBinding
import com.trolla.healthsdk.feature_dashboard.data.DashboardResponse
import com.trolla.healthsdk.feature_dashboard.data.DashboardResponse.DashboardProduct
import org.koin.java.KoinJavaComponent.inject

class ProductsListFragment() : Fragment() {

    val title by lazy {
        arguments?.let {
            it.getString("title")
        }
    }

    val id by lazy {
        arguments?.let {
            it.getString("id")
        }
    }

    companion object {
        fun newInstance(title: String, id: String): ProductsListFragment {
            val bundle = Bundle()
            bundle.putString("title", title)
            bundle.putString("id", id)
            var productsListFragment = ProductsListFragment()
            productsListFragment.arguments = bundle
            return productsListFragment
        }
    }

    val productsListViewModel: ProductsListViewModel by inject(ProductsListViewModel::class.java)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val binding = DataBindingUtil.inflate<ProductsListFragmentBinding>(
            inflater,
            R.layout.products_list_fragment,
            container,
            false
        )

        binding.lifecycleOwner = this
        binding.viewModel = productsListViewModel

        productsListViewModel.headerTitle.value = title

        return binding.root

    }

}