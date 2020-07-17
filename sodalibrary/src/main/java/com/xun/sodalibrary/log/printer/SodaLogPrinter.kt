package com.xun.sodalibrary.log.printer

import com.xun.sodalibrary.log.SodaLogConfig

interface SodaLogPrinter {
    fun print(config: SodaLogConfig, level: Int, tag: String, printString: String)
}