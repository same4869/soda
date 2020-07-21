package com.xun.sodaui.refresh

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.animation.AnimationUtils
import android.view.animation.LinearInterpolator
import com.xun.sodalibrary.log.SodaLog
import com.xun.sodalibrary.utils.dp2px
import com.xun.sodaui.R
import kotlinx.android.synthetic.main.soda_refresh_overview.view.*

/**
 * @Description:
 * @Author:         xwang
 * @CreateDate:     2020/7/21
 */

class SodaTextOverView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : SodaOverView(context, attrs, defStyleAttr) {
    override fun init() {
        LayoutInflater.from(context).inflate(R.layout.soda_refresh_overview, this, true)
        setPullRefreshHeight(80.dp2px)
    }

    override fun onScroll(scrollY: Int, pullRefreshHeight: Int) {
        SodaLog.d("onScroll")
    }

    override fun onVisible() {
        SodaLog.d("onVisible")
        mText.text = "下拉刷新"
    }

    override fun onOver() {
        SodaLog.d("onOver")
        mText.text = "松开刷新"
    }

    override fun onRefresh() {
        SodaLog.d("onRefresh")
        val operatingAnim =
            AnimationUtils.loadAnimation(context, R.anim.rotate_anim)
        val lin = LinearInterpolator()
        operatingAnim.interpolator = lin
        mRotateView.startAnimation(operatingAnim)
    }

    override fun onFinish() {
        SodaLog.d("onFinish")
        mRotateView.clearAnimation()
    }

}