package com.xun.sodaability.download.core

import okhttp3.MediaType
import okhttp3.ResponseBody
import okio.*
import java.io.IOException

class DownloadResponseBody(
    responseBody: ResponseBody,
    downloadListener: DownloadListener,
    private var isWithPoint: Boolean
) : ResponseBody() {
    private var mResponseBody: ResponseBody? = responseBody
    private var mDownloadListener: DownloadListener? = downloadListener
    private var mBufferedSource: BufferedSource? = null

    fun setIsWithPoint(isWithPoint: Boolean) {
        this.isWithPoint = isWithPoint
    }

    fun getIsWithPoint(): Boolean {
        return isWithPoint
    }

    override fun contentLength(): Long {
        return mResponseBody!!.contentLength()
    }

    override fun contentType(): MediaType? {
        return mResponseBody!!.contentType()
    }

    override fun source(): BufferedSource {
        if (mBufferedSource == null) {
            mBufferedSource = source(mResponseBody!!.source()).buffer()
        }
        return mBufferedSource!!
    }

    private fun source(source: Source): Source {
        return object : ForwardingSource(source) {
            var totalBytesRead = 0L

            @Throws(IOException::class)
            override fun read(sink: Buffer, byteCount: Long): Long {
                val bytesRead = super.read(sink, byteCount)
                // read() returns the number of bytes read, or -1 if this source is exhausted.
                totalBytesRead += if (bytesRead != -1L) bytesRead else 0
                if (null != mDownloadListener) {
                    if (bytesRead != -1L) {
                        if (!isWithPoint) {
                            mDownloadListener!!.onProgress((totalBytesRead * 100 / mResponseBody!!.contentLength()).toInt())
                        }
                    }
                }
                return bytesRead
            }
        }
    }
}