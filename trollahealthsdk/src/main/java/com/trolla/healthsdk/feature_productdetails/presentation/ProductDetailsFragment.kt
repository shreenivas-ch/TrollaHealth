package com.trolla.healthsdk.feature_productdetails.presentation

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.google.android.material.tabs.TabLayout
import com.trolla.healthsdk.R
import com.trolla.healthsdk.databinding.AddAddressFragmentBinding
import com.trolla.healthsdk.databinding.ProductDetailsFragmentBinding
import com.trolla.healthsdk.databinding.ProductsListFragmentBinding
import com.trolla.healthsdk.feature_address.presentation.AddAddressViewModel
import org.koin.java.KoinJavaComponent
import org.koin.java.KoinJavaComponent.inject

class ProductDetailsFragment : Fragment() {

    companion object {
        fun newInstance() = ProductDetailsFragment()
    }

    val productDetailsViewModel: ProductDetailsViewModel by inject(
        ProductDetailsViewModel::class.java
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        var binding = DataBindingUtil.inflate<ProductDetailsFragmentBinding>(
            inflater,
            R.layout.product_details_fragment,
            container,
            false
        )

        binding.lifecycleOwner = this
        binding.viewModel = productDetailsViewModel

        binding.tabLayout.addTab(
            binding.tabLayout.newTab().setText(getString(R.string.product_description))
        )
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText(getString(R.string.Uses)))
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText(getString(R.string.Benefits)))
        binding.tabLayout.addTab(
            binding.tabLayout.newTab().setText(getString(R.string.storage_conditions))
        )

        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {

                when (tab?.position) {
                    0 -> {

                    }
                    1 -> {

                    }
                    2 -> {

                    }
                    3 -> {

                    }
                }

            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }

        })

        return binding.root
    }

}