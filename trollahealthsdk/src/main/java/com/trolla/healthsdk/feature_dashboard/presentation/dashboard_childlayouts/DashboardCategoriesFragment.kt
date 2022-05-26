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
import com.trolla.healthsdk.feature_dashboard.data.DashboardResponse.HomePagePositionsListItem.BannerData

class DashboardCategoriesFragment: Fragment() {
    lateinit var binding: FragmentDashboardCategoriesBinding

    var bannersList = ArrayList<BannerData>()

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

        val genericAdapter = GenericAdapter<BannerData>(R.layout.item_dashboard_category)

        genericAdapter.setOnListItemViewClickListener(object : GenericAdapter.OnListItemViewClickListener{
            override fun onClick(view: View, position: Int) {
                Toast.makeText(view.context, "Clicked at row $position", Toast.LENGTH_LONG).show()
            }

        })
        binding.rlDashboardCategories.adapter = genericAdapter
        genericAdapter.addItems(bannersList)

        return binding.root
    }
}