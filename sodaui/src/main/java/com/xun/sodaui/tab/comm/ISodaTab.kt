package com.xun.sodaui.tab.comm

/**
 * @Description:
 * @Author:         xwang
 * @CreateDate:     2020/7/18
 */

interface ISodaTab<D> : ISodaTabLayout.OnTabSelectedListener<D> {
    fun setSodaTabInfo(data: D)

    fun resetHeight(height: Int)
}