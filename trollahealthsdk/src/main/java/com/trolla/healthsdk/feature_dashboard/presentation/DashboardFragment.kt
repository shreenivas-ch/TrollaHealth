package com.trolla.healthsdk.feature_dashboard.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.trolla.healthsdk.R
import com.trolla.healthsdk.core.ComponentGenerator
import com.trolla.healthsdk.data.Resource
import com.trolla.healthsdk.databinding.FragmentDashboardBinding
import com.trolla.healthsdk.feature_cart.presentation.CartFragment
import com.trolla.healthsdk.feature_cart.presentation.CartViewModel
import com.trolla.healthsdk.feature_dashboard.data.*
import com.trolla.healthsdk.feature_productdetails.presentation.ProductDetailsFragment
import com.trolla.healthsdk.utils.LogUtil
import com.trolla.healthsdk.utils.TrollaHealthUtility
import com.trolla.healthsdk.utils.asString
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.koin.java.KoinJavaComponent.inject


class DashboardFragment : Fragment() {
    lateinit var binding: FragmentDashboardBinding

    val dashboardViewModel: DashboardViewModel by inject(DashboardViewModel::class.java)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_dashboard,
            container,
            false
        )

        binding.lifecycleOwner = this
        dashboardViewModel.getDashboard()

        binding.dashboardSwifeRefresh.setOnRefreshListener {
            binding.dashboardSwifeRefresh.isRefreshing = false
            dashboardViewModel.getDashboard()
        }

        dashboardViewModel.progressStatus.observe(viewLifecycleOwner)
        {
            (activity as DashboardActivity).showHideProgressBar(it)
        }

        dashboardViewModel.dashboardResponseLiveData.observe(viewLifecycleOwner)
        {
            when (it) {
                is Resource.Success -> {

                    binding.llViewContainer.removeAllViews()
                    binding.llViewContainer.invalidate()

                    val response = dashboardViewModel.dashboardResponseLiveData.value
                    response?.data?.data?.homePagePositionsList?.forEach { homepageitem ->
                        if (homepageitem.name == "Placeholder 2") {
                            val dashboardComponentModel =
                                DashboardComponentModel(
                                    ComponentGenerator.TYPE_DASHBOARD_BANNER,
                                    homepageitem.banner_data
                                )
                            val fragment =
                                ComponentGenerator.getComponentObject(dashboardComponentModel)

                            if (fragment != null) {
                                childFragmentManager.beginTransaction()
                                    .add(binding.llViewContainer.id, fragment)
                                    .commit()
                            }
                        }
                        if (homepageitem.name == "Browse by Category") {
                            val dashboardComponentModel =
                                DashboardComponentModel(
                                    ComponentGenerator.TYPE_DASHBOARD_CATEGORIES,
                                    homepageitem.banner_data
                                )
                            val fragment =
                                ComponentGenerator.getComponentObject(dashboardComponentModel)

                            if (fragment != null) {
                                childFragmentManager.beginTransaction()
                                    .add(binding.llViewContainer.id, fragment)
                                    .commit()
                            }
                        }

                        if (homepageitem.name == "Featured Brands") {
                            val dashboardComponentModel =
                                DashboardComponentModel(
                                    ComponentGenerator.TYPE_DASHBOARD_FEATURED_BRANDS,
                                    homepageitem.banner_data
                                )
                            val fragment =
                                ComponentGenerator.getComponentObject(dashboardComponentModel)

                            if (fragment != null) {
                                childFragmentManager?.beginTransaction()
                                    .add(binding.llViewContainer.id, fragment)
                                    .commit()
                            }
                        }
                    }

                    response?.data?.data?.popularProdList?.let { trendingProducts ->
                        val dashboardComponentModel =
                            DashboardComponentModel(
                                ComponentGenerator.TYPE_DASHBOARD_TRENDING_PRODUCTS,
                                trendingProducts.product_list
                            )
                        val fragment =
                            ComponentGenerator.getComponentObject(dashboardComponentModel)

                        if (fragment != null) {
                            childFragmentManager?.beginTransaction()
                                .add(binding.llViewContainer.id, fragment)
                                .commit()
                        }
                    }

                    response?.data?.data?.recommendedProdList?.let { recommendedProducts ->
                        var dashboardComponentModel =
                            DashboardComponentModel(
                                ComponentGenerator.TYPE_DASHBOARD_RECOMMENDED_PRODUCTS,
                                recommendedProducts.product_list
                            )
                        var fragment =
                            ComponentGenerator.getComponentObject(dashboardComponentModel)

                        if (fragment != null) {
                            childFragmentManager?.beginTransaction()
                                .add(binding.llViewContainer.id, fragment)
                                .commit()
                        }
                    }

                    response?.data?.data?.newArrivalProdList?.let { newArrivalsProducts ->
                        var dashboardComponentModel =
                            DashboardComponentModel(
                                ComponentGenerator.TYPE_DASHBOARD_NEW_ARRIVALS,
                                newArrivalsProducts.product_list
                            )
                        var fragment =
                            ComponentGenerator.getComponentObject(dashboardComponentModel)

                        if (fragment != null) {
                            childFragmentManager?.beginTransaction()
                                .add(binding.llViewContainer.id, fragment)
                                .commit()
                        }
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
    fun doThis(addToCartActionEvent: AddToCartActionEvent) {
        (activity as DashboardActivity).cartViewModel.addToCart(addToCartActionEvent.product_id, addToCartActionEvent.qty)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun doThis(refreshDashboardEvent: RefreshDashboardEvent) {
        dashboardViewModel.getDashboard()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun doThis(goToCartEvent: GoToCartEvent) {

        var cartFragment = CartFragment.newInstance()

        (activity as DashboardActivity).addOrReplaceFragment(cartFragment, true)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun doThis(goToProductDetailsEvent: GoToProductDetailsEvent) {
        var productDetailsFragment = ProductDetailsFragment.newInstance(
            goToProductDetailsEvent.product_id.toString(),
            goToProductDetailsEvent.product_name ?: ""
        )

        (activity as DashboardActivity).addOrReplaceFragment(productDetailsFragment, true)
    }
}