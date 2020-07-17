package com.xun.sodalibrary.log

import com.xun.sodalibrary.log.printer.SodaConsolePrinter
import com.xun.sodalibrary.log.printer.SodaLogPrinter

/**
 * @Description:
 * @Author:         xwang
 * @CreateDate:     2020/7/16
 */

object SodaLogManager {
    private var mConfig: SodaLogConfig? = null
    private var mPrinters = mutableListOf<SodaLogPrinter>()

    //支持init自己定制config，也可以使用默认的
    fun init(config: SodaLogConfig, vararg printers: SodaLogPrinter) {
        mConfig = config
        mPrinters.add(SodaConsolePrinter())
        printers.forEach {
            mPrinters.add(it)
        }
    }

    fun getConfig(): SodaLogConfig {
        if (mConfig == null) {
            mConfig = getDefaultConfig()
        }
        return mConfig!!
    }

    fun getPrinters(): List<SodaLogPrinter> {
        return mPrinters
    }

    fun addPrinter(printer: SodaLogPrinter) {
        mPrinters.add(printer)
    }

    fun removePrinter(printer: SodaLogPrinter) {
        mPrinters.remove(printer)
    }

    private fun getDefaultConfig(): SodaLogConfig {
        val config = SodaLogConfig()
        addPrinter(SodaConsolePrinter())
        return config
    }

}