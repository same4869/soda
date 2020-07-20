package com.xun.sodaui.tab.comm

import android.view.ViewGroup
import com.xun.sodaui.tab.bottom.SodaTabBottom

/**
 * @Description:
 * @Author:         xwang
 * @CreateDate:     2020/7/18
 */

interface ISodaTabLayout<Tab : ViewGroup, D> {
    fun findTab(data: D): SodaTabBottom?

    fun addTabSelectedChangeListener(listener: OnTabSelectedListener<D>)

    fun defaultSelected(defaultInfo: D)

    fun inflateInfo(infoList: List<D>)

    interface OnTabSelectedListener<D> {
        fun onTabSelectedChange(index: Int, prevInfo: D?, nextInfo: D)
    }
}

