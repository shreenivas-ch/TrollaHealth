package com.trolla.healthsdk.feature_dashboard.presentation.dashboard_childlayouts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.trolla.healthsdk.R
import com.trolla.healthsdk.databinding.FragmentDashboardBannerBinding
import com.trolla.healthsdk.feature_dashboard.data.DashboardResponse
import com.trolla.healthsdk.feature_dashboard.data.DashboardResponse.HomePagePositionsListItem.BannerData
import com.trolla.healthsdk.feature_dashboard.presentation.adapters.BannersAdapter
import com.trolla.healthsdk.utils.TrollaHealthUtility

class DashboardBannerFragment : Fragment() {

    lateinit var binding: FragmentDashboardBannerBinding
    var bannersList = ArrayList<BannerData>()

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sliderAdapter = BannersAdapter(
            requireActivity(),
            bannersList
        )

        var pager = binding.bannerViewPager?.apply {
            adapter = sliderAdapter
            startAutoScroll()
            interval = 10000
            isCycle = true
            clipToPadding = false
        }

        pager.setPaddingRelative(
            TrollaHealthUtility.getMarginInDp(25f, requireActivity()),
            0,
            TrollaHealthUtility.getMarginInDp(25f, requireActivity()),
            0
        )

        binding.bannerDotsIndicator?.setViewPager(pager as ViewPager)

    }

}