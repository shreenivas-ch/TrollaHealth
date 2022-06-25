package com.trolla.healthsdk.feature_productslist.presentation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.trolla.healthsdk.R
import com.trolla.healthsdk.core.GenericAdapter
import com.trolla.healthsdk.data.Resource
import com.trolla.healthsdk.databinding.ProductsListFragmentBinding
import com.trolla.healthsdk.feature_cart.presentation.CartViewModel
import com.trolla.healthsdk.feature_dashboard.data.DashboardResponse
import com.trolla.healthsdk.feature_dashboard.data.DashboardResponse.DashboardProduct
import com.trolla.healthsdk.feature_dashboard.presentation.DashboardActivity
import com.trolla.healthsdk.feature_dashboard.presentation.DashboardViewModel
import com.trolla.healthsdk.feature_productdetails.presentation.ProductDetailsFragment
import com.trolla.healthsdk.utils.TrollaConstants
import com.trolla.healthsdk.utils.TrollaHealthUtility
import com.trolla.healthsdk.utils.asString
import okhttp3.internal.notify
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

    var page = TrollaConstants.PAGINATION_DEFAULT_INITIAL_PAGE
    var limit = TrollaConstants.PAGINATION_DEFAULT_LIMIT

    val productsListViewModel: ProductsListViewModel by inject(ProductsListViewModel::class.java)
    val cartViewModel: CartViewModel by inject(CartViewModel::class.java)

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

        binding.root.setOnClickListener {
            var productDetailsFragment = ProductDetailsFragment.newInstance();
            (activity as DashboardActivity).addOrReplaceFragment(productDetailsFragment, true)
        }

        val genericAdapter = GenericAdapter<DashboardProduct>(
            R.layout.item_dashboard_recommended_product
        )

        genericAdapter.setOnListItemViewClickListener(object :
            GenericAdapter.OnListItemViewClickListener {
            override fun onClick(view: View, position: Int) {
                Toast.makeText(view.context, "Clicked at row $position", Toast.LENGTH_LONG).show()
            }

            override fun onAddToCartClick(view: View, position: Int) {
                Toast.makeText(view.context, "Add to cart clicked at $position", Toast.LENGTH_LONG)
                    .show()

                cartViewModel
            }

        })
        binding.productsList.adapter = genericAdapter

        productsListViewModel.productsListResponseLiveData.observe(viewLifecycleOwner)
        {
            when (it) {
                is Resource.Success -> {
                    val response = productsListViewModel.productsListResponseLiveData.value
                    genericAdapter.addItems(response?.data?.data?.list!!)
                    genericAdapter.notifyDataSetChanged()
                }

                is Resource.Error -> {
                    TrollaHealthUtility.showAlertDialogue(
                        requireContext(),
                        it.uiText?.asString(requireContext())
                    )
                }
            }
        }

        productsListViewModel.progressStatus.observe(viewLifecycleOwner)
        {
            (activity as DashboardActivity).showHideProgressBar(it)
        }

        productsListViewModel.getProductsList(
            page.toString(),
            limit.toString(),
            "4196",
            "test"
        )

        return binding.root

    }

}