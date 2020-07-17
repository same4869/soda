package com.xun.sodalibrary.log.formatter

interface SodaLogFormatter<T> {
    fun format(data: T): String
}