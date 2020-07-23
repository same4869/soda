package com.xun.sodaui.banner.indicator

import android.view.View

/**
 * @Description:
 * @Author:         xwang
 * @CreateDate:     2020/7/22
 */

interface ISodaIndicator<out T : View> {
    fun get(): T

    /**
     * 初始化
     * count : 幻灯片数量
     */
    fun onInflate(count: Int)

    /**
     * 幻灯片切换回调
     * current 切换到幻灯片位置
     * count 幻灯片数量
     */
    fun onPointChange(current: Int, count: Int)
}