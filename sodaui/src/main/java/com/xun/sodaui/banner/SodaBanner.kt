package com.xun.sodaui.banner

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import androidx.viewpager.widget.ViewPager
import com.xun.sodaui.R
import com.xun.sodaui.banner.core.IBindAdapter
import com.xun.sodaui.banner.core.ISodaBanner
import com.xun.sodaui.banner.core.SodaBannerBean
import com.xun.sodaui.banner.core.SodaBannerDelegate
import com.xun.sodaui.banner.indicator.ISodaIndicator

/**
 * @Description:
 * @Author:         xwang
 * @CreateDate:     2020/7/21
 */

class SodaBanner @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr), ISodaBanner {
    var delegate: SodaBannerDelegate? = null

    init {
        delegate = SodaBannerDelegate(context, this)
        initCustomAttrs(context, attrs)
    }

    private fun initCustomAttrs(context: Context, attrs: AttributeSet?) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.SodaBanner)
        val autoPlay = typedArray.getBoolean(R.styleable.SodaBanner_autoPlay, true)
        val loop = typedArray.getBoolean(R.styleable.SodaBanner_loop, false)
        val intervalTime = typedArray.getInteger(R.styleable.SodaBanner_intervalTime, -1)
        setAutoPlay(autoPlay)
        setLoop(loop)
        setIntervalTime(intervalTime)
        typedArray.recycle()
    }

    override fun setBannerData(layoutResId: Int, models: List<SodaBannerBean>) {
        delegate?.setBannerData(layoutResId, models)
    }

    override fun setSodaIndicator(sodaIndicator: ISodaIndicator<View>) {
        delegate?.setSodaIndicator(sodaIndicator)
    }

    override fun setAutoPlay(autoPlay: Boolean) {
        delegate?.setAutoPlay(autoPlay)
    }

    override fun setLoop(loop: Boolean) {
        delegate?.setLoop(loop)
    }

    override fun setIntervalTime(intervalTime: Int) {
        delegate?.setIntervalTime(intervalTime)
    }

    override fun setBindAdapter(bindAdapter: IBindAdapter) {
        delegate?.setBindAdapter(bindAdapter)
    }

    override fun setOnPageChangeListener(onPageChangeListener: ViewPager.OnPageChangeListener) {
        delegate?.setOnPageChangeListener(onPageChangeListener)
    }

    override fun setOnBannerClickListener(onBannerClickListener: ISodaBanner.OnBannerClickListener) {
        delegate?.setOnBannerClickListener(onBannerClickListener)
    }

    override fun setScrollDuration(duration: Int) {
        delegate?.setScrollDuration(duration)
    }

}