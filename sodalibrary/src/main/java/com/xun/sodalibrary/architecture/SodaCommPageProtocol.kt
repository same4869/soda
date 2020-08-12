package com.xun.sodalibrary.architecture

/**
 * @Description:
 * @Author:         xwang
 * @CreateDate:     2020/8/11
 */

interface SodaCommPageProtocol {
    fun refreshPageStatus(status: String, extra: Any = Any())
}