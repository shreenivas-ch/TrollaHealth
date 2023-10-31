package com.trolla.health

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.trolla.health.AppConstants.COSHOP_APPID
import com.trolla.healthsdk.TrollaHealthManager

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        TrollaHealthManager.Builder().appid(COSHOP_APPID).context(this).application(application)
            .build()
            .launch()

        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                return@OnCompleteListener
            }

            val token = task.result

            TrollaHealthManager.Builder().appid(COSHOP_APPID).context(this).application(application)
                .build().updateFCMToken(token)
        })

        finish()

        /*findViewById<TextView>(R.id.txtLaunch).setOnClickListener {
            TrollaHealthManager.Builder().appid("adfad").context(this).application(application).build()
                .launch()
        }*/
    }
}