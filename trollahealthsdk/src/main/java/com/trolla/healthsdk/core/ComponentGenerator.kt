package com.trolla.healthsdk.core

import androidx.fragment.app.Fragment
import com.trolla.healthsdk.feature_dashboard.data.DashboardComponentModel

object ComponentGenerator {

    private const val TYPE_DASHBOARD_BANNER="dashboardBanner"
    private const val TYPE_DASHBOARD_CATEGORIES="dashboardCategories"
    private const val TYPE_DASHBOARD_FEATURED_BRANDS="dashboardFeaturedBrands"
    private const val TYPE_DASHBOARD_TRENDING_PRODUCTS="dashboardTrendingProducts"
    private const val TYPE_DASHBOARD_RECOMMENDED_PRODUCTS="dashboardRecommendedProducts"
    private const val TYPE_DASHBOARD_NEW_ARRIVALS="dashboardRecommendedProducts"

    fun <T> getComponentObject(dashboardComponentModel: DashboardComponentModel<T>): Fragment? {
    /*    if (dashboardComponentModel.template == TYPE_DASHBOARD_BANNER) {
            return createBannerFragment(dashboardComponentModel.data)
        } else if (data.template == searchKeyForCategory) {
            return createCategoryObject(data)
        }

    */
    return null
    }

}