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
import com.trolla.healthsdk.databinding.FragmentDashboardCategoriesBinding
import com.trolla.healthsdk.databinding.FragmentDashboardFeaturedBrandsBinding
import com.trolla.healthsdk.feature_dashboard.data.DashboardResponse
import com.trolla.healthsdk.feature_dashboard.data.DashboardResponse.HomePagePositionsListItem.BannerData
import com.trolla.healthsdk.feature_dashboard.presentation.DashboardActivity
import com.trolla.healthsdk.feature_productslist.presentation.ProductsListFragment

class DashboardFeaturedBrandsFragment:Fragment() {
    lateinit var binding: FragmentDashboardFeaturedBrandsBinding

    var bannersList = ArrayList<BannerData>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_dashboard_featured_brands,
            container,
            false
        )

        val genericAdapter = GenericAdapter<BannerData>(
            R.layout.item_dashboard_featured_brand,bannersList)

        genericAdapter.setOnListItemViewClickListener(object : GenericAdapter.OnListItemViewClickListener{
            override fun onClick(view: View, position: Int) {
                var productsFragment = ProductsListFragment.newInstance(
                    bannersList[position].name,
                    bannersList[position].brand_id.toString()
                )
                (activity as DashboardActivity).addOrReplaceFragment(productsFragment, true)
            }

        })
        binding.rvFeaturedBrands.adapter = genericAdapter
        //genericAdapter.addItems(bannersList)
        genericAdapter.notifyDataSetChanged()

        return binding.root
    }
}