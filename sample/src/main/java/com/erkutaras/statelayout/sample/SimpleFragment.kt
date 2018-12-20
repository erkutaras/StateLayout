package com.erkutaras.statelayout.sample

import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.erkutaras.statelayout.StateLayout
import kotlinx.android.synthetic.main.activity_state_layout_sample.*

class SimpleFragment : Fragment(), StateLayout.OnStateLayoutListener {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.activity_state_layout_sample, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        webView.webViewClient = SampleWebViewClient(stateLayout, this)
        webView.loadUrl(WEB_URL)
    }

    override fun onStateLayoutInfoButtonClick() {
        webView.loadUrl(WEB_URL)
        Toast.makeText(context, "Refreshing Page...", Toast.LENGTH_SHORT).show()
    }

    fun webViewCanGoBack() = webView.canGoBack()

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

    companion object {
        @JvmStatic
        fun newInstance() = SimpleFragment()

    }
}
