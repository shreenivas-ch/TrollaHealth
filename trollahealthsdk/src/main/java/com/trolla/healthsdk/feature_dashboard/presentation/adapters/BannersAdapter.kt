package com.trolla.healthsdk.feature_dashboard.presentation.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import com.bumptech.glide.Glide
import com.makeramen.roundedimageview.RoundedImageView
import com.trolla.healthsdk.R
import com.trolla.healthsdk.feature_dashboard.data.DashboardResponse

class BannersAdapter(
    val mContext: Context,
    val bannerArray: ArrayList<DashboardResponse.HomePagePositionsListItem.BannerData>,
    val apiDefinition: DashboardResponse.HomePagePositionsListItem.APIDefinition?,
    val bannerClickedListner: OnBannerClickedListner
) : PagerAdapter() {

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object`
    }

    override fun instantiateItem(collection: ViewGroup, position: Int): Any {
        val inflater = LayoutInflater.from(mContext)
        val layout =
            inflater.inflate(R.layout.item_banner, collection, false) as ViewGroup
        val imageBanner = layout.findViewById(R.id.imvBanner) as RoundedImageView

        mContext.let {
            Glide.with(it).load(bannerArray?.get(position).banner_url).into(imageBanner)
        }

        imageBanner.setOnClickListener {
            bannerClickedListner.onBannerClicked(position)
        }

        collection.addView(layout)
        return layout
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View?)
    }

    override fun getCount(): Int = bannerArray?.size

    interface OnBannerClickedListner {
        fun onBannerClicked(position: Int)
    }
}