package com.trolla.healthsdk.ui_utils

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import com.trolla.healthsdk.databinding.ActivityWebviewBinding
import com.trolla.healthsdk.utils.setVisibilityOnBoolean
import org.koin.java.KoinJavaComponent

class WebviewActivity : AppCompatActivity() {

    val webviewViewModel: WebviewViewModel by KoinJavaComponent.inject(
        WebviewViewModel::class.java
    )

    private lateinit var binding: ActivityWebviewBinding

    val title by lazy {
        intent?.let {
            it.getStringExtra("title")
        }
    }

    val url by lazy {
        intent?.let {
            it.getStringExtra("url")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityWebviewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.lifecycleOwner = this
        binding.viewModel = webviewViewModel

        webviewViewModel.headerTitle.value = title
        webviewViewModel.headerBottomLine.value = 1
        webviewViewModel.headerBackButton.value = 1

        url?.let {
            loadURL(it)
        }

        webviewViewModel.progressStatus.observe(this) {
            binding.progressBar.setVisibilityOnBoolean(it, true)
        }

        binding.commonHeader.imgBack.setOnClickListener {
            finish()
        }

    }

    private fun loadURL(url: String) {
        binding.webView.webViewClient = MyBrowser()
        val webSettings = binding.webView.settings
        binding.webView.clearCache(true)
        webSettings.javaScriptEnabled = true
        webSettings.allowFileAccess = true
        webSettings.domStorageEnabled = true
        webSettings.cacheMode = WebSettings.LOAD_NO_CACHE

        binding.webView.loadUrl(url)
    }

    private inner class MyBrowser : WebViewClient() {

        override fun shouldOverrideUrlLoading(
            view: WebView?,
            request: WebResourceRequest?
        ): Boolean {
            url?.let {
                view?.loadUrl(it)
            }
            return false

        }


        override fun onPageFinished(view: WebView, url: String) {
            super.onPageFinished(view, url)

        }
    }
}