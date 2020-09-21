package com.xun.sodaability.webview

import android.net.Uri
import android.webkit.ValueCallback

/**
 *  author: xun.wang on 2019/4/4
 **/
interface IWebClientListener{
    fun onReceivedError(msg: String?)

    fun onPageStarted(url: String?)

    fun onPageFinished(url: String?)

    fun onProgressChanged(newProgress: Int)

    fun shouldOverrideUrlLoading(url: String?) : Boolean

    fun onReceivedTitle(title: String?)

    fun onShowFileChooser(
        filePathCallback: ValueCallback<Array<Uri>>?
    ): Boolean
}