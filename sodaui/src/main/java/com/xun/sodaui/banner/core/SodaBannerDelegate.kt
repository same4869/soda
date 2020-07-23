package com.xun.sodaui.banner.core

import android.content.Context
import android.view.View
import android.widget.FrameLayout
import androidx.viewpager.widget.ViewPager
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import com.xun.sodaui.banner.SodaBanner
import com.xun.sodaui.banner.indicator.ISodaIndicator
import com.xun.sodaui.banner.indicator.SodaCircleIndicator

/**
 * @Description:
 * @Author:         xwang
 * @CreateDate:     2020/7/22
 */

class SodaBannerDelegate(context: Context, banner: SodaBanner) : ViewPager.OnPageChangeListener,
    ISodaBanner {
    private var mContext: Context? = null
    private var mBanner: SodaBanner? = null
    private var mSodaBannerBeans: List<SodaBannerBean> = listOf()

    private var mAdapter: SodaBannerAdapter? = null
    private var mSodaIndicator: ISodaIndicator<View>? = null
    private var mOnPageChangeListener: OnPageChangeListener? = null
    private var mSodaViewPager: SodaViewPager? = null
    private var mOnBannerClickListener: ISodaBanner.OnBannerClickListener? = null

    private var mAutoPlay = false
    private var mLoop = false
    private var mIntervalTime = 5000
    private var mScrollDuration = -1

    init {
        mContext = context
        mBanner = banner
    }

    override fun onPageScrollStateChanged(state: Int) {
        mOnPageChangeListener?.onPageScrollStateChanged(state)
    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
        if (mAdapter?.getRealCount() != 0) {
            mOnPageChangeListener?.onPageScrolled(
                position % mAdapter!!.getRealCount(),
                positionOffset,
                positionOffsetPixels
            )
        }
    }

    override fun onPageSelected(position: Int) {
        if (mAdapter == null || mAdapter?.getRealCount() == 0) {
            return
        }
        var pos = position
        pos %= mAdapter!!.getRealCount()
        mOnPageChangeListener?.onPageSelected(pos)
        mSodaIndicator?.onPointChange(pos, mAdapter!!.getRealCount())
    }

    override fun setBannerData(layoutResId: Int, models: List<SodaBannerBean>) {
        mSodaBannerBeans = models
        init(layoutResId)
    }

    private fun init(layoutResId: Int) {
        if (mAdapter == null) {
            mAdapter = SodaBannerAdapter(mContext!!)
        }
        if (mSodaIndicator == null) {
            mSodaIndicator = SodaCircleIndicator(mContext!!) as ISodaIndicator<View>
        }

        mSodaIndicator!!.onInflate(mSodaBannerBeans.size)
        mAdapter!!.setLayoutResId(layoutResId)
        mAdapter!!.setBannerData(mSodaBannerBeans)
        mAdapter!!.setAutoPlay(mAutoPlay)
        mAdapter!!.setLoop(mLoop)
        mAdapter!!.setOnBannerClickListener(mOnBannerClickListener)

        mSodaViewPager = SodaViewPager(mContext!!)
        mSodaViewPager!!.setIntervalTime(mIntervalTime)
        mSodaViewPager!!.addOnPageChangeListener(this)
        mSodaViewPager!!.setAutoPlay(mAutoPlay)
        if (mScrollDuration > 0) mSodaViewPager!!.setScrollDuration(mScrollDuration)
        val layoutParams = FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.MATCH_PARENT,
            FrameLayout.LayoutParams.MATCH_PARENT
        )

        mSodaViewPager!!.adapter = mAdapter

        if ((mLoop || mAutoPlay) && mAdapter!!.getRealCount() != 0) {
            //无限轮播关键点：使第一张能反向滑动到最后一张，已达到无限滚动的效果
            val firstItem = mAdapter!!.getFirstItem()
            mSodaViewPager!!.setCurrentItem(firstItem, false)
        }

        //清除缓存view
        mBanner!!.removeAllViews()
        mBanner!!.addView(mSodaViewPager, layoutParams)
        mBanner!!.addView(mSodaIndicator!!.get(), layoutParams)
    }

    override fun setSodaIndicator(sodaIndicator: ISodaIndicator<View>) {
        this.mSodaIndicator = sodaIndicator
    }

    override fun setAutoPlay(autoPlay: Boolean) {
        mAutoPlay = autoPlay
        mAdapter?.setAutoPlay(autoPlay)
        mSodaViewPager?.setAutoPlay(autoPlay)
    }

    override fun setLoop(loop: Boolean) {
        this.mLoop = loop
    }

    override fun setIntervalTime(intervalTime: Int) {
        if (intervalTime > 0) {
            mIntervalTime = intervalTime
        }
    }

    override fun setBindAdapter(bindAdapter: IBindAdapter) {
        mAdapter?.setBindAdapter(bindAdapter)
    }

    override fun setOnPageChangeListener(onPageChangeListener: OnPageChangeListener) {
        this.mOnPageChangeListener = onPageChangeListener
    }

    override fun setOnBannerClickListener(onBannerClickListener: ISodaBanner.OnBannerClickListener) {
        this.mOnBannerClickListener = onBannerClickListener
    }

    override fun setScrollDuration(duration: Int) {
        mScrollDuration = duration
        if (mSodaViewPager != null && duration > 0) mSodaViewPager!!.setScrollDuration(duration)
    }

}