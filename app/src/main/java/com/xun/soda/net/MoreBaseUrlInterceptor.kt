package com.xun.soda.net

import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

/**
 *  author: xun.wang on 2019/4/9
 *  处理多个host的逻辑,仅演示，do nothing
 **/
class MoreBaseUrlInterceptor : Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        //获取原始的originalRequest
        val originalRequest = chain.request()
//        //获取老的url
//        val oldUrl = originalRequest.url
//        //获取originalRequest的创建者builder
//        val builder = originalRequest.newBuilder()
//        //获取头信息的集合如：manage,mdffx
//        val urlnameList = originalRequest.headers(ApiType.KEY_HEADER)
//        if (urlnameList.size > 0) {
//            //删除原有配置中的值,就是namesAndValues集合里的值
//            builder.removeHeader(ApiType.KEY_HEADER)
//            //获取头信息中配置的value,如：manage或者mdffx
//            val urlname = ApiType.getType(urlnameList[0])
//            var baseURL: HttpUrl? = null
//            //根据头信息中配置的value,来匹配新的base_url地址
//            //http协议如：http或者https
//            //主机地址
//            //端口
//            //获取处理后的新newRequest
//            when (urlname) {
//                ApiType.Type.API_TAKUMI -> baseURL =
//                    ApiUtils.getBaseUrl(ApiUtils.HOST_TYPE.API_TAKUMI).toHttpUrlOrNull()
//                ApiType.Type.API_LOGIN -> baseURL =
//                    ApiUtils.getBaseUrl(ApiUtils.HOST_TYPE.API_LOGIN).toHttpUrlOrNull()
//            }
//
//            //重建新的HttpUrl，需要重新设置的url部分
//            val newHttpUrl = oldUrl.newBuilder()
//                .scheme(baseURL!!.scheme)//http协议如：http或者https
//                .host(baseURL.host)//主机地址
//                .port(baseURL.port)//端口
//                .build()
//            //获取处理后的新newRequest
//            val newRequest = builder.url(newHttpUrl).build()
//            return chain.proceed(newRequest)
//        } else {
            return chain.proceed(originalRequest)
//        }
    }
}
