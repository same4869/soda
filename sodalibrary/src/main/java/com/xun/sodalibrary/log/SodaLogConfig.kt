package com.xun.sodalibrary.log

import com.xun.sodalibrary.log.formatter.SodaStackTraceFormatter
import com.xun.sodalibrary.log.printer.SodaLogPrinter

/**
 * @Description:
 * @Author:         xwang
 * @CreateDate:     2020/7/15
 */
open class SodaLogConfig {
    companion object {
        const val MAX_LEN = 512 //每一行最多显示这么多的字符
        val HI_STACK_TRACE_FORMATTER = SodaStackTraceFormatter()
    }

    open fun enable(): Boolean {
        return true
    }

    open fun printers(): Array<SodaLogPrinter>? {
        return null
    }

    open fun injectJsonParser(): JsonParser? {
        return null
    }

    open fun stackTraceDepth(): Int {
        return 0
    }

    interface JsonParser {
        fun toJson(src: Any): String
    }
}