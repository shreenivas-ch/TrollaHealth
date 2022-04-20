package com.trolla.healthsdk.feature_auth.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.trolla.healthsdk.R

class AuthenticationActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_authentication)

        addOrReplaceFragment(LoginEmailFragment())
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
}