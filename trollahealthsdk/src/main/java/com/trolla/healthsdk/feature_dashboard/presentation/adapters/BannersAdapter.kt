package com.trolla.healthsdk.feature_dashboard.presentation.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.viewpager.widget.PagerAdapter
import com.bumptech.glide.Glide
import com.makeramen.roundedimageview.RoundedImageView
import com.trolla.healthsdk.R

class BannersAdapter(
    val mContext: Context
) : PagerAdapter() {

    var drawableArray = arrayListOf<String>(
        "https://d1j4fphs4leb29.cloudfront.net/placeholder_banner/placeholderMobileBanner_1653390946_491.jpg",
        "https://d1j4fphs4leb29.cloudfront.net/placeholder_banner/placeholderMobileBanner_1653390975_492.jpg",
        "https://d1j4fphs4leb29.cloudfront.net/placeholder_banner/placeholderMobileBanner_1653391007_510.jpg"
    )

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object`
    }

    override fun instantiateItem(collection: ViewGroup, position: Int): Any {
        val inflater = LayoutInflater.from(mContext)
        val layout =
            inflater.inflate(R.layout.item_banner, collection, false) as ViewGroup
        val imageBanner = layout.findViewById(R.id.imvBanner) as RoundedImageView

        mContext.let {
            Glide.with(it).load(drawableArray?.get(position)).into(imageBanner)
        }

        collection.addView(layout)
        return layout
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View?)
    }

    override fun getCount(): Int = drawableArray?.size
}