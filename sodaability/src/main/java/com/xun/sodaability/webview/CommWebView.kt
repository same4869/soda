package com.xun.sodaability.webview

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.ApplicationInfo
import android.net.Uri
import android.os.Build
import android.util.AttributeSet
import android.view.View
import android.webkit.CookieManager
import android.webkit.ValueCallback
import android.webkit.WebSettings
import android.webkit.WebView
import com.xun.sodaability.webview.client.CommWebChromeClient
import com.xun.sodaability.webview.client.CommWebClient
import com.xun.sodalibrary.log.SodaLog

/**
 * @Description:
 * @Author:         xwang
 * @CreateDate:     2020/9/21
 */

class CommWebView : WebView, IWebView {
    private var webClientListener: IWebClientListener? = null

    constructor(context: Context) : super(context) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init(context)
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun init(context: Context) {
        isVerticalScrollBarEnabled = false
        isHorizontalScrollBarEnabled = false
        settings.javaScriptEnabled = true
        settings.domStorageEnabled = true
        settings.setAppCacheEnabled(true)
        settings.cacheMode = WebSettings.LOAD_DEFAULT
        settings.javaScriptCanOpenWindowsAutomatically = true
        settings.setAppCachePath(WebConstants.DATA_DATA + context.packageName + WebConstants.CACHE)
        settings.setAppCacheMaxSize(WebConstants.APP_CACHE_MAX_SIZE.toLong())
        val databasePath = context.getDir(WebConstants.DATABASES_DIRS, Context.MODE_PRIVATE).path
        settings.databasePath = databasePath
        settings.builtInZoomControls = false
        settings.databaseEnabled = true
        settings.pluginState = WebSettings.PluginState.ON
        settings.setRenderPriority(WebSettings.RenderPriority.HIGH)
        settings.defaultTextEncodingName = "UTF-8"

        settings.loadWithOverviewMode = true
        settings.useWideViewPort = true

        val ua = settings.userAgentString//原来获取的UA
        //settings.userAgentString = ua + " miHoYoBBS/" + getAppVersionCode(context)

        //加入允许 JS 跨域访问 cookie
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            settings.allowFileAccess = true
            settings.allowContentAccess = true
            //跨文件域访问安全性问题，所以禁止----》191008,富文本需求，需要开启
            settings.allowUniversalAccessFromFileURLs = true
            settings.allowFileAccessFromFileURLs = true
            CookieManager.getInstance().setAcceptThirdPartyCookies(this, true)
        }

        //set webview debugable only app debuggable
        if (0 != context.applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE) {
            setWebContentsDebuggingEnabled(true)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            settings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
        }

        val webClientListener2 = object : IWebClientListener {
            override fun onShowFileChooser(
                filePathCallback: ValueCallback<Array<Uri>>?
            ): Boolean {
                return webClientListener?.onShowFileChooser(
                    filePathCallback
                ) ?: true
            }

            override fun onReceivedTitle(title: String?) {
                webClientListener?.onReceivedTitle(title)
            }

            override fun shouldOverrideUrlLoading(url: String?): Boolean {
                SodaLog.d("shouldOverrideUrlLoading url -> $url")
                return webClientListener?.shouldOverrideUrlLoading(url) ?: true
            }

            override fun onReceivedError(msg: String?) {
                webClientListener?.onReceivedError(msg)
            }

            override fun onPageStarted(url: String?) {
                webClientListener?.onPageStarted(url)
            }

            override fun onPageFinished(url: String?) {
                webClientListener?.onPageFinished(url)
            }

            override fun onProgressChanged(newProgress: Int) {
                webClientListener?.onProgressChanged(newProgress)
            }
        }

        val commWebClient = CommWebClient()
        commWebClient.setWebClientListener(webClientListener2)
        webViewClient = commWebClient

        val commWebChromeClient = CommWebChromeClient()
        commWebChromeClient.setWebClientListener(webClientListener2)
        webChromeClient = commWebChromeClient

        //2014年香港理工大学的研究人员Daoyuan Wu和Rocky Chang发现了两个新的攻击向量存在于android/webkit/AccessibilityInjector.java中，
        // 分别是"accessibility" 和"accessibilityTraversal" ，调用了此组件的应用在开启辅助功能选项中第三方服务的安卓系统中会造成远程代码执行漏洞。
        // 该漏洞公布于CVE-2014-7224, 此漏洞原理与searchBoxJavaBridge_接口远程代码执行相似，均为未移除不安全的默认接口，建议开发者通过以下方式移除该JavaScript接口
        removeJavascriptInterface("accessibility")
        removeJavascriptInterface("accessibilityTraversal")
    }

    override fun setWebClientListener(listener: IWebClientListener) {
        webClientListener = listener
    }

    override fun evaluateJavascript(js: String, callback: (String) -> Unit) {
        evaluateJavascript(js, ValueCallback(callback))
    }

    override fun getHost(): View {
        return this
    }
}