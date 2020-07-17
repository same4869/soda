package com.xun.sodalibrary.log.bean

import android.util.Log
import java.text.SimpleDateFormat
import java.util.*

/**
 * @Description:
 * @Author:         xwang
 * @CreateDate:     2020/7/17
 */

class SodaLogBean(private var timeMillis: Long, var level: Int, var tag: String, var log: String) {
    companion object {
        val sdf = SimpleDateFormat("yy-MM-dd HH:mm:ss:SSS", Locale.CHINA)
    }

    private fun format(timeMillis: Long): String {
        return sdf.format(timeMillis)
    }

    fun getFlattened(): String {
        return format(timeMillis) + '|' + formatLevel(level) + '|' + tag + "|:"
    }

    fun flattenedLog(): String {
        return getFlattened() + "\n" + log
    }

    private fun formatLevel(tag: Int): String {
        when (tag) {
            Log.DEBUG -> return "D"
            Log.ASSERT -> return "A"
            Log.ERROR -> return "E"
            Log.INFO -> return "I"
            Log.VERBOSE -> return "V"
            Log.WARN -> return "W"
        }
        return "LOG"
    }

}