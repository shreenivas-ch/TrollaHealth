package com.trolla.healthsdk.feature_onboarding.presentation

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.viewpager.widget.PagerAdapter
import com.bumptech.glide.Glide
import com.trolla.healthsdk.R

class OnBoardingSlidesAdapter(
    val mContext: Context
) : PagerAdapter() {

    var drawableArray = arrayListOf<Int>(
        R.drawable.img_onboarding1,
        R.drawable.img_onboarding2,
        R.drawable.img_onboarding3
    )

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object`
    }

    override fun instantiateItem(collection: ViewGroup, position: Int): Any {
        val inflater = LayoutInflater.from(mContext)
        val layout =
            inflater.inflate(R.layout.layout_onboarding_slide, collection, false) as ViewGroup
        val imageBanner = layout.findViewById(R.id.imgSlide) as AppCompatImageView

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