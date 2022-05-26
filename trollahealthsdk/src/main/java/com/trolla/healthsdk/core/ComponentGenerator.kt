package com.trolla.healthsdk.core

import androidx.fragment.app.Fragment
import com.trolla.healthsdk.feature_dashboard.data.DashboardComponentModel
import com.trolla.healthsdk.feature_dashboard.data.DashboardResponse
import com.trolla.healthsdk.feature_dashboard.data.DashboardResponse.DashboardProduct
import com.trolla.healthsdk.feature_dashboard.data.DashboardResponse.HomePagePositionsListItem.*
import com.trolla.healthsdk.feature_dashboard.presentation.dashboard_childlayouts.*

object ComponentGenerator {

    const val TYPE_DASHBOARD_BANNER = "dashboardBanner"
    const val TYPE_DASHBOARD_CATEGORIES = "dashboardCategories"
    const val TYPE_DASHBOARD_FEATURED_BRANDS = "dashboardFeaturedBrands"
    const val TYPE_DASHBOARD_TRENDING_PRODUCTS = "dashboardTrendingProducts"
    const val TYPE_DASHBOARD_RECOMMENDED_PRODUCTS = "dashboardRecommendedProducts"
    const val TYPE_DASHBOARD_NEW_ARRIVALS = "dashboardRecommendedProducts"

    fun <T> getComponentObject(dashboardComponentModel: DashboardComponentModel<T>): Fragment? {
        if (dashboardComponentModel.template == TYPE_DASHBOARD_BANNER) {
            return createBannerFragment(dashboardComponentModel.data as ArrayList<BannerData>)
        } else if (dashboardComponentModel.template == TYPE_DASHBOARD_CATEGORIES) {
            return createCategoriesFragment(dashboardComponentModel.data as ArrayList<BannerData>)
        } else if (dashboardComponentModel.template == TYPE_DASHBOARD_FEATURED_BRANDS) {
            return createFeaturedBrandsFragment(dashboardComponentModel.data as ArrayList<BannerData>)
        } else if (dashboardComponentModel.template == TYPE_DASHBOARD_RECOMMENDED_PRODUCTS) {
            return createRecommendedProductsFragment(dashboardComponentModel.data as ArrayList<DashboardProduct>)
        } else if (dashboardComponentModel.template == TYPE_DASHBOARD_TRENDING_PRODUCTS) {
            return createTrendingProductsFragment(dashboardComponentModel.data as ArrayList<DashboardProduct>)
        } else if (dashboardComponentModel.template == TYPE_DASHBOARD_NEW_ARRIVALS) {
            return createNewArrivalsProductsFragment(dashboardComponentModel.data as ArrayList<DashboardProduct>)
        }

        return null
    }

    fun createBannerFragment(banners: ArrayList<BannerData>): Fragment {
        var bannerFragment = DashboardBannerFragment()
        bannerFragment.bannersList = banners
        return bannerFragment
    }

    fun createCategoriesFragment(banners: ArrayList<BannerData>): Fragment {
        var dashboardCategoriesFragment = DashboardCategoriesFragment()
        dashboardCategoriesFragment.bannersList = banners
        return dashboardCategoriesFragment
    }

    fun createFeaturedBrandsFragment(banners: ArrayList<BannerData>): Fragment {
        var dashboardFeaturedBrandsFragment = DashboardFeaturedBrandsFragment()
        dashboardFeaturedBrandsFragment.bannersList = banners
        return dashboardFeaturedBrandsFragment
    }

    fun createTrendingProductsFragment(banners: ArrayList<DashboardProduct>): Fragment {
        var trendingProductsFragment = DashboardTrendingProductsFragment()
        trendingProductsFragment.bannersList = banners
        return trendingProductsFragment
    }

    fun createRecommendedProductsFragment(banners: ArrayList<DashboardProduct>): Fragment {
        var dashboardRecommendedFragment = DashboardRecommendedFragment()
        dashboardRecommendedFragment.bannersList = banners
        return dashboardRecommendedFragment
    }

    fun createNewArrivalsProductsFragment(banners: ArrayList<DashboardProduct>): Fragment {
        var dashboardNewArrivalsFragment = DashboardNewArrivalsFragment()
        dashboardNewArrivalsFragment.bannersList = banners
        return dashboardNewArrivalsFragment
    }

}