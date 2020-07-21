package com.xun.sodaui.refresh

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import com.xun.sodalibrary.utils.dp2px

/**
 * @Description:
 * @Author:         xwang
 * @CreateDate:     2020/7/21
 */

abstract class SodaOverView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    enum class RefreshState {
        /**
         * 初始态
         */
        STATE_INIT,

        /**
         * Header展示的状态
         */
        STATE_VISIBLE,

        /**
         * 超出可刷新距离的状态
         */
        STATE_OVER,

        /**
         * 刷新中的状态
         */
        STATE_REFRESH,

        /**
         * 超出刷新位置松开手后的状态
         */
        STATE_OVER_RELEASE
    }

    protected var mState = RefreshState.STATE_INIT

    /**
     * 触发下拉刷新 需要的最小高度
     */
    var mPullRefreshHeight = 66.dp2px

    /**
     * 最小阻尼
     */
    var minDamp = 1.6f

    /**
     * 最大阻尼
     */
    var maxDamp = 2.2f

    init {
        init()
    }

    fun setPullRefreshHeight(pullRefreshHeight: Int) {
        mPullRefreshHeight = pullRefreshHeight
    }

    /**
     * 初始化
     */
    abstract fun init()

    abstract fun onScroll(scrollY: Int, pullRefreshHeight: Int)

    /**
     * 显示Overlay
     */
    abstract fun onVisible()

    /**
     * 超过Overlay，释放就会加载
     */
    abstract fun onOver()

    /**
     * 开始加载
     */
    abstract fun onRefresh()

    /**
     * 加载完成
     */
    abstract fun onFinish()

    /**
     * 设置状态
     *
     * @param state 状态
     */
    open fun setState(state: RefreshState) {
        mState = state
    }

    /**
     * 获取状态
     *
     * @return 状态
     */
    open fun getState(): RefreshState? {
        return mState
    }
}