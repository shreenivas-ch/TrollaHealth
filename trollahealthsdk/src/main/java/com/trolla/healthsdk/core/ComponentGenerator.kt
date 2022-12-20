package com.trolla.healthsdk.core

import androidx.fragment.app.Fragment
import com.trolla.healthsdk.feature_dashboard.data.DashboardComponentModel
import com.trolla.healthsdk.feature_dashboard.data.DashboardResponse.DashboardProduct
import com.trolla.healthsdk.feature_dashboard.data.DashboardResponse.HomePagePositionsListItem.APIDefinition
import com.trolla.healthsdk.feature_dashboard.data.DashboardResponse.HomePagePositionsListItem.BannerData
import com.trolla.healthsdk.feature_dashboard.presentation.dashboard_childlayouts.*

object ComponentGenerator {

    const val TYPE_DASHBOARD_BANNER = "dashboardBanner"
    const val TYPE_DASHBOARD_CATEGORIES = "dashboardCategories"
    const val TYPE_DASHBOARD_FEATURED_BRANDS = "dashboardFeaturedBrands"
    const val TYPE_DASHBOARD_TRENDING_PRODUCTS = "dashboardTrendingProducts"
    const val TYPE_DASHBOARD_RECOMMENDED_PRODUCTS = "dashboardRecommendedProducts"
    const val TYPE_DASHBOARD_NEW_ARRIVALS = "dashboardNewArrivalProducts"

    fun <T> getComponentObject(dashboardComponentModel: DashboardComponentModel<T>): Fragment? {
        when (dashboardComponentModel.template) {
            TYPE_DASHBOARD_BANNER -> {
                return createBannerFragment(
                    dashboardComponentModel.data as ArrayList<BannerData>,
                    dashboardComponentModel.apiDefinition
                )
            }
            TYPE_DASHBOARD_CATEGORIES -> {
                return createCategoriesFragment(
                    dashboardComponentModel.data as ArrayList<BannerData>,
                    dashboardComponentModel.apiDefinition
                )
            }
            TYPE_DASHBOARD_FEATURED_BRANDS -> {
                return createFeaturedBrandsFragment(
                    dashboardComponentModel.data as ArrayList<BannerData>,
                    dashboardComponentModel.apiDefinition
                )
            }
            TYPE_DASHBOARD_RECOMMENDED_PRODUCTS -> {
                return createRecommendedProductsFragment(dashboardComponentModel.data as ArrayList<DashboardProduct>)
            }
            TYPE_DASHBOARD_TRENDING_PRODUCTS -> {
                return createTrendingProductsFragment(dashboardComponentModel.data as ArrayList<DashboardProduct>)
            }
            TYPE_DASHBOARD_NEW_ARRIVALS -> {
                return createNewArrivalsProductsFragment(dashboardComponentModel.data as ArrayList<DashboardProduct>)
            }
            else -> return null
        }

    }

    fun createBannerFragment(
        banners: ArrayList<BannerData>,
        apiDefinition: APIDefinition?
    ): Fragment {

        var bannerFragment = DashboardBannerFragment()
        bannerFragment.bannersList = banners
        bannerFragment.apiDefinition = apiDefinition
        return bannerFragment
    }

    fun createCategoriesFragment(
        banners: ArrayList<BannerData>,
        apiDefinition: APIDefinition?
    ): Fragment {
        var dashboardCategoriesFragment = DashboardCategoriesFragment()
        dashboardCategoriesFragment.bannersList = banners
        dashboardCategoriesFragment.bannersList.removeAll(listOf<BannerData?>(null))
        dashboardCategoriesFragment.apiDefinition = apiDefinition
        return dashboardCategoriesFragment
    }

    fun createFeaturedBrandsFragment(
        banners: ArrayList<BannerData>,
        apiDefinition: APIDefinition?
    ): Fragment {
        var dashboardFeaturedBrandsFragment = DashboardFeaturedBrandsFragment()
        dashboardFeaturedBrandsFragment.bannersList = banners
        dashboardFeaturedBrandsFragment.apiDefinition = apiDefinition
        return dashboardFeaturedBrandsFragment
    }

    fun createTrendingProductsFragment(banners: ArrayList<DashboardProduct>): Fragment {
        var trendingProductsFragment = DashboardTrendingProductsFragment()
        trendingProductsFragment.bannersList = banners
        return trendingProductsFragment
    }

    fun createRecommendedProductsFragment(banners: ArrayList<DashboardProduct>): Fragment {
        var dashboardRecommendedFragment = DashboardRecommendedFragment()
        dashboardRecommendedFragment.productsList = banners
        return dashboardRecommendedFragment
    }

    fun createNewArrivalsProductsFragment(banners: ArrayList<DashboardProduct>): Fragment {
        var dashboardNewArrivalsFragment = DashboardNewArrivalsFragment()
        dashboardNewArrivalsFragment.productsList = banners
        return dashboardNewArrivalsFragment
    }

}