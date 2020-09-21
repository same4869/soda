package com.xun.sodaability.webview

import android.text.TextUtils
import android.webkit.JavascriptInterface
import com.google.gson.Gson
import com.xun.sodaability.webview.bean.JSJsonParamsBean
import com.xun.sodalibrary.log.SodaLog

/**
 * @Description:
 * @Author:         xwang
 * @CreateDate:     2020/9/21
 */

class CommJSInterface {

    companion object {
        const val JS_BRIDGE_PUSHPAGE = "pushPage"
        const val JS_BRIDGE_CLOSEPAGE = "closePage"
    }

    private var mH5NewCallbackInterface: H5NewCallbackInterface? = null

    fun setH5NewCallbackInterface(newH5CallbackInterface: H5NewCallbackInterface?) {
        mH5NewCallbackInterface = newH5CallbackInterface
    }

    interface H5NewCallbackInterface {
        fun onH5CallBack(jSJsonParamsBean: JSJsonParamsBean)
    }

    @JavascriptInterface
    fun postMessage(jsonMsg: String) {
        SodaLog.d("kkkkkkkk", "postMessage jsonMsg:$jsonMsg")
        if (TextUtils.isEmpty(jsonMsg)) {
            return
        }
        val jSJsonParamsBean = Gson().fromJson(jsonMsg, JSJsonParamsBean::class.java)
        mH5NewCallbackInterface?.onH5CallBack(jSJsonParamsBean)
    }
}