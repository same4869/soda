package com.xun.soda

import android.app.Application
import com.google.gson.Gson
import com.xun.sodalibrary.log.SodaLogConfig
import com.xun.sodalibrary.log.SodaLogManager
import com.xun.sodalibrary.log.printer.SodaFilePrinter

/**
 * @Description:
 * @Author:         xwang
 * @CreateDate:     2020/7/16
 */

class SodaSampleApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        SodaLogManager.init(object : SodaLogConfig() {
            override fun injectJsonParser(): JsonParser? {
                return object : JsonParser {
                    override fun toJson(src: Any): String {
                        return Gson().toJson(src)
                    }
                }
            }
        }, SodaFilePrinter().apply {
            init(applicationContext.cacheDir.absolutePath, 60 * 1000 * 60)
        })
    }
}