package com.xun.soda.net.converter.exception

import io.reactivex.functions.Consumer
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException

/**
 *  author: xun.wang on 2019/5/31
 *  全局 RxJava 异常处理 Error Consumer
 *  仅示例，do nothing
 **/
open class BaseErrorConsumer(private val mBlock: (errCode: Int) -> Unit = {}) :
    Consumer<Throwable> {

    companion object {
        const val ERROR_NET = -999
        const val PARSE_JSON_ERROR = -3000
    }

    override fun accept(e: Throwable) {
//        if (!isNetConnected()) {
//            SodaLog.d("no net")
//            mBlock(ERROR_NET)
//            showToast(getResString(R.string.error_message_toast))
//            return
//        }
        when (e) {
            is HttpException -> {// We had non-2XX http error
//                SodaLog.d("HttpException")
//                showToast(getResString(R.string.error_message_toast))
//                mBlock(ERROR_NET)
//                logError(e)
            }
            is SocketTimeoutException -> {
//                SodaLog.d("SocketTimeoutException")
//                mBlock(ERROR_NET)
//                logError(e)
//                showToast(getResString(R.string.error_message_toast))
            }
            is IOException -> {// A network or conversion error happened
//                SodaLog.d("IOException")
//                mBlock(ERROR_NET)
//                logError(e)
//                showToast(getResString(R.string.error_message_toast))
            }
            is ApiException -> {
//                SodaLog.d("ApiException : " + e.errorCode)
//                when (e.errorCode) {
//                    ERROR_CODE_100, ERROR_CODE_110 -> {
//                        //如果丢失了登录态，清掉SP里面的值
//                        showToast(e.errorMessage)
//                        AccountManager.logOut()
//                        mBlock(e.errorCode)
//                    }
//
//                    //通行证错误，弹toast
//                    //通行证200的错误，需要在业务回调内自己处理
//                    ERROR_CODE_PASS_400 -> {
//                        val baseBean = Gson().fromJson(e.errorMessage, BaseAccountBean::class.java)
//                        if (baseBean.data.status == ERROR_CODE_PASS_107 || baseBean.data.status == ERROR_CODE_PASS_102 || baseBean.data.status ==
//                            ERROR_CODE_PASS_201 || baseBean.data.status == ERROR_CODE_PASS_202
//                        ) {
//                            showToast(baseBean.data.msg)
//                        }
//                        mBlock(baseBean.data.status)
//                    }
//
//                    PARSE_JSON_ERROR -> {
//                        if (BuildConfig.ISDEBUG) {
//                            showToast(APPLICATION.resources.getString(R.string.api_log_parse_json_error))
//                        }
//                        mBlock(e.errorCode)
//                    }
//
//                    else -> {
//                        logError(e)
//                        mBlock(e.errorCode)
//                        showToast(getResString(R.string.error_message_toast))
//                        SodaLog.d("code:${e.errorCode} msg:${e.errorMessage}")
//                    }
//                }
            }
            else -> {
                logError(e)
//                SodaLog.d(APPLICATION.resources.getString(R.string.api_log_unknown_error))
//                showToast(getResString(R.string.error_message_toast))
            }
        }
    }

    private fun logError(e: Throwable) {
//        if (BuildConfig.ISDEBUG) {
//            e.printStackTrace()
//            if (NetworkUtils.isAvailable) {
//                Wolf.saveCrashLog(e)
//            }
//        }
    }

}
