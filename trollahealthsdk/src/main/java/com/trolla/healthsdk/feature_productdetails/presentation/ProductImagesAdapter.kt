package com.trolla.healthsdk.feature_productdetails.presentation

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import com.bumptech.glide.Glide
import com.makeramen.roundedimageview.RoundedImageView
import com.trolla.healthsdk.R

class ProductImagesAdapter(
    val mContext: Context,
    val productImages: ArrayList<String>
) : PagerAdapter() {

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object`
    }

    override fun instantiateItem(collection: ViewGroup, position: Int): Any {
        val inflater = LayoutInflater.from(mContext)
        val layout =
            inflater.inflate(R.layout.item_product_image, collection, false) as ViewGroup
        val imageBanner = layout.findViewById(R.id.imvBanner) as RoundedImageView

        mContext.let {
            Glide.with(it).load(productImages?.get(position)).into(imageBanner)
        }

        imageBanner.setOnClickListener {
            var fullscreenIntent = Intent(mContext, FullscreenImageViewerActivity::class.java)
            fullscreenIntent.putExtra("imageurl", productImages[position])
            mContext.startActivity(fullscreenIntent)
        }

        collection.addView(layout)
        return layout
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View?)
    }

    override fun getCount(): Int = productImages?.size
}