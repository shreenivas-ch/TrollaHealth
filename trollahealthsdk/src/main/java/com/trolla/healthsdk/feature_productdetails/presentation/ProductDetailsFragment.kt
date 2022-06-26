package com.trolla.healthsdk.feature_productdetails.presentation

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.trolla.healthsdk.R
import com.trolla.healthsdk.data.Resource
import com.trolla.healthsdk.databinding.AddAddressFragmentBinding
import com.trolla.healthsdk.databinding.ProductDetailsFragmentBinding
import com.trolla.healthsdk.databinding.ProductsListFragmentBinding
import com.trolla.healthsdk.feature_address.presentation.AddAddressViewModel
import com.trolla.healthsdk.feature_dashboard.data.DashboardResponse
import com.trolla.healthsdk.feature_dashboard.presentation.DashboardActivity
import com.trolla.healthsdk.feature_dashboard.presentation.adapters.BannersAdapter
import com.trolla.healthsdk.feature_productslist.presentation.ProductsListFragment
import com.trolla.healthsdk.utils.TrollaHealthUtility
import com.trolla.healthsdk.utils.asString
import com.trolla.healthsdk.utils.show
import org.koin.java.KoinJavaComponent
import org.koin.java.KoinJavaComponent.inject

class ProductDetailsFragment : Fragment() {

    val productDetailsViewModel: ProductDetailsViewModel by inject(
        ProductDetailsViewModel::class.java
    )

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

    lateinit var binding: ProductDetailsFragmentBinding

    companion object {
        fun newInstance(id: String, title: String): ProductDetailsFragment {
            val bundle = Bundle()
            bundle.putString("title", title)
            bundle.putString("id", id)
            var productsListFragment = ProductDetailsFragment()
            productsListFragment.arguments = bundle
            return productsListFragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.product_details_fragment,
            container,
            false
        )

        binding.lifecycleOwner = this
        binding.viewModel = productDetailsViewModel

        productDetailsViewModel.headerTitle.value = ""

        productDetailsViewModel.getProductDetailsResponseLiveData.observe(viewLifecycleOwner)
        {
            when (it) {
                is Resource.Success -> {
                    val response = productDetailsViewModel.getProductDetailsResponseLiveData.value
                    productDetailsViewModel.dashboardProduct.value = response?.data?.data?.detail!!

                    binding.llProductDetails.show()

                    var dashboardProduct = response?.data?.data?.detail!!
                    binding.txtTitle.text = dashboardProduct.title
                    manageProductImages(dashboardProduct.product_img)
                }

                is Resource.Error -> {
                    TrollaHealthUtility.showAlertDialogue(
                        requireContext(),
                        it.uiText?.asString(requireContext())
                    )
                }
            }
        }

        createTabs()

        productDetailsViewModel.progressStatus.observe(viewLifecycleOwner)
        {
            (activity as DashboardActivity).showHideProgressBar(it)
        }

        productDetailsViewModel.getProductDetails(id.toString())

        return binding.root
    }

    private fun manageProductImages(productImages: ArrayList<String>) {
        val sliderAdapter = ProductImagesAdapter(
            requireActivity(),
            productImages
        )

        var pager = binding.productsViewPager?.apply {
            adapter = sliderAdapter
            startAutoScroll()
            interval = 10000
            isCycle = true
            clipToPadding = false
        }

        binding.bannerDotsIndicator?.setViewPager(pager as ViewPager)
    }

    private fun createTabs() {
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
    }

}