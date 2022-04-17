package com.trolla.healthsdk.feature_onboarding.presentation

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.viewpager.widget.PagerAdapter
import com.bumptech.glide.Glide
import com.trolla.healthsdk.R

class OnBoardingTextsAdapter(
    val mContext: Context
) : PagerAdapter() {

    var titleArray = arrayListOf(
        "Medicines at Your Doorstep",
        "Money Saving Deals",
        "Get Fast Delivery 24 x 7"
    )
    var subtitleArray = arrayListOf(
        "Order quality medicines or products online and get them safely delivered to you",
        "Get offers, coupons, deals and points the more you shop.",
        "Our stores are open 24x7 & are at your service at all times"
    )

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object`
    }

    override fun instantiateItem(collection: ViewGroup, position: Int): Any {
        val inflater = LayoutInflater.from(mContext)
        val layout =
            inflater.inflate(R.layout.layout_onboarding_texts, collection, false) as ViewGroup
        val txtTitle = layout.findViewById(R.id.txtTitle) as TextView
        val txtSubtitle = layout.findViewById(R.id.txtSubtitle) as TextView

        mContext.let {
            txtTitle.text = titleArray?.get(position)
            txtSubtitle.text = subtitleArray?.get(position)
        }

        collection.addView(layout)
        return layout
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View?)
    }

    override fun getCount(): Int = titleArray?.size
}