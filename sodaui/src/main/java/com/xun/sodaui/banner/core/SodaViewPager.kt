package com.xun.sodaui.banner.core

import android.app.Activity
import android.content.Context
import android.os.Handler
import android.view.MotionEvent
import androidx.viewpager.widget.ViewPager

/**
 * @Description:    自动翻页的ViewPager
 * @Author:         xwang
 * @CreateDate:     2020/7/22
 */

class SodaViewPager(context: Context) : ViewPager(context) {
    private var mIntervalTime = 0L
    private var mAutoPlay = true
    private var isLayout = false
    private val mHandler = Handler()
    private val mRunnable = object : Runnable {
        override fun run() {
            next()
            mHandler.postDelayed(this, mIntervalTime)
        }
    }

    fun setAutoPlay(autoPlay: Boolean) {
        this.mAutoPlay = autoPlay
        if (!mAutoPlay) {
            mHandler.removeCallbacks(mRunnable)
        }
    }

    override fun onTouchEvent(ev: MotionEvent): Boolean {
        when (ev.action) {
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                start()
            }
            else -> {
                stop()
            }
        }
        return super.onTouchEvent(ev)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        super.onLayout(changed, l, t, r, b)
        isLayout = true
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        if (isLayout && adapter != null && adapter!!.count > 0) {
            try {
                //fix 使用RecyclerView + ViewPager bug https://blog.csdn.net/u011002668/article/details/72884893
                val mScroller = ViewPager::class.java.getDeclaredField("mFirstLayout")
                mScroller.isAccessible = true
                mScroller[this] = false
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        start()
    }

    override fun onDetachedFromWindow() {
        //fix 使用RecyclerView + ViewPager bug
        if ((context as Activity).isFinishing) {
            super.onDetachedFromWindow()
        }
        stop()
    }

    /**
     * 设置ViewPager的滚动速度
     *
     * @param duration page切换的时间长度
     */
    fun setScrollDuration(duration: Int) {
        try {
            val scrollerField = ViewPager::class.java.getDeclaredField("mScroller")
            scrollerField.isAccessible = true
            scrollerField[this] = SodaBannerScroller(context, duration)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }

    /**
     * 设置页面停留时间
     *
     * @param intervalTime 停留时间单位毫秒
     */
    fun setIntervalTime(intervalTime: Int) {
        mIntervalTime = intervalTime.toLong()
    }

    private fun start() {
        mHandler.removeCallbacksAndMessages(null)
        if (mAutoPlay) {
            mHandler.postDelayed(mRunnable, mIntervalTime)
        }
    }

    private fun stop() {
        mHandler.removeCallbacksAndMessages(null) //停止Timer
    }

    /**
     * 设置下一个要显示的item，并返回item的pos
     *
     * @return 下一个要显示item的pos
     */
    private fun next(): Int {
        var nextPosition = -1
        if (adapter == null || adapter!!.count <= 1) {
            stop()
            return nextPosition
        }
        nextPosition = currentItem + 1
        //下一个索引大于adapter的view的最大数量时重新开始
        if (nextPosition >= adapter!!.count) {
            nextPosition = (adapter as SodaBannerAdapter).getFirstItem()
        }
        setCurrentItem(nextPosition, true)
        return nextPosition
    }

}