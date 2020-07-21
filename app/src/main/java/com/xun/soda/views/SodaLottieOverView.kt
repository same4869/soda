package com.xun.soda.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import com.xun.soda.R
import com.xun.sodalibrary.log.SodaLog
import com.xun.sodaui.refresh.SodaOverView
import kotlinx.android.synthetic.main.lottie_overview.view.*

/**
 * @Description:
 * @Author:         xwang
 * @CreateDate:     2020/7/21
 */

class SodaLottieOverView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : SodaOverView(context, attrs, defStyleAttr) {
    override fun init() {
        LayoutInflater.from(context).inflate(R.layout.lottie_overview, this, true)
        pull_animation.setAnimation("loading_wave.json")
    }

    override fun onScroll(scrollY: Int, pullRefreshHeight: Int) {
        SodaLog.d("scrollY:$scrollY pullRefreshHeight:$pullRefreshHeight")
    }

    override fun onVisible() {
    }

    override fun onOver() {
    }

    override fun onRefresh() {
        pull_animation.speed = 2f
        pull_animation.playAnimation()
    }

    override fun onFinish() {
        pull_animation.progress = 0f
        pull_animation.cancelAnimation()
    }

}