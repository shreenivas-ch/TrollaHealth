package com.trolla.healthsdk.feature_dashboard.presentation.dashboard_childlayouts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.trolla.healthsdk.R
import com.trolla.healthsdk.core.GenericAdapter
import com.trolla.healthsdk.databinding.FragmentDashboardRecommendedBinding
import com.trolla.healthsdk.feature_dashboard.data.AddToCartActionEvent
import com.trolla.healthsdk.feature_dashboard.data.DashboardResponse.DashboardProduct
import com.trolla.healthsdk.feature_dashboard.data.GoToCartEvent
import com.trolla.healthsdk.feature_dashboard.data.GoToProductDetailsEvent
import com.trolla.healthsdk.feature_dashboard.presentation.DashboardActivity
import com.trolla.healthsdk.feature_productslist.presentation.ProductsListFragment
import org.greenrobot.eventbus.EventBus


class DashboardRecommendedFragment : Fragment() {
    lateinit var binding: FragmentDashboardRecommendedBinding

    var bannersList = ArrayList<DashboardProduct>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_dashboard_recommended,
            container,
            false
        )

        val genericAdapter = GenericAdapter<DashboardProduct>(
            R.layout.item_dashboard_recommended_product
        )

        genericAdapter.setOnListItemViewClickListener(object :
            GenericAdapter.OnListItemViewClickListener {
            override fun onClick(view: View, position: Int) {
                EventBus.getDefault().post(GoToProductDetailsEvent(bannersList[position].product_id,bannersList[position].title))
            }

            override fun onAddToCartClick(view: View, position: Int) {
                EventBus.getDefault().post(AddToCartActionEvent(bannersList[position].product_id,1))
            }

            override fun goToCart() {
                EventBus.getDefault().post(GoToCartEvent())
            }

        })
        binding.rvRecommendedProducts.adapter = genericAdapter

        for (i in bannersList.indices) {
            if ((activity as DashboardActivity).cartItemsIdsArray.contains(bannersList?.get(i)?.product_id.toString())) {
                bannersList[i]?.cartQty = 1
            } else {
                bannersList[i]?.cartQty = 0
            }
        }

        genericAdapter.addItems(bannersList)

        binding.rvRecommendedProducts.setOnClickListener {
            var productsFragment = ProductsListFragment.newInstance(
                getString(R.string.recommended), ""
            )
            (activity as DashboardActivity).addOrReplaceFragment(productsFragment, true)
        }

        return binding.root
    }
}