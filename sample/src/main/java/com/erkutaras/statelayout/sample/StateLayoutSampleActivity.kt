package com.erkutaras.statelayout.sample

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import com.erkutaras.statelayout.StateLayout
import kotlinx.android.synthetic.main.activity_state_layout_sample.*

private const val WEB_URL = "http://www.erkutaras.com/"

class StateLayoutSampleActivity : AppCompatActivity(), StateLayout.OnStateLayoutListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_state_layout_sample)

        webView.webViewClient = SampleWebViewClient(stateLayout, this)
        webView.loadUrl(WEB_URL)
    }

    override fun onStateLayoutInfoButtonClick() {
        webView.loadUrl(WEB_URL)
        Toast.makeText(this, "Refreshing Page...", Toast.LENGTH_SHORT).show()
    }

    override fun onBackPressed() {
        if (webView.canGoBack()) webView.goBack()
        else super.onBackPressed()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_sample, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_custom -> {
                startActivity(Intent(this, CustomSampleActivity::class.java))
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private class SampleWebViewClient(val stateLayout: StateLayout,
                                      val onStateLayoutListener: StateLayout.OnStateLayoutListener)
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
            stateLayout.infoImage(R.drawable.ic_android_black_64dp)
                .infoTitle("Ooops.... :(")
                .infoMessage("Unexpected error occurred. Please refresh the page!")
                .infoButtonText("Refresh")
                .infoButtonListener {
                    onStateLayoutListener.onStateLayoutInfoButtonClick()
                }
        }

    }
}
