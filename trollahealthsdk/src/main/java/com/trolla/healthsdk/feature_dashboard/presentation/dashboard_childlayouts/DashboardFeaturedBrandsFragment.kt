package com.trolla.healthsdk.feature_dashboard.presentation.dashboard_childlayouts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.trolla.healthsdk.R
import com.trolla.healthsdk.core.GenericAdapter
import com.trolla.healthsdk.databinding.FragmentDashboardFeaturedBrandsBinding
import com.trolla.healthsdk.feature_dashboard.data.DashboardResponse
import com.trolla.healthsdk.feature_dashboard.data.DashboardResponse.HomePagePositionsListItem.BannerData
import com.trolla.healthsdk.feature_dashboard.presentation.DashboardActivity
import com.trolla.healthsdk.feature_productslist.presentation.ProductsListFragment

class DashboardFeaturedBrandsFragment : Fragment() {
    lateinit var binding: FragmentDashboardFeaturedBrandsBinding

    var bannersList = ArrayList<BannerData>()
    var apiDefinition: DashboardResponse.HomePagePositionsListItem.APIDefinition? = null

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
            R.layout.item_dashboard_featured_brand, bannersList
        )

        genericAdapter.setOnListItemViewClickListener(object :
            GenericAdapter.OnListItemViewClickListener {
            override fun onClick(view: View, position: Int) {

                if (apiDefinition != null) {

                    var filterBy = apiDefinition?.filterBy
                    var valueOf = apiDefinition?.valueOf
                    var id =
                        when (valueOf) {
                            "id" -> bannersList[position].id
                            "brand_id" -> bannersList[position].brand_id
                            "tag_id" -> bannersList[position].tag_id
                            "category_id" -> bannersList[position].category_id
                            else -> bannersList[position].id
                        }

                    var productsFragment = ProductsListFragment.newInstance(
                        bannersList[position].name,
                        id.toString(),
                        filterBy!!
                    )
                    (activity as DashboardActivity).addOrReplaceFragment(productsFragment, true)
                }
            }

        })
        binding.rvFeaturedBrands.adapter = genericAdapter
        //genericAdapter.addItems(bannersList)
        genericAdapter.notifyDataSetChanged()

        return binding.root
    }
}