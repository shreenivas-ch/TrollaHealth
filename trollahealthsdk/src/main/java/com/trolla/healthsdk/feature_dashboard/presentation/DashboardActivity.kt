package com.trolla.healthsdk.feature_dashboard.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import com.trolla.healthsdk.R
import com.trolla.healthsdk.utils.setVisibilityOnBoolean

class DashboardActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)
        addOrReplaceFragment(HomeFragment())
    }

    fun addOrReplaceFragment(fragment: Fragment, isAdd: Boolean = false) {
        var transaction = supportFragmentManager.beginTransaction()
        if (isAdd) {
            transaction.add(R.id.contentContainer, fragment)
        } else {
            transaction.replace(R.id.contentContainer, fragment)
        }
        transaction.commit()
    }

    fun showHideProgressBar(isShow: Boolean = false) {
        findViewById<ProgressBar>(R.id.progressBar).setVisibilityOnBoolean(isShow, true)
    }
}