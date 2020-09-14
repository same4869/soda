package com.xun.soda.net

import com.xun.soda.BuildConfig
import com.xun.soda.net.converter.CustomGsonConverterFactory
import com.xun.soda.net.converter.exception.BaseErrorConsumer
import com.xun.sodaability.comm.applySchedulers
import com.xun.sodaability.mock.createMocker
import com.xun.sodalibrary.log.SodaLog
import com.xun.sodalibrary.utils.APPLICATION
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Action
import io.reactivex.functions.Consumer
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import java.io.File
import java.util.concurrent.TimeUnit

/**
 *  author: xun.wang on 2019/4/8
 **/
object RetrofitClient {
    private val httpCacheDirectory: File = File(APPLICATION.cacheDir, "app_cache")
    private val cache: Cache?
    val okHttpClient: OkHttpClient
    private var retrofit: Retrofit
    private val DEFAULT_TIMEOUT: Long = 15
    private val url = "https://api-community.mihoyo.com/" //默认占坑，无实际意义，真正的baseurl在header拦截器里设置

    private val serviceCacheMap = HashMap<Int, Any>()

    init {
        cache = if (httpCacheDirectory.exists()) {
            Cache(httpCacheDirectory, 10 * 1024 * 1024)
        } else {
            null
        }

        val interceptor = HttpLoggingInterceptor(object : HttpLoggingInterceptor.Logger {
            override fun log(message: String) {
                SodaLog.d("okhttp response : $message")
            }
        })

        interceptor.level = HttpLoggingInterceptor.Level.BODY

        val okHttpClientBuilder = OkHttpClient.Builder()
            .addNetworkInterceptor(interceptor)
            .cache(cache)
            .addInterceptor(MoreBaseUrlInterceptor())
            .addInterceptor(LoginStatusInterceptor())
//            .addInterceptor(Wolf.getNetInterceptor())
            .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)

        okHttpClient = okHttpClientBuilder.build()
        retrofit = Retrofit.Builder().client(okHttpClient)
            .addConverterFactory(CustomGsonConverterFactory())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .baseUrl(url)
            .build()
    }

    fun getInstance() = RetrofitClient

    fun getApiServiceWithBaseUrl(): ApiService {
        return getOrCreateService(ApiService::class.java)
    }

    @Deprecated(
        "use getOrCreateService() instead", ReplaceWith(
            "getOrCreateService(className)",
            "com.mihoyo.hyperion.net.RetrofitClient.getOrCreateService"
        )
    )
    fun <T> getApiServiceWithBaseUrl(className: Class<T>): T {
        return getOrCreateService(className)
    }

    /**
     * 如果[serviceCacheMap]中存在，则从其中取
     * 如果不存在，则新建并放入map中，并返回该值
     */
    inline fun <reified T> getOrCreateService(): T {
        return getOrCreateService(T::class.java)
    }

    @Suppress("UNCHECKED_CAST")
    fun <T> getOrCreateService(clazz: Class<T>): T {
        var result = serviceCacheMap[clazz.name.hashCode()]
        if (result == null) {
            //第二个参数一般传BuildConfig.ISDEBUG来保证正式包是非mock的数据
            result = retrofit.createMocker(clazz, true)//retrofit.create(clazz)
            serviceCacheMap[clazz.name.hashCode()] = result!!
        }
        return result as T
    }

}

fun <T> Observable<T>.request(
    onNext: (data: T) -> Unit
): Disposable {
    return request(onNext, null, {})
}

fun <T> Observable<T>.request(
    onNext: (data: T) -> Unit,
    onError: ((t: Throwable) -> Unit)? = null,
    onComplete: () -> Unit = {}
): Disposable {
    return applySchedulers()
        .subscribe(
            Consumer { onNext(it) },
            if (onError == null) BaseErrorConsumer() else Consumer { onError(it) },
            Action { onComplete() }
        )
}