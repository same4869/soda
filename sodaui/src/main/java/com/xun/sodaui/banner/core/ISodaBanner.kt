package com.xun.sodaui.banner.core

import android.view.View
import androidx.annotation.LayoutRes
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import com.xun.sodaui.banner.indicator.ISodaIndicator as ISodaIndicator

/**
 * @Description:
 * @Author:         xwang
 * @CreateDate:     2020/7/21
 */

interface ISodaBanner {
    fun setBannerData(@LayoutRes layoutResId: Int = 0, models: List<SodaBannerBean>)

    fun setSodaIndicator(sodaIndicator: ISodaIndicator<View>)

    fun setAutoPlay(autoPlay: Boolean)

    fun setLoop(loop: Boolean)

    fun setIntervalTime(intervalTime: Int)

    fun setBindAdapter(bindAdapter: IBindAdapter)

    fun setOnPageChangeListener(onPageChangeListener: OnPageChangeListener)

    fun setOnBannerClickListener(onBannerClickListener: OnBannerClickListener)

    fun setScrollDuration(duration: Int)


    interface OnBannerClickListener {
        fun onBannerClick(viewHolder: View, bannerMo: SodaBannerBean, position: Int)
    }
}