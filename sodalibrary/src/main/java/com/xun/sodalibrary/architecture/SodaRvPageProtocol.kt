package com.xun.sodalibrary.architecture

/**
 * @Description:
 * @Author:         xwang
 * @CreateDate:     2020/8/11
 */

interface SodaRvPageProtocol : SodaCommPageProtocol {
    fun refreshDatas(datas: List<Any>, isLoadMore: Boolean = false, extra: Any = Any())
}