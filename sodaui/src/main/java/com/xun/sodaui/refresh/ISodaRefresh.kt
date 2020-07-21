package com.xun.sodaui.refresh

/**
 * @Description:
 * @Author:         xwang
 * @CreateDate:     2020/7/21
 */

interface ISodaRefresh {
    /**
     * 刷新时是否禁止滚动
     *
     * @param disableRefreshScroll 否禁止滚动
     */
    fun setDisableRefreshScroll(disableRefreshScroll: Boolean)

    /**
     * 刷新完成
     */
    fun refreshFinished()

    /**
     * 设置下拉刷新的监听器
     *
     * @param sodaRefreshListener 刷新的监听器
     */
    fun setRefreshListener(sodaRefreshListener: SodaRefreshListener?)

    /**
     * 设置下拉刷新的视图
     *
     * @param sodaOverView 下拉刷新的视图
     */
    fun setRefreshOverView(sodaOverView: SodaOverView?)

    interface SodaRefreshListener {
        fun onRefresh()
        fun enableRefresh(): Boolean
    }
}