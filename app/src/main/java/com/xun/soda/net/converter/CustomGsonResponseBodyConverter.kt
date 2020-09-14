package com.xun.soda.net.converter

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.ResponseBody
import retrofit2.Converter
import java.io.IOException

/**
 *  author: xun.wang on 2019/5/31
 *  仅演示,do nothing
 **/
internal class CustomGsonResponseBodyConverter<T>(private val typeToken: TypeToken<T>) :
    Converter<ResponseBody, T> {

    @Throws(IOException::class)
    override fun convert(value: ResponseBody): T? {
        val response = value.string()
//        val httpStatus: HttpStatus
//
//        try {
//            SodaLog.d("httpResponse -->gson parse : $response")
//            httpStatus = response.toObject()!!
//        } catch (e: Exception) {
//            e.printStackTrace()
//            SodaLog.d("gson parse error")
//            value.close()
//            throw ApiException(-3000, "gson parse error")
//        }
//
//        SodaLog.d(
//            "httpStatus.code --> " + httpStatus.code + " httpStatus.retcode --> " + httpStatus.retcode
//        )
//
//        when {
//            httpStatus.retcode == 0 && httpStatus.code == 0 -> {
//                //社区接口正常
//            }
//            httpStatus.retcode == 0 && httpStatus.code == 200 -> {
//                val permitHttpStatus: PermitHttpStatus =
//                    response.toObject<PermitHttpStatus>()!!
//                if (permitHttpStatus.data.status == 1) {
//                    //通行证接口正常
//                } else {
//                    //通行证接口异常
//                    value.close()
//                    val rtnCode = 2001
//                    val errMsg = permitHttpStatus.data.msg
//                    throw ApiException(rtnCode, errMsg, permitHttpStatus.data.status)
//                }
//            }
//            else -> {
//                value.close()
//                val rtnCode = if (httpStatus.retcode != 0) httpStatus.retcode else httpStatus.code
//                val errMsg = if (httpStatus.retcode != 0) httpStatus.message else response
//                throw ApiException(rtnCode, errMsg)
//            }
//        }
        return Gson().fromJson<T>(response, typeToken.type)
    }
}
