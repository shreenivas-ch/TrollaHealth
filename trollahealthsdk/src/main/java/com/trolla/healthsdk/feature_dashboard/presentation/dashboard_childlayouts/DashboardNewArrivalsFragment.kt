package com.trolla.healthsdk.feature_dashboard.presentation.dashboard_childlayouts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.trolla.healthsdk.R
import com.trolla.healthsdk.core.GenericAdapter
import com.trolla.healthsdk.databinding.FragmentDashboardNewarrivalsBinding
import com.trolla.healthsdk.feature_dashboard.RefreshLocalCartDataEvent
import com.trolla.healthsdk.feature_dashboard.data.AddToCartActionEvent
import com.trolla.healthsdk.feature_dashboard.data.DashboardResponse.DashboardProduct
import com.trolla.healthsdk.feature_dashboard.data.GoToCartEvent
import com.trolla.healthsdk.feature_dashboard.data.GoToProductDetailsEvent
import com.trolla.healthsdk.feature_dashboard.presentation.DashboardActivity
import com.trolla.healthsdk.feature_productslist.presentation.ProductsListFragment
import com.trolla.healthsdk.utils.hide
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class DashboardNewArrivalsFragment : Fragment() {
    lateinit var binding: FragmentDashboardNewarrivalsBinding

    var productsList = ArrayList<DashboardProduct>()
    lateinit var genericAdapter: GenericAdapter<DashboardProduct>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_dashboard_newarrivals,
            container,
            false
        )

        genericAdapter = GenericAdapter(
            R.layout.item_dashboard_recommended_product,
            productsList
        )

        genericAdapter.setOnListItemViewClickListener(object :
            GenericAdapter.OnListItemViewClickListener {
            override fun onClick(view: View, position: Int) {
                EventBus.getDefault().post(
                    GoToProductDetailsEvent(
                        productsList[position].product_id,
                        productsList[position].title
                    )
                )
            }

            override fun onAddToCartClick(view: View, position: Int) {
                EventBus.getDefault()
                    .post(AddToCartActionEvent(productsList[position].product_id, 1))
            }

            override fun goToCart() {
                EventBus.getDefault().post(GoToCartEvent())
            }

        })
        binding.rvNewArrivals.adapter = genericAdapter

        for (i in productsList.indices) {
            if ((activity as DashboardActivity).cartItemsIdsArray.contains(productsList?.get(i)?.product_id.toString())) {
                productsList[i]?.cartQty = 1
            } else {
                productsList[i]?.cartQty = 0
            }
        }

        //genericAdapter.addItems(productsList)
        genericAdapter.notifyDataSetChanged()

        binding.txtNewArrivalsShowAll.setOnClickListener {
            var productsFragment = ProductsListFragment.newInstance(
                getString(R.string.new_arrivals), "",""
            )
            (activity as DashboardActivity).addOrReplaceFragment(productsFragment, true)
        }

        binding.txtNewArrivalsShowAll.hide()

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
    fun doThis(addToCartActionEvent: RefreshLocalCartDataEvent) {
        val cartProductIds = (activity as DashboardActivity).cartItemsIdsArray
        for (i in productsList.indices) {
            if (cartProductIds.contains(productsList[i].product_id.toString())) {
                productsList[i].cartQty = 1
            } else {
                productsList[i].cartQty = 0
            }
        }

        genericAdapter.notifyDataSetChanged()
    }
}