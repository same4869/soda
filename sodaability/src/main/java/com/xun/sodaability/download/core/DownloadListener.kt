package com.xun.sodaability.download.core

interface DownloadListener {
    fun onStartDownload()

    fun onProgress(progress: Int)

    fun onFinishDownload(url: String, path: String)

    fun onFail(url: String, errorInfo: String)
}