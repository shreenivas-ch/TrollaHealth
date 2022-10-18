package com.trolla.healthsdk.feature_dashboard.presentation.dashboard_childlayouts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.trolla.healthsdk.R
import com.trolla.healthsdk.core.GenericAdapter
import com.trolla.healthsdk.databinding.FragmentDashboardCategoriesBinding
import com.trolla.healthsdk.feature_dashboard.data.DashboardResponse
import com.trolla.healthsdk.feature_dashboard.data.DashboardResponse.HomePagePositionsListItem.BannerData
import com.trolla.healthsdk.feature_dashboard.presentation.DashboardActivity
import com.trolla.healthsdk.feature_productslist.presentation.ProductsListFragment

class DashboardCategoriesFragment : Fragment() {
    lateinit var binding: FragmentDashboardCategoriesBinding

    var bannersList = ArrayList<BannerData>()
    var apiDefinition: DashboardResponse.HomePagePositionsListItem.APIDefinition? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_dashboard_categories,
            container,
            false
        )

        val genericAdapter =
            GenericAdapter<BannerData>(R.layout.item_dashboard_category, bannersList)

        genericAdapter.setOnListItemViewClickListener(object :
            GenericAdapter.OnListItemViewClickListener {
            override fun onClick(view: View, position: Int) {

                var filterBy = apiDefinition?.filterBy
                var valueOf = apiDefinition?.valueOf
                var categoryid =
                    when (valueOf) {
                        "id" -> bannersList[position].id
                        "brand_id" -> bannersList[position].brand_id
                        "tag_id" -> bannersList[position].tag_id
                        "category_id" -> bannersList[position].category_id
                        else -> bannersList[position].id
                    }

                if(categoryid!=null && categoryid!=0) {
                    var productsFragment = ProductsListFragment.newInstance(
                        bannersList[position].category_name ?: "Category",
                        categoryid.toString(), filterBy!!
                    )
                    (activity as DashboardActivity).addOrReplaceFragment(productsFragment, true)
                }

            }

            override fun goToCart() {

            }

        })
        binding.rlDashboardCategories.adapter = genericAdapter
        //genericAdapter.addItems(bannersList)
        genericAdapter.notifyDataSetChanged()

        return binding.root
    }
}