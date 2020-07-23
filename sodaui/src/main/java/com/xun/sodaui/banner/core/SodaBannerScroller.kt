package com.xun.sodaui.banner.core

import android.content.Context
import android.widget.Scroller

/**
 * @Description:
 * @Author:         xwang
 * @CreateDate:     2020/7/22
 */

class SodaBannerScroller(context: Context?, duration: Int) : Scroller(context) {
    /**
     * 值越大，滑动越慢
     */
    private var mDuration = 1000

    init {
        mDuration = duration
    }

    override fun startScroll(startX: Int, startY: Int, dx: Int, dy: Int) {
        super.startScroll(startX, startY, dx, dy, mDuration)
    }

    override fun startScroll(startX: Int, startY: Int, dx: Int, dy: Int, duration: Int) {
        super.startScroll(startX, startY, dx, dy, mDuration)
    }
}