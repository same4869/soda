package com.xun.sodaability.download.core

import android.annotation.SuppressLint
import com.xun.sodaability.comm.applySchedulers
import com.xun.sodalibrary.log.SodaLog
import com.xun.sodalibrary.utils.SodaSPUtils
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import java.io.*
import java.util.concurrent.TimeUnit

/**
 * @Description:
 * @Author:         xwang
 * @CreateDate:     2020/8/20
 */

class DownloadHelper {
    companion object {
        const val DOWNLOAD_FILE_FOLDER = "/soda_download/"
        private const val DEFAULT_TIMEOUT: Long = 10
    }

    private var listener: DownloadListener? = null
    private var downloadUrl = ""
    private val mInterceptor: DownloadInterceptor
    private var isDownloading = false
    private var mDisposable: Disposable? = null
    private val mDownloadApi: DownloadService
    private val url = "https://www.baidu.com/" //默认占坑，无实际意义，真正的baseurl在header拦截器里设置

    private val mPreferenceEditor by lazy {
        SodaSPUtils.getInstance(SodaSPUtils.SpName.SP_TABLE_DOWNLOAD).edit()
    }

    private val mListener: DownloadListener = object :
        DownloadListener {
        override fun onStartDownload() {
            listener?.onStartDownload()
        }

        override fun onProgress(progress: Int) {
            listener?.onProgress(progress)
        }

        override fun onFinishDownload(url: String, path: String) {
            listener?.onFinishDownload(url, path)
        }

        override fun onFail(url: String, errorInfo: String) {
            listener?.onFail(url, errorInfo)
        }
    }

    init {
        mInterceptor =
            DownloadInterceptor(mListener)
        val httpClient = OkHttpClient.Builder()
            .addInterceptor(mInterceptor)
            .retryOnConnectionFailure(true)
            .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
            .build()

        mDownloadApi = Retrofit.Builder()
            .baseUrl(url)
            .client(httpClient)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build().create(DownloadService::class.java)
    }

    fun init(listener: DownloadListener): DownloadHelper {
        this.listener = listener
        return this
    }

    fun download(
        downloadUrl: String,
        filePath: String,
        startFromBreakpoint: Boolean = false,
        range: Long = 0
    ) {
        this.downloadUrl = downloadUrl
        SodaLog.d("DownloadHelper download ")
        mInterceptor.setIsWithPoint(startFromBreakpoint)

        mListener.onStartDownload()
        isDownloading = true
        if (startFromBreakpoint) { //断点下载

            val file = File(filePath)
            var totalLength = "-"
            if (file.exists()) {
                totalLength += file.length()
            }

            SodaLog.d("download bytes=$range$totalLength")
            mDisposable?.dispose()

            mDisposable = mDownloadApi.downloadWithPoint("bytes=$range-", downloadUrl)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe({
                    writeFileWithPoint(it, filePath, range, downloadUrl)
                }, { onDownloadError(it) })
        } else { // 非断点下载
            mDisposable?.dispose()

            mDisposable = mDownloadApi.download(downloadUrl).applySchedulers()
                .map { t -> t.byteStream() }
                .observeOn(Schedulers.io())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    writeFile(it, filePath)
                }, { onDownloadError(it) })
        }
    }

    @SuppressLint("CheckResult")
    private fun writeFileWithPoint(
        responseBody: ResponseBody,
        path: String,
        range: Long,
        url: String
    ) {
        var randomAccessFile: RandomAccessFile? = null
        var inputStream: InputStream? = null
        var total = range
        var responseLength: Long = 0
        try {
            val buf = ByteArray(10 * 1024)
            responseLength = responseBody.contentLength()
            inputStream = responseBody.byteStream()

            val file = File(path)
            if (!file.exists()) {
                file.parentFile?.mkdirs()
                file.createNewFile()
            }
            randomAccessFile = RandomAccessFile(file, "rwd")
            if (range == 0L) {
                randomAccessFile.setLength(responseLength)
            }
            randomAccessFile.seek(range)

            var progress = 0
            var lastProgress = 0

            var length: Int = inputStream.read(buf)
            while (length != -1) {
                randomAccessFile.write(buf, 0, length)
                total += length.toLong()
                mPreferenceEditor.putLong(url, total)
                lastProgress = progress
                progress = (total * 100 / randomAccessFile.length()).toInt()

                if (progress > 0 && progress != lastProgress) {
                    mPreferenceEditor.apply()
                    //切换到主线程
                    Observable.just(progress)
                        .subscribeOn(AndroidSchedulers.mainThread())
                        .subscribe {
                            mListener.onProgress(progress)
                        }
                }
                length = inputStream.read(buf)
            }
            onDownloadFinish(path)
        } catch (e: Exception) {
            onDownloadError(e)
        } finally {
            try {
                randomAccessFile?.close()
                inputStream?.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
    }

    private fun writeFile(inputString: InputStream, filePath: String) {
        val file = File(filePath)
        if (file.exists()) {
            file.delete()
        }
        createFileByDeleteOldFile(file)

        try {
            val fos = FileOutputStream(file)
            val b = ByteArray(1024)

            var len: Int = inputString.read(b)
            while (len != -1) {
                fos.write(b, 0, len)
                len = inputString.read(b)
            }
            inputString.close()
            fos.close()
            onDownloadFinish(filePath)
        } catch (e: FileNotFoundException) {
            onDownloadError(e)
        } catch (e: IOException) {
            onDownloadError(e)
        }
    }

    private fun createFileByDeleteOldFile(file: File?): Boolean {
        if (file == null) return false
        // file exists and unsuccessfully delete then return false
        if (file.exists() && !file.delete()) return false
        if (!createOrExistsDir(file.parentFile)) return false
        return try {
            file.createNewFile()
        } catch (e: IOException) {
            e.printStackTrace()
            false
        }
    }

    private fun createOrExistsDir(file: File?): Boolean {
        return file != null && if (file.exists()) file.isDirectory else file.mkdirs()
    }

    fun isDownloading(): Boolean {
        return isDownloading
    }

    fun stopDownload() {
        if (mDisposable?.isDisposed != true) {
            mDisposable?.dispose()
        }
        mDisposable = null
        isDownloading = false
        downloadUrl = ""
    }

    private fun onDownloadFinish(path: String) {
        mPreferenceEditor.apply()
        downloadUrl = ""
        isDownloading = false
        mListener.onFinishDownload(url, path)
        SodaLog.d("download finish")
    }

    @SuppressLint("CheckResult")
    private fun onDownloadError(e: Throwable) {
        e.printStackTrace()
        mPreferenceEditor.apply()
        isDownloading = false
        downloadUrl = ""
        SodaLog.d("download err : ${e.message}")
        Observable.just(e)
            .subscribeOn(AndroidSchedulers.mainThread())
            .subscribe {
                mListener.onFail(downloadUrl, it.message ?: "download error")
            }
    }

}