package com.erkutaras.statelayout.sample

import android.graphics.Bitmap
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import com.erkutaras.statelayout.OnStateLayoutListener
import com.erkutaras.statelayout.StateLayout
import kotlinx.android.synthetic.main.activity_state_layout_sample.*

const val WEB_URL = "http://www.erkutaras.com/"

class StateLayoutSampleActivity : AppCompatActivity(), OnStateLayoutListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_state_layout_sample)

        webView.webViewClient = SampleWebViewClient(stateLayout, this)
        webView.loadUrl(WEB_URL)
    }

    override fun onErrorStateButtonClick() {
        webView.loadUrl(WEB_URL)
        Toast.makeText(this, "Refreshing Page...", Toast.LENGTH_SHORT).show()
    }

    override fun onBackPressed() {
        if (webView.canGoBack()) webView.goBack()
        else super.onBackPressed()
    }

    private class SampleWebViewClient(val stateLayout: StateLayout,
                                      val onStateLayoutListener: OnStateLayoutListener)
        : WebViewClient() {

        var hasError: Boolean = false

        override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
            super.onPageStarted(view, url, favicon)
            hasError = false
            if (url.equals(WEB_URL)) stateLayout.loading()
            else stateLayout.loadingWithContent()
        }

        override fun onPageFinished(view: WebView?, url: String?) {
            super.onPageFinished(view, url)
            if (hasError.not()) stateLayout.content()
        }

        override fun onReceivedError(view: WebView?, request: WebResourceRequest?, error: WebResourceError?) {
            super.onReceivedError(view, request, error)
            hasError = true
            stateLayout.error(R.drawable.ic_android_black_64dp)
                    .error("Ooops.... :(", "Unexpected error occurred. Please refresh the page!")
                    .error("Refresh", onStateLayoutListener)
        }

    }
}
