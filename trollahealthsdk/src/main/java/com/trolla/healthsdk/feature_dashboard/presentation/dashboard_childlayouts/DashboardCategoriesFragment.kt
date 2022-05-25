package com.trolla.healthsdk.feature_dashboard.presentation.dashboard_childlayouts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.trolla.healthsdk.R
import com.trolla.healthsdk.databinding.FragmentDashboardBannerBinding
import com.trolla.healthsdk.feature_dashboard.data.DashboardResponse

class DashboardCategoriesFragment: Fragment() {
    lateinit var binding: FragmentDashboardBannerBinding
    var bannersList = ArrayList<DashboardResponse.HomePagePositionsListItem.BannerData>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_dashboard_banner,
            container,
            false
        )

        return binding.root
    }
}