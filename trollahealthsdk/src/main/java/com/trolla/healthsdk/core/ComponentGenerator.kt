package com.trolla.healthsdk.core

import androidx.fragment.app.Fragment
import com.trolla.healthsdk.feature_dashboard.data.DashboardComponentModel
import com.trolla.healthsdk.feature_dashboard.data.DashboardResponse.HomePagePositionsListItem.*
import com.trolla.healthsdk.feature_dashboard.presentation.dashboard_childlayouts.DashboardBannerFragment

object ComponentGenerator {

    private const val TYPE_DASHBOARD_BANNER = "dashboardBanner"
    private const val TYPE_DASHBOARD_CATEGORIES = "dashboardCategories"
    private const val TYPE_DASHBOARD_FEATURED_BRANDS = "dashboardFeaturedBrands"
    private const val TYPE_DASHBOARD_TRENDING_PRODUCTS = "dashboardTrendingProducts"
    private const val TYPE_DASHBOARD_RECOMMENDED_PRODUCTS = "dashboardRecommendedProducts"
    private const val TYPE_DASHBOARD_NEW_ARRIVALS = "dashboardRecommendedProducts"

    fun <T> getComponentObject(dashboardComponentModel: DashboardComponentModel<T>): Fragment? {
        if (dashboardComponentModel.template == TYPE_DASHBOARD_BANNER) {
            return createBannerFragment(dashboardComponentModel.data as ArrayList<BannerData>)
        } else if (dashboardComponentModel.template == TYPE_DASHBOARD_CATEGORIES) {
            return null
        }

        return null
    }

    fun createBannerFragment(banners: ArrayList<BannerData>): Fragment {
        var bannerFragment = DashboardBannerFragment()
        bannerFragment.bannersList = banners
        return bannerFragment
    }

}