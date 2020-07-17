package com.xun.sodalibrary.log.printer

import android.util.Log
import com.xun.sodalibrary.log.SodaLogConfig
import com.xun.sodalibrary.log.SodaLogConfig.Companion.MAX_LEN
import java.lang.StringBuilder

/**
 * @Description:
 * @Author:         xwang
 * @CreateDate:     2020/7/16
 */

class SodaConsolePrinter : SodaLogPrinter {
    override fun print(config: SodaLogConfig, level: Int, tag: String, printString: String) {
        val len = printString.length
        val countOfSub = len / MAX_LEN
        if (countOfSub > 0) {
            val log = StringBuilder()
            var index = 0
            for (i in 0 until countOfSub) {
                log.append(printString.substring(index, index + MAX_LEN))
                index += MAX_LEN
            }
            if (index != len) {
                log.append(printString.substring(index, len))
            }
            Log.println(level, tag, log.toString())
        } else {
            Log.println(level, tag, printString)
        }
    }
}