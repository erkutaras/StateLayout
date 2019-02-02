package com.erkutaras.statelayout.sample

import android.graphics.Bitmap
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.webkit.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.erkutaras.statelayout.StateLayout
import kotlinx.android.synthetic.main.activity_custom_sample.*
import kotlinx.android.synthetic.main.layout_custom_info.*
import kotlinx.android.synthetic.main.layout_custom_loading.*

/**
 * Created by erkutaras on 21.12.2018.
 */
private const val WEB_URL = "https://medium.com/@erkutaras"

class CustomSampleActivity : SampleBaseActivity() {

    private var hasError: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_custom_sample)

        webView.webViewClient = object : WebViewClient() {

            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                hasError = false
                if (url.equals(WEB_URL)) stateLayout.loading()
                else stateLayout.loadingWithContent()
            }

            override fun onReceivedError(view: WebView?, request: WebResourceRequest?, error: WebResourceError?) {
                super.onReceivedError(view, request, error)
                hasError = true
                showInfoState()
            }
        }
        webView.webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                super.onProgressChanged(view, newProgress)
                contentLoadingProgressBar.progress = newProgress
                textView_progress.text = "$newProgress%"

                if (!hasError && newProgress == 100) stateLayout.content()
                if (hasError && newProgress == 100) showInfoState()
            }
        }
        loadUrl()
    }

    override fun getMenuResId(): Int = R.menu.menu_custom

    private fun showInfoState() {
        stateLayout.info()
        button_refresh.setOnClickListener { loadUrl() }
        button_close.setOnClickListener { finish() }
    }

    private fun loadUrl() {
        webView.loadUrl(WEB_URL)
    }

    override fun onBackPressed() {
        if (webView.canGoBack()) webView.goBack()
        else super.onBackPressed()
    }
}