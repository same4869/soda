package com.xun.soda.net.converter

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import retrofit2.Converter
import java.io.IOException

/**
 *  author: xun.wang on 2019/5/31
 **/
internal class CustomGsonRequestBodyConverter<T>(private val typeToken: TypeToken<T>) : Converter<T, RequestBody> {

    @Throws(IOException::class)
    override fun convert(value: T): RequestBody {
        return RequestBody.create(MEDIA_TYPE, Gson().toJson(value))
    }

    companion object {
        private val MEDIA_TYPE = "application/json; charset=UTF-8".toMediaTypeOrNull()
    }
}