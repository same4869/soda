package com.xun.sodaui.refresh

import android.view.GestureDetector
import android.view.MotionEvent

/**
 * @Description:
 * @Author:         xwang
 * @CreateDate:     2020/7/21
 */

open class SodaGestureDetector : GestureDetector.OnGestureListener {
    override fun onShowPress(p0: MotionEvent?) {
    }

    override fun onSingleTapUp(p0: MotionEvent?): Boolean {
        return false
    }

    override fun onDown(p0: MotionEvent?): Boolean {
        return false
    }

    override fun onFling(p0: MotionEvent?, p1: MotionEvent?, p2: Float, p3: Float): Boolean {
        return false
    }

    override fun onScroll(p0: MotionEvent?, p1: MotionEvent?, p2: Float, p3: Float): Boolean {
        return false
    }

    override fun onLongPress(p0: MotionEvent?) {
    }

}