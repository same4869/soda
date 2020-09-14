package com.xun.soda.net

import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

/**
 *  author: xun.wang on 2019/4/9
 *  处理登录态，仅演示，do nothing
 **/
class LoginStatusInterceptor : Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val builder = originalRequest.newBuilder()

//        //如果需要登录态，则去sp中把登录的相关信息找出来拼到header的cookie字段中去
//        val accountId = AccountManager.getUserId()
//        val cookieToken = AccountManager.getSToken()
//        if (accountId != "0" && !TextUtils.isEmpty(cookieToken)) {
//            builder.addHeader("cookie", "stuid=$accountId;stoken=$cookieToken;")
//        }
//
//        //这里加入通用公共参数
//        requestCommonParams().forEach { entity ->
//            builder.addHeader(entity.key, entity.value)
//        }

//        //不是我们自己的api，不加这个，不然可能被三方服务器拒绝
//        val headerList = originalRequest.headers("otherUrl")
//        if (headerList.size > 0) {
//            builder.removeHeader("otherUrl")
//            val isOtherUrl = headerList[0]
//            if (isOtherUrl == "true") {
//                builder.removeHeader("Referer")
//            }
//        }

        val newRequest = builder.build()
        return chain.proceed(newRequest)
    }
}
