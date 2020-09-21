package com.xun.sodaability.webview.client

import android.graphics.Bitmap
import android.net.http.SslError
import android.os.Build
import android.webkit.*
import androidx.annotation.RequiresApi
import com.xun.sodaability.webview.IWebClientListener
import com.xun.sodalibrary.log.SodaLog

/**
 *  author: xun.wang on 2019/4/4
 **/
class CommWebClient : WebViewClient() {
    private var webClientListener: IWebClientListener? = null

    override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
        super.onPageStarted(view, url, favicon)
        SodaLog.d("CommWebClient onPageStarted url:$url")
        webClientListener?.onPageStarted(url)
    }

    override fun onPageFinished(view: WebView?, url: String?) {
        super.onPageFinished(view, url)
        SodaLog.d("CommWebClient onPageFinished url:$url")
        webClientListener?.onPageFinished(url)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onReceivedError(
        view: WebView?,
        request: WebResourceRequest?,
        error: WebResourceError?
    ) {
        super.onReceivedError(view, request, error)
        SodaLog.d("CommWebClient onReceivedError M " + error?.description.toString())
        webClientListener?.onReceivedError(error?.description.toString())
    }

    override fun onReceivedError(
        view: WebView?,
        errorCode: Int,
        description: String?,
        failingUrl: String?
    ) {
        super.onReceivedError(view, errorCode, description, failingUrl)
        SodaLog.d("CommWebClient onReceivedError")
        webClientListener?.onReceivedError(description)
    }

    override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
        SodaLog.d("CommWebClient shouldOverrideUrlLoading url:$url")
        if (webClientListener?.shouldOverrideUrlLoading(url) == true) {
            SodaLog.d("CommWebClient shouldOverrideUrlLoading true")
            return true
        }
        view?.loadUrl(url)
        return true
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
        SodaLog.d("CommWebClient shouldOverrideUrlLoading L url:" + request?.url)
        if (webClientListener?.shouldOverrideUrlLoading(request?.url?.toString()) == true) {
            SodaLog.d("CommWebClient shouldOverrideUrlLoading L true")
            return true
        }
        view?.loadUrl(request?.url?.toString())
        return true
    }

    override fun onReceivedSslError(view: WebView?, handler: SslErrorHandler?, error: SslError?) {
        SodaLog.d("CommWebClient onReceivedSslError")
        super.onReceivedSslError(view, handler, error)
    }

    fun setWebClientListener(webClientListener: IWebClientListener) {
        this.webClientListener = webClientListener
    }
}