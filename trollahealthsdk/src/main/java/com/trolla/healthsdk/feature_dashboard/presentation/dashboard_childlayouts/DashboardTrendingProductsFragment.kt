package com.trolla.healthsdk.feature_dashboard.presentation.dashboard_childlayouts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.trolla.healthsdk.R
import com.trolla.healthsdk.core.GenericAdapter
import com.trolla.healthsdk.databinding.FragmentDashboardTrendingBinding
import com.trolla.healthsdk.feature_dashboard.data.DashboardResponse.DashboardProduct
import com.trolla.healthsdk.feature_dashboard.presentation.DashboardActivity
import com.trolla.healthsdk.feature_productslist.presentation.ProductsListFragment

class DashboardTrendingProductsFragment : Fragment() {
    lateinit var binding: FragmentDashboardTrendingBinding

    var bannersList = ArrayList<DashboardProduct>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_dashboard_trending,
            container,
            false
        )

        val genericAdapter = GenericAdapter<DashboardProduct>(
            R.layout.item_dashboard_trending_product
        )

        genericAdapter.setOnListItemViewClickListener(object :
            GenericAdapter.OnListItemViewClickListener {
            override fun onClick(view: View, position: Int) {
                Toast.makeText(view.context, "Clicked at row $position", Toast.LENGTH_LONG).show()
            }

        })
        binding.rvTrendingProducts.adapter = genericAdapter
        genericAdapter.addItems(bannersList)

        binding.txtTrendingShowAll.setOnClickListener {
            var productsFragment = ProductsListFragment.newInstance(
                getString(R.string.trending_products), ""
            )
            (activity as DashboardActivity).addOrReplaceFragment(productsFragment, true)
        }

        return binding.root
    }
}