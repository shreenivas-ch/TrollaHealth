package com.trolla.healthsdk.feature_productslist.presentation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import com.trolla.healthsdk.R
import com.trolla.healthsdk.core.GenericAdapter
import com.trolla.healthsdk.data.Resource
import com.trolla.healthsdk.databinding.ProductsListFragmentBinding
import com.trolla.healthsdk.feature_cart.presentation.CartFragment
import com.trolla.healthsdk.feature_cart.presentation.CartViewModel
import com.trolla.healthsdk.feature_dashboard.data.DashboardResponse
import com.trolla.healthsdk.feature_dashboard.data.DashboardResponse.DashboardProduct
import com.trolla.healthsdk.feature_dashboard.presentation.DashboardActivity
import com.trolla.healthsdk.feature_dashboard.presentation.DashboardViewModel
import com.trolla.healthsdk.feature_productdetails.presentation.ProductDetailsFragment
import com.trolla.healthsdk.ui_utils.PaginationScrollListener
import com.trolla.healthsdk.utils.LogUtil
import com.trolla.healthsdk.utils.TrollaConstants
import com.trolla.healthsdk.utils.TrollaHealthUtility
import com.trolla.healthsdk.utils.asString
import okhttp3.internal.notify
import org.koin.java.KoinJavaComponent.inject

class ProductsListFragment() : Fragment() {

    var cartItemsIdsArray = ArrayList<String>()

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

    var productsList = ArrayList<DashboardProduct>()
    lateinit var genericAdapter: GenericAdapter<DashboardProduct>

    var isLoading = false
    var isLastPage = false

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

        productsListViewModel.headerBackButton.value = 1
        productsListViewModel.headerTitle.value = title

        binding.commonHeader.imgBack.setOnClickListener {
            parentFragmentManager?.popBackStack()
        }

        genericAdapter = GenericAdapter<DashboardProduct>(
            R.layout.item_dashboard_recommended_product, productsList
        )

        genericAdapter.setOnListItemViewClickListener(object :
            GenericAdapter.OnListItemViewClickListener {
            override fun onClick(view: View, position: Int) {

                val response = productsListViewModel.productsListResponseLiveData.value

                var product_id = response?.data?.data?.list?.get(position)?.product_id
                var product_name = response?.data?.data?.list?.get(position)?.product_name

                var productDetailsFragment = ProductDetailsFragment.newInstance(
                    product_id.toString(),
                    product_name.toString()
                )

                (activity as DashboardActivity).addOrReplaceFragment(productDetailsFragment, true)

            }

            override fun onAddToCartClick(view: View, position: Int) {

                val response = productsListViewModel.productsListResponseLiveData.value

                cartViewModel.addToCart(
                    response?.data?.data?.list?.get(position)?.product_id!!,
                    1
                )
            }

            override fun goToCart() {
                var cartFragment = CartFragment.newInstance()

                (activity as DashboardActivity).addOrReplaceFragment(cartFragment, true)
            }
        })

        binding.productsList.adapter = genericAdapter
        binding.productsList.addOnScrollListener(object :
            PaginationScrollListener(binding.productsList.layoutManager as GridLayoutManager) {
            override fun loadMoreItems() {
                page += 1
                getProductsList()
            }

            override fun isLastPage(): Boolean {
                return isLastPage
            }

            override fun isLoading(): Boolean {
                return isLoading
            }

        })

        productsListViewModel.productsListResponseLiveData.observe(viewLifecycleOwner)
        {
            when (it) {
                is Resource.Success -> {
                    //productsList.clear()
                    productsList.addAll(productsListViewModel.productsListResponseLiveData.value?.data?.data?.list!!)

                    for (i in productsList?.indices) {
                        if (cartItemsIdsArray.contains(productsList?.get(i)?.product_id.toString())) {
                            productsList?.get(i)?.cartQty = 1
                        } else {
                            productsList?.get(i)?.cartQty = 0
                        }
                    }

                    //genericAdapter.addItems(productsList)
                    genericAdapter.notifyDataSetChanged()

                    if (productsListViewModel.productsListResponseLiveData.value?.data?.data?.list!!.isNullOrEmpty()) {
                        isLastPage = true
                    }
                }

                is Resource.Error -> {
                    TrollaHealthUtility.showAlertDialogue(
                        requireContext(),
                        it.uiText?.asString(requireContext())
                    )
                }
            }
        }

        cartViewModel.addToCartResponseLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Success -> {
                    val response = cartViewModel.addToCartResponseLiveData.value
                    cartItemsIdsArray.clear()
                    for (i in response?.data?.data?.cart?.products?.indices ?: arrayListOf()) {
                        cartItemsIdsArray.add(response?.data?.data?.cart?.products?.get(i)?.product?.product_id.toString())
                    }

                    //refreshProductsList()
                    getProductsList()
                    (activity as DashboardActivity).cartViewModel.addToCartResponseLiveData.value =
                        it
                }

                is Resource.Error -> {
                    TrollaHealthUtility.showAlertDialogue(
                        requireContext(),
                        it.uiText?.asString(requireContext())
                    )
                }
            }
        }

        cartViewModel.cartDetailsResponseLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Success -> {
                    val response = cartViewModel.cartDetailsResponseLiveData.value
                    cartItemsIdsArray.clear()
                    for (i in response?.data?.data?.cart?.products?.indices ?: arrayListOf()) {
                        cartItemsIdsArray.add(response?.data?.data?.cart?.products?.get(i)?.product?.product_id.toString())
                    }

                    LogUtil.printObject(cartItemsIdsArray)

                    getProductsList()
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
            isLoading = it
        }

        cartViewModel.getCartDetails()

        return binding.root

    }

    private fun refreshProductsList() {
        for (i in productsList.indices) {
            if (cartItemsIdsArray.contains(productsList[i].product_id.toString())) {
                productsList[i].cartQty == 1
            } else {
                productsList[i].cartQty == 0
            }
        }

        //genericAdapter.addItems(productsList)
        genericAdapter.notifyDataSetChanged()
    }

    fun getProductsList() {
        productsListViewModel.getProductsList(
            page.toString(),
            limit.toString(),
            id!!,
            "test"
        )
    }

}