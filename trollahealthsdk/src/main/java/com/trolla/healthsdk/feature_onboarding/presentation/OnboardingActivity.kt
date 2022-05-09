package com.trolla.healthsdk.feature_onboarding.presentation

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.viewpager.widget.ViewPager
import com.tbuonomo.viewpagerdotsindicator.WormDotsIndicator
import com.trolla.healthsdk.R
import com.trolla.healthsdk.feature_auth.presentation.AuthenticationActivity
import com.trolla.healthsdk.feature_dashboard.presentation.DashboardActivity
import com.trolla.healthsdk.ui_utils.AutoScrollViewPager
import com.trolla.healthsdk.utils.LogUtil

class OnboardingActivity : AppCompatActivity() {

    var slidesTitlesArray = arrayListOf(
        R.string.onboarding_slide1_title,
        R.string.onboarding_slide2_title,
        R.string.onboarding_slide3_title
    )
    var slidesSubTitlesArray = arrayListOf(
        R.string.onboarding_slide1_subtitle,
        R.string.onboarding_slide2_subtitle,
        R.string.onboarding_slide3_subtitle
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_onboarding)

        val sliderAdapter = OnBoardingSlidesAdapter(
            this
        )

        var pager = findViewById<AutoScrollViewPager>(R.id.viewPager)?.apply {
            adapter = sliderAdapter
            startAutoScroll()
            interval = 3000
            isCycle = true
            clipToPadding = false
            addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
                override fun onPageScrolled(
                    position: Int,
                    positionOffset: Float,
                    positionOffsetPixels: Int
                ) {

                }

                override fun onPageSelected(position: Int) {
                    LogUtil.printObject(position)
                    changeSlidesText(position)
                }

                override fun onPageScrollStateChanged(state: Int) {

                }

            })
        }

        findViewById<WormDotsIndicator>(R.id.dots_indicator)?.setViewPager(pager as ViewPager)

        findViewById<AppCompatImageView>(R.id.imgBack)?.setOnClickListener {
            finish()
        }

        findViewById<TextView>(R.id.imgSkip)?.setOnClickListener {
            startActivity(Intent(this, DashboardActivity::class.java))
            finish()
        }

        findViewById<TextView>(R.id.txtLogin)?.setOnClickListener {
            startActivity(Intent(this, AuthenticationActivity::class.java))
            finish()
        }

        findViewById<TextView>(R.id.txtSignup)?.setOnClickListener {
            startActivity(Intent(this, AuthenticationActivity::class.java))
            finish()
        }
    }

    private fun changeSlidesText(position: Int) {
        findViewById<TextView>(R.id.txtTitle).text = getString(slidesTitlesArray[position])
        findViewById<TextView>(R.id.txtSubtitle).text =
            getString(slidesSubTitlesArray[position])
    }
}