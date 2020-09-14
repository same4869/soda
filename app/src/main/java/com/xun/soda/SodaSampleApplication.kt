package com.xun.soda

import android.app.Application
import com.google.gson.Gson
import com.xun.sodaability.hotfix.tinker.app.SodaTinkerApplicationLike
import com.xun.sodalibrary.log.SodaLogConfig
import com.xun.sodalibrary.log.SodaLogManager
import com.xun.sodalibrary.log.printer.SodaFilePrinter
import com.xun.sodalibrary.utils.APPLICATION

/**
 * @Description:
 * @Author:         xwang
 * @CreateDate:     2020/7/16
 */

//TODO 使用library库里的tinkerapplication作为入口时没办法调到上层的这个类，需要看看怎么解决
class SodaSampleApplication : SodaTinkerApplicationLike.InitInApplicationInterface {

    override fun onInitInApplication(application: Application) {
//        SodaTinkerApplicationLike.APP_LIKE_INSTANCE?.setInitInApplicationInterface(this)

        APPLICATION = application

        SodaLogManager.init(object : SodaLogConfig() {
            override fun injectJsonParser(): JsonParser? {
                return object : JsonParser {
                    override fun toJson(src: Any): String {
                        return Gson().toJson(src)
                    }
                }
            }
        }, SodaFilePrinter().apply {
            init(application.cacheDir.absolutePath, 60 * 1000 * 60)
        })
    }
}