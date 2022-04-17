package com.trolla.healthsdk.feature_onboarding.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.viewpager.widget.ViewPager
import com.tbuonomo.viewpagerdotsindicator.WormDotsIndicator
import com.trolla.healthsdk.R
import com.trolla.healthsdk.ui_utils.AutoScrollViewPager
import com.trolla.healthsdk.utils.LogUtil

class OnboardingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_onboarding)

        val sliderAdapter = OnBoardingSlidesAdapter(
            this
        )

        val sliderTextsAdapter = OnBoardingTextsAdapter(
            this
        )

        var pager = findViewById<AutoScrollViewPager>(R.id.viewPager)?.apply {
            adapter = sliderAdapter
            startAutoScroll()
            interval = 3000
            isCycle = true
            clipToPadding = false
        }

        findViewById<AutoScrollViewPager>(R.id.viewPagerTexts)?.apply {
            adapter = sliderTextsAdapter
            startAutoScroll()
            interval = 3000
            isCycle = true
            clipToPadding = false
        }

        findViewById<WormDotsIndicator>(R.id.dots_indicator)?.setViewPager(pager as ViewPager)

    }
}