package com.xun.sodaability.webview.client

import android.graphics.Bitmap
import android.net.Uri
import android.webkit.ValueCallback
import android.webkit.WebChromeClient
import android.webkit.WebView
import com.xun.sodaability.webview.IWebClientListener
import com.xun.sodalibrary.log.SodaLog

/**
 *  author: xun.wang on 2019/4/4
 **/
class CommWebChromeClient : WebChromeClient() {
    private var webClientListener: IWebClientListener? = null

    override fun onProgressChanged(view: WebView?, newProgress: Int) {
        super.onProgressChanged(view, newProgress)
        SodaLog.d("CommWebChromeClient onProgressChanged")
        webClientListener?.onProgressChanged(newProgress)
    }

    fun setWebClientListener(webClientListener: IWebClientListener) {
        this.webClientListener = webClientListener
    }

    override fun onReceivedTitle(view: WebView?, title: String?) {
        super.onReceivedTitle(view, title)
        webClientListener?.onReceivedTitle(title)
    }

    override fun onShowFileChooser(
        webView: WebView?,
        filePathCallback: ValueCallback<Array<Uri>>?,
        fileChooserParams: FileChooserParams?
    ): Boolean {
        return webClientListener?.onShowFileChooser(filePathCallback)
            ?: true
    }

    override fun getDefaultVideoPoster(): Bitmap? {
        return if (super.getDefaultVideoPoster() == null) {
            Bitmap.createBitmap(100, 100, Bitmap.Config.RGB_565)
        } else {
            super.getDefaultVideoPoster()
        }
    }

}