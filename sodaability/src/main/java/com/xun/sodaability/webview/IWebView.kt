package com.xun.sodaability.webview

import android.view.View

interface IWebView {
    fun destroy()

    fun setWebClientListener(listener : IWebClientListener)

    fun addJavascriptInterface(obj : Any, interfaceName : String)

    fun loadUrl(url : String)

    fun canGoBack() : Boolean

    fun goBack()

    fun reload()

    fun evaluateJavascript(js: String, callback: (String) -> Unit)

    fun getHost() : View
}