package com.xun.sodalibrary.log

import android.util.Log
import com.xun.sodalibrary.log.utils.SodaStackTraceUtil

/**
 * @Description:
 * @Author:         xwang
 * @CreateDate:     2020/7/15
 */

object SodaLog {
    private val HI_LOG_PACKAGE: String =
        SodaLog::class.java.name.substring(0, SodaLog::class.java.name.lastIndexOf('.') + 1)
    private var mClassname: String? = null
    private var mMethods: ArrayList<String>? = null

    init {
        mClassname = SodaLog::class.java.name
        mMethods = ArrayList()

        val ms = SodaLog::class.java.declaredMethods
        for (m in ms) {
            mMethods!!.add(m.name)
        }
    }

    fun d(contents: Any) {
        log(Log.DEBUG, "", contents)
    }

    fun d(tag: String = "", contents: Any) {
        log(Log.DEBUG, tag, contents)
    }

    fun log(type: Int, tag: String, contents: Any) {
        log(SodaLogManager.getConfig(), type, tag, contents)
    }

    fun log(config: SodaLogConfig, type: Int = Log.DEBUG, tag: String = "", contents: Any) {
        if (!config.enable()) {
            return
        }
        val sb = StringBuilder()
        var mTag = tag
        var mLineNum = ""

        val extraInfo = getTagAndLineNumber(mTag)
        if (extraInfo.size >= 2) {
            mTag = extraInfo[0]
            mLineNum = extraInfo[1]
        }

        if (config.stackTraceDepth() > 0) {
            val stackTrace = SodaLogConfig.HI_STACK_TRACE_FORMATTER.format(
                SodaStackTraceUtil.getCroppedRealStackTrack(
                    Throwable().stackTrace,
                    HI_LOG_PACKAGE,
                    config.stackTraceDepth()
                )
            )
            sb.append(stackTrace).append("\n")
        }

        var body = parseBody(config, contents, mLineNum)
        if (body.isNotBlank()) {
            body = body.replace("\\\"", "\"")
        }
        sb.append(body)
        val printers = if (config.printers() != null) {
            config.printers()!!.asList()
        } else {
            SodaLogManager.getPrinters()
        }
        printers.forEach {
            it.print(config, type, mTag, sb.toString())
        }
    }

    private fun getTagAndLineNumber(tag: String): Array<String> {
        try {
            for (st in Throwable().stackTrace) {
                if (mClassname == st.className || mMethods!!.contains(st.methodName)) {
                    continue
                } else {
                    val b = st.className.lastIndexOf(".") + 1
                    val defaultTag = if (tag.isBlank()) {
                        st.className.substring(b)
                    } else {
                        tag
                    }
                    val message =
                        "method:${st.methodName}() line:${st.lineNumber} thread:${Thread.currentThread()} --> "
                    return arrayOf(defaultTag, message)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return arrayOf()
    }

    private fun parseBody(config: SodaLogConfig, contents: Any, prefixInfo: String): String {
        if (config.injectJsonParser() != null) {
            return prefixInfo + config.injectJsonParser()!!.toJson(contents)
        }
        //集合类型只能默认只能打印一层，嵌套太深还是自己注入一个json解析器好了
        if (contents is Collection<*>) {
            val sb = StringBuilder()
            sb.append("$prefixInfo[")
            contents.forEach {
                sb.append(it).append(",")
            }
            if (sb.isNotEmpty()) {
                sb.deleteCharAt(sb.length - 1)
            }
            sb.append("]")
            return sb.toString()
        }
        return prefixInfo + contents.toString()
    }
}