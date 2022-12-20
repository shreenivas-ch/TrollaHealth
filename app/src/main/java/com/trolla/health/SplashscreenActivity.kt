package com.trolla.health

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class SplashscreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splashscreen)

        getVersion()

        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this@SplashscreenActivity, MainActivity::class.java))
            finish()
        }, 2000)
    }

    fun getVersion() {
        var flavourDetails = if (BuildConfig.FLAVOR == "live") {
            if (BuildConfig.DEBUG) {
                " Live"
            } else {
                ""
            }
        } else {
            " (Staging)"
        }

        findViewById<TextView>(R.id.txtVersionDetails).text =
            "Version " + BuildConfig.VERSION_NAME + flavourDetails
    }
}