package com.xun.soda.net.converter

import com.google.gson.reflect.TypeToken
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import java.lang.reflect.Type

/**
 *  author: xun.wang on 2019/5/31
 *  全局错误码拦截器
 **/

class CustomGsonConverterFactory : Converter.Factory() {

    override fun responseBodyConverter(
        type: Type, annotations: Array<Annotation>?,
        retrofit: Retrofit?
    ): Converter<ResponseBody, *> {
        return CustomGsonResponseBodyConverter(TypeToken.get(type))
    }

    override fun requestBodyConverter(
        type: Type,
        parameterAnnotations: Array<Annotation>?, methodAnnotations: Array<Annotation>?, retrofit: Retrofit?
    ): Converter<*, RequestBody> {
        return CustomGsonRequestBodyConverter(TypeToken.get(type))
    }
}