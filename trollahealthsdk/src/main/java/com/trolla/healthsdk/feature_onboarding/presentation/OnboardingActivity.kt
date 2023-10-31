package com.trolla.healthsdk.feature_onboarding.presentation

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.ContextCompat
import androidx.viewpager.widget.ViewPager
import com.google.android.material.snackbar.Snackbar
import com.tbuonomo.viewpagerdotsindicator.WormDotsIndicator
import com.trolla.healthsdk.R
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

        findViewById<TextView>(R.id.txtLogin)?.setOnClickListener {
            var loginIntent = Intent(this, DashboardActivity::class.java)
            loginIntent.putExtra("action", DashboardActivity.DASHBOARD_ACTION_LOGIN)
            startActivity(loginIntent)
            finish()
            finish()
        }

        findViewById<TextView>(R.id.txtSignup)?.setOnClickListener {
            var loginIntent = Intent(this, DashboardActivity::class.java)
            loginIntent.putExtra("action", DashboardActivity.DASHBOARD_ACTION_LOGIN)
            startActivity(loginIntent)
            finish()
        }

        var requestPermissionLauncher: ActivityResultLauncher<String> =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) {
                if (it) {

                } else {
                    Snackbar.make(
                        findViewById<View>(android.R.id.content).rootView,
                        "Please grant Notification permission from App Settings",
                        Snackbar.LENGTH_LONG
                    ).show()
                }
            }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS,
                ) == PackageManager.PERMISSION_GRANTED
            ) {
            } else {
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }

    private fun changeSlidesText(position: Int) {
        findViewById<TextView>(R.id.txtTitle).text = getString(slidesTitlesArray[position])
        findViewById<TextView>(R.id.txtSubtitle).text =
            getString(slidesSubTitlesArray[position])
    }
}