package com.xun.sodahotfix.tinker.utils

import com.xun.sodalibrary.log.SodaLog

/**
 * @Description:
 * @Author:         xwang
 * @CreateDate:     2020/8/19
 */

object LogUtils {
    fun d(contents: Any) {
        SodaLog.d(contents)
    }

    fun d(tag: String = "", contents: Any) {
        SodaLog.d(tag, contents)
    }
}