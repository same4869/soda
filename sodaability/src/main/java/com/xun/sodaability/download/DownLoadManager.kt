package com.xun.sodaability.download

import android.net.Uri
import com.xun.sodaability.download.core.DownloadHelper
import com.xun.sodaability.download.core.DownloadListener
import com.xun.sodalibrary.log.SodaLog
import com.xun.sodalibrary.utils.SodaSPUtils
import com.xun.sodalibrary.utils.getCacheDirPath
import java.io.File

/**
 * @Description:
 * @Author:         xwang
 * @CreateDate:     2020/8/20
 */

class DownLoadManager {
    private var downloadListener: DownloadListener? = null
    private var downloadUrl: String = ""
    private var fileName: String = ""
    private var isWithPoint: Boolean = false
    private var mDownloadHelper : DownloadHelper = DownloadHelper()

    private val mListener: DownloadListener = object :
        DownloadListener {
        override fun onStartDownload() {
            downloadListener?.onStartDownload()
        }

        override fun onProgress(progress: Int) {
            downloadListener?.onProgress(progress)
        }

        override fun onFinishDownload(url: String, path: String) {
            downloadListener?.onFinishDownload(url, path)
        }

        override fun onFail(url: String, errorInfo: String) {
            downloadListener?.onFail(url, errorInfo)
        }
    }

    fun stopDownload() {
        if (mDownloadHelper.isDownloading()) {
            mDownloadHelper.stopDownload()
        }
    }


    fun continueDownload() {
        if (downloadListener != null && !mDownloadHelper.isDownloading()) {
            download(downloadUrl, fileName, isWithPoint, downloadListener!!)
        }
    }

    fun download(
        downloadUrl: String,
        fileName: String,
        startFromBreakpoint: Boolean,
        downloadListener: DownloadListener
    ) {
        val filePath = getCacheDirPath() + DownloadHelper.DOWNLOAD_FILE_FOLDER + fileName
        downloadWithAbsolutePath(downloadUrl, filePath, startFromBreakpoint, downloadListener)
    }

    fun downloadWithAbsolutePath(
        downloadUrl: String,
        path: String,
        startFromBreakpoint: Boolean,
        downloadListener: DownloadListener
    ) {
        this.downloadUrl = downloadUrl
        this.isWithPoint = startFromBreakpoint
        this.downloadListener = downloadListener
        this.fileName = Uri.parse(path).lastPathSegment ?: ""
        SodaLog.d("sodaDownload path:$path")


        if (startFromBreakpoint) {
            val file = File(path)
            var range: Long = 0
            val progress: Int
            if (file.exists()) {
                range = SodaSPUtils.getInstance(SodaSPUtils.SpName.SP_TABLE_DOWNLOAD).getLong(downloadUrl, 0)
                progress = (range * 100 / file.length()).toInt()
                SodaLog.d("sodaDownload 文件已下载${progress}%  range:$range, fileLength:${file.length()}")
                if (range == file.length()) {
                    SodaLog.d("sodaDownload 上次文件已下载完成")
                    return
                }
            }
            mDownloadHelper.init(mListener).download(
                downloadUrl,
                path,
                startFromBreakpoint = startFromBreakpoint,
                range = range
            )
            return
        }

        mDownloadHelper.init(mListener)
            .download(downloadUrl, path)
    }

}