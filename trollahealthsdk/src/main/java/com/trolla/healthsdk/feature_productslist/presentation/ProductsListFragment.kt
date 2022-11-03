package com.trolla.healthsdk.feature_productslist.presentation

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import com.trolla.healthsdk.R
import com.trolla.healthsdk.core.GenericAdapter
import com.trolla.healthsdk.data.Resource
import com.trolla.healthsdk.databinding.ProductsListFragmentBinding
import com.trolla.healthsdk.feature_cart.data.models.AddToCartSuccessEvent
import com.trolla.healthsdk.feature_cart.data.models.CartCountChangeEvent
import com.trolla.healthsdk.feature_cart.data.models.CartDetailsRefreshedEvent
import com.trolla.healthsdk.feature_cart.presentation.CartFragment
import com.trolla.healthsdk.feature_dashboard.data.DashboardResponse.DashboardProduct
import com.trolla.healthsdk.feature_dashboard.presentation.DashboardActivity
import com.trolla.healthsdk.feature_dashboard.presentation.getCartViewModel
import com.trolla.healthsdk.feature_productdetails.presentation.ProductDetailsFragment
import com.trolla.healthsdk.feature_productslist.data.RefreshProductListEvent
import com.trolla.healthsdk.feature_search.presentation.SearchFragment
import com.trolla.healthsdk.ui_utils.PaginationScrollListener
import com.trolla.healthsdk.utils.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
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

    val filterBy by lazy {
        arguments?.let {
            it.getString("filterBy")
        }
    }

    var page = TrollaConstants.PAGINATION_DEFAULT_INITIAL_PAGE
    var limit = TrollaConstants.PAGINATION_DEFAULT_LIMIT

    val productsListViewModel: ProductsListViewModel by inject(ProductsListViewModel::class.java)

    var productsList = ArrayList<DashboardProduct>()
    lateinit var genericAdapter: GenericAdapter<DashboardProduct>

    var isLoading = false
    var isLastPage = false

    lateinit var binding: ProductsListFragmentBinding

    companion object {
        fun newInstance(title: String, id: String, filterBy: String): ProductsListFragment {
            val bundle = Bundle()
            bundle.putString("title", title)
            bundle.putString("id", id)
            bundle.putString("filterBy", filterBy)
            var productsListFragment = ProductsListFragment()
            productsListFragment.arguments = bundle
            return productsListFragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.products_list_fragment,
            container,
            false
        )

        binding.lifecycleOwner = this
        binding.viewModel = productsListViewModel

        productsListViewModel.headerBackButton.value = 1
        productsListViewModel.headerBottomLine.value = 0
        productsListViewModel.headerTitle.value = title

        binding.commonHeader.imgBack.setOnClickListener {
            parentFragmentManager?.popBackStack()
        }

        binding.llSearch.setOnClickListener {
            (activity as DashboardActivity).addOrReplaceFragment(SearchFragment.newInstance(), true)
        }

        genericAdapter = GenericAdapter(
            R.layout.item_dashboard_recommended_product, productsList
        )

        genericAdapter.setOnListItemViewClickListener(object :
            GenericAdapter.OnListItemViewClickListener {
            override fun onClick(view: View, position: Int) {

                var product_id = productsList[position]?.product_id
                var product_name = productsList[position]?.product_name

                var productDetailsFragment = ProductDetailsFragment.newInstance(
                    product_id!!,
                    product_name.toString()
                )

                (activity as DashboardActivity).addOrReplaceFragment(productDetailsFragment, true)

            }

            override fun onAddToCartClick(view: View, position: Int) {

                getCartViewModel().addToCart(
                    productsList[position]?.product_id!!,
                    1, from = "product list"
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
                        if ((activity as DashboardActivity).cartItemsIdsArray.contains(
                                productsList?.get(
                                    i
                                )?.product_id.toString()
                            )
                        ) {
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

        productsListViewModel.progressStatus.observe(viewLifecycleOwner)
        {
            (activity as DashboardActivity).showHideProgressBar(it)
            isLoading = it
        }

        binding.commonHeader.rlCart.setOnClickListener {
            var cartFragment = CartFragment.newInstance()
            (activity as DashboardActivity).addOrReplaceFragment(cartFragment, true)
        }

        Handler(Looper.getMainLooper()).postDelayed({ getCartViewModel().getCartDetails() }, 200)

        return binding.root

    }

    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun doThis(refreshProductListEvent: RefreshProductListEvent) {
        LogUtil.printObject("----->product list fragment: refreshProductListEvent")
        getCartViewModel().getCartDetails()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun doThis(cartDetailsRefreshedEvent: CartDetailsRefreshedEvent) {
        getCartViewModel().cartDetailsResponseLiveData.value?.data?.data?.cart?.products?.let {
            getProductsList()
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun doThis(addToCartSuccessEvent: AddToCartSuccessEvent) {
        getCartViewModel().addToCartResponseLiveData.value?.data?.data?.cart?.products?.let {
            getProductsList()
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun doThis(cartCountChangeEvent: CartCountChangeEvent) {
        updateCartCount(getCartViewModel().cartCountLiveData.value ?: 0)
    }

    fun updateCartCount(count: Int) {
        if (count == 0) {
            productsListViewModel.headerCartIcon.value = 0
        } else {
            productsListViewModel.headerCartIcon.value = 1
            productsListViewModel.headerCartCount.value = count
        }
    }

    fun getProductsList() {
        productsListViewModel.getProductsList(
            page.toString(),
            limit.toString(),
            id!!,
            filterBy ?: ""
        )
    }

}