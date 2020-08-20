package com.xun.sodaability.download.core

import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*


/**
 * @Description:
 * @Author:         xwang
 * @CreateDate:     2019-10-14
 */

interface DownloadService {
    @Streaming
    @GET
    fun download(@Url url: String): Observable<ResponseBody>

    @Streaming
    @GET
    fun downloadWithPoint(@Header("Range") range: String, @Url url: String): Observable<ResponseBody>

    @HEAD
    fun getHeaderOnly(@Url url: String): Observable<Response<Void>>
}