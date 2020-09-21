package com.xun.sodaability.webview

import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.webkit.WebView
import androidx.appcompat.app.AppCompatActivity

/**
 * @Description:
 * @Author:         xwang
 * @CreateDate:     2020/9/21
 */

abstract class CommWebActivity : AppCompatActivity(), IWebClientListener,
    CommJSInterface.H5NewCallbackInterface {
    protected var url = ""
    protected val mCommWebOption: CommWebOption? by lazy {
        intent.getSerializableExtra(OPTION) as CommWebOption?
    }

    companion object {
        const val URL = "activity_web_view_url"
        const val OPTION = "activity_web_view_option"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            WebView.enableSlowWholeDocumentDraw()
        }
        super.onCreate(savedInstanceState)

        url = intent?.getStringExtra(URL) ?: ""

        setContentView(getLayoutId())
        if (TextUtils.isEmpty(url)) {
            finish()
            return
        }

        init()
    }

    abstract fun init()

    abstract fun getLayoutId(): Int

    override fun applyOverrideConfiguration(overrideConfiguration: Configuration?) {
        if (Build.VERSION.SDK_INT in 21..22) {
            return
        }
        super.applyOverrideConfiguration(overrideConfiguration)
    }
}