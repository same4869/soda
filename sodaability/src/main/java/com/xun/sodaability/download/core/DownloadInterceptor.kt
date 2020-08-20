package com.xun.sodaability.download.core

import okhttp3.Interceptor
import okhttp3.Response

/**
 * @Description:
 * @Author:         xwang
 * @CreateDate:     2019-10-14
 */

class DownloadInterceptor : Interceptor {
    private var downloadListener: DownloadListener? = null
    private var downloadResponseBody: DownloadResponseBody? = null
    private var mIsWithPoint: Boolean = false

    constructor(downloadListener: DownloadListener) {
        this.downloadListener = downloadListener
    }

    fun setIsWithPoint(isWithPoint: Boolean) {
        mIsWithPoint = isWithPoint
    }

    fun getIsWithPoint(): Boolean {
        return mIsWithPoint
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val response = chain.proceed(chain.request())
        if (response.body == null || downloadListener == null) {
            return response
        }
        downloadResponseBody =
            DownloadResponseBody(
                response.body!!,
                downloadListener!!,
                false
            )

        downloadResponseBody?.setIsWithPoint(mIsWithPoint)
        return response.newBuilder()
            .body(downloadResponseBody).build()
    }

}