package com.trolla.health

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.trolla.healthsdk.TrollaHealthManager

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        TrollaHealthManager.Builder().appid("adfad").context(this).build().launch()
    }
}