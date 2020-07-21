package com.xun.sodaui.refresh

import android.content.Context
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.view.animation.LinearInterpolator
import android.widget.FrameLayout
import android.widget.Scroller
import com.xun.sodalibrary.log.SodaLog

/**
 * @Description:
 * @Author:         xwang
 * @CreateDate:     2020/7/21
 */

class SodaRefreshLayout @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr), ISodaRefresh {
    private var mState = SodaOverView.RefreshState.STATE_INIT
    private var mSodaRefreshListener: ISodaRefresh.SodaRefreshListener? = null
    private var mGestureDetector: GestureDetector? = null
    protected var mSodaOverView: SodaOverView? = null
    private var mAutoScroller: AutoScroller? = null
    private var mLastY = 0

    //刷新时是否禁止滚动
    private var disableRefreshScroll = false

    private var sodaGestureDetector = object : SodaGestureDetector() {
        override fun onScroll(
            p0: MotionEvent?,
            p1: MotionEvent?,
            disX: Float,
            disY: Float
        ): Boolean {
            if (Math.abs(disX) > Math.abs(disY) || (mSodaRefreshListener != null && !mSodaRefreshListener!!.enableRefresh())) {
                //横向滑动，或刷新被禁止则不处理
                return false
            }
            if (disableRefreshScroll && mState == SodaOverView.RefreshState.STATE_REFRESH) {
                return true
            }

            val head = getChildAt(0)
            val child = findScrollableChild(this@SodaRefreshLayout)
            if (childScrolled(child)) {
                //如果列表发生了滚动则不处理
                return false
            }
            if (mSodaOverView == null) {
                return false
            }
            //没有刷新或没有达到可以刷新的距离，且头部已经划出或下拉
            return if ((mState !== SodaOverView.RefreshState.STATE_REFRESH || head.bottom <= mSodaOverView!!.mPullRefreshHeight) && (head.bottom > 0 || disY <= 0.0f)) {
                //还在滑动中
                if (mState !== SodaOverView.RefreshState.STATE_OVER_RELEASE) {
                    //阻尼计算
                    val speed: Int = (if (child.top < mSodaOverView!!.mPullRefreshHeight) {
                        (mLastY / mSodaOverView!!.minDamp)
                    } else {
                        (mLastY / mSodaOverView!!.maxDamp)
                    }).toInt()
                    //如果是正在刷新状态，则不允许在滑动的时候改变状态
                    val bool: Boolean = moveDown(speed, true)
                    mLastY = (-disY).toInt()
                    bool
                } else {
                    false
                }
            } else {
                false
            }
        }
    }

    init {
        mGestureDetector = GestureDetector(context, sodaGestureDetector)
        mAutoScroller = AutoScroller()
    }

    /**
     * 根据偏移量移动header与child
     *
     * @param offsetY 偏移量
     * @param nonAuto 是否非自动滚动触发
     * @return
     */
    private fun moveDown(offsetYArg: Int, nonAuto: Boolean): Boolean {
        if (mSodaOverView == null) {
            return false
        }
        var offsetY = offsetYArg
        SodaLog.i("changeState:$nonAuto")
        val head = getChildAt(0)
        val child = getChildAt(1)
        val childTop = child.top + offsetY

        SodaLog.i(
            "moveDown head-bottom:" + head.bottom + ",child.getTop():" + child.top + ",offsetY:" + offsetY
        )
        if (childTop <= 0) { //异常情况的补充
            SodaLog.i("childTop<=0,mState$mState")
            offsetY = -child.top
            //移动head与child的位置，到原始位置
            head.offsetTopAndBottom(offsetY)
            child.offsetTopAndBottom(offsetY)
            if (mState !== SodaOverView.RefreshState.STATE_REFRESH) {
                mState = SodaOverView.RefreshState.STATE_INIT
            }
        } else if (mState === SodaOverView.RefreshState.STATE_REFRESH && childTop > mSodaOverView!!.mPullRefreshHeight) {
            //如果正在下拉刷新中，禁止继续下拉
            return false
        } else if (childTop <= mSodaOverView!!.mPullRefreshHeight) {
            //还没超出设定的刷新距离
            if (mSodaOverView!!.getState() !== SodaOverView.RefreshState.STATE_VISIBLE && nonAuto) {
                //头部开始显示
                mSodaOverView!!.onVisible()
                mSodaOverView!!.setState(SodaOverView.RefreshState.STATE_VISIBLE)
                mState = SodaOverView.RefreshState.STATE_VISIBLE
            }
            head.offsetTopAndBottom(offsetY)
            child.offsetTopAndBottom(offsetY)
            if (childTop == mSodaOverView!!.mPullRefreshHeight && mState === SodaOverView.RefreshState.STATE_OVER_RELEASE) {
                SodaLog.i("refresh，childTop：$childTop")
                refresh()
            }
        } else {
            if (mSodaOverView!!.getState() !== SodaOverView.RefreshState.STATE_OVER && nonAuto) {
                //超出刷新位置
                mSodaOverView!!.onOver()
                mSodaOverView!!.setState(SodaOverView.RefreshState.STATE_OVER)
            }
            head.offsetTopAndBottom(offsetY)
            child.offsetTopAndBottom(offsetY)
        }
        mSodaOverView!!.onScroll(head.bottom, mSodaOverView!!.mPullRefreshHeight)
        return true
    }

    /**
     * 刷新
     */
    private fun refresh() {
        if (mSodaRefreshListener != null) {
            mState = SodaOverView.RefreshState.STATE_REFRESH
            mSodaOverView?.onRefresh()
            mSodaOverView?.setState(SodaOverView.RefreshState.STATE_REFRESH)
            mSodaRefreshListener?.onRefresh()
        }
    }

    override fun setDisableRefreshScroll(disableRefreshScroll: Boolean) {
        this.disableRefreshScroll = disableRefreshScroll
    }

    override fun refreshFinished() {
        val head = getChildAt(0)
        SodaLog.i("refreshFinished head-bottom:" + head.bottom)
        mSodaOverView?.onFinish()
        mSodaOverView?.setState(SodaOverView.RefreshState.STATE_INIT)
        val bottom = head.bottom
        if (bottom > 0) {
            //下over pull 200，height 100
            //  bottom  =100 ,height 100
            recover(bottom)
        }
        mState = SodaOverView.RefreshState.STATE_INIT
    }

    override fun setRefreshListener(sodaRefreshListener: ISodaRefresh.SodaRefreshListener?) {
        mSodaRefreshListener = sodaRefreshListener
    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        //事件分发处理
        if (!mAutoScroller!!.isFinished()) {
            return false
        }
        val head = getChildAt(0)
        if (ev.action == MotionEvent.ACTION_UP || ev.action == MotionEvent.ACTION_CANCEL || ev.action == MotionEvent.ACTION_POINTER_INDEX_MASK
        ) { //松开手
            if (head.bottom > 0) {
                if (mState !== SodaOverView.RefreshState.STATE_REFRESH) { //非正在刷新
                    recover(head.bottom)
                    return false
                }
            }
            mLastY = 0
        }
        val consumed = mGestureDetector!!.onTouchEvent(ev)
        SodaLog.i("gesture consumed：$consumed")
        if ((consumed || mState !== SodaOverView.RefreshState.STATE_INIT && mState !== SodaOverView.RefreshState.STATE_REFRESH) && head.bottom != 0) {
            ev.action = MotionEvent.ACTION_CANCEL //让父类接受不到真实的事件
            return super.dispatchTouchEvent(ev)
        }
        return if (consumed) {
            true
        } else {
            super.dispatchTouchEvent(ev)
        }
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        //定义head和child的排列位置
        val head = getChildAt(0)
        val child = getChildAt(1)
        if (head != null && child != null) {
            SodaLog.i("onLayout head-height:" + head.measuredHeight)
            val childTop = child.top
            if (mState === SodaOverView.RefreshState.STATE_REFRESH) {
                head.layout(
                    0,
                    mSodaOverView!!.mPullRefreshHeight - head.measuredHeight,
                    right,
                    mSodaOverView!!.mPullRefreshHeight
                )
                child.layout(
                    0,
                    mSodaOverView!!.mPullRefreshHeight,
                    right,
                    mSodaOverView!!.mPullRefreshHeight + child.measuredHeight
                )
            } else {
                //left,top,right,bottom
                head.layout(0, childTop - head.measuredHeight, right, childTop)
                child.layout(0, childTop, right, childTop + child.measuredHeight)
            }
            var other: View
            for (i in 2 until childCount) {
                other = getChildAt(i)
                other.layout(0, top, right, bottom)
            }
            SodaLog.i("onLayout head-bottom:" + head.bottom)
        }
    }

    /**
     * 设置下拉刷新的视图
     *
     * @param sodaOverView
     */
    override fun setRefreshOverView(sodaOverView: SodaOverView?) {
        if (mSodaOverView != null) {
            removeView(mSodaOverView)
        }
        this.mSodaOverView = sodaOverView
        val params = LayoutParams(
            LayoutParams.MATCH_PARENT,
            LayoutParams.MATCH_PARENT
        )
        addView(mSodaOverView, 0, params)
    }

    private fun recover(dis: Int) { //dis =200  200-100
        if (mSodaOverView == null) return
        if (mSodaRefreshListener != null && dis > mSodaOverView!!.mPullRefreshHeight) {
            mAutoScroller!!.recover(dis - mSodaOverView!!.mPullRefreshHeight)
            mState = SodaOverView.RefreshState.STATE_OVER_RELEASE
        } else {
            mAutoScroller!!.recover(dis)
        }
    }

    inner class AutoScroller : Runnable {
        private var mScroller: Scroller? = null
        private var mLastY = 0
        private var mIsFinished = false

        init {
            mScroller = Scroller(context, LinearInterpolator())
            mIsFinished = true
        }

        override fun run() {
            if (mScroller!!.computeScrollOffset()) {
                //还未滚动完成
                moveDown(mLastY - mScroller!!.currY, false)
                mLastY = mScroller!!.currY
                post(this)
            } else {
                removeCallbacks(this)
                mIsFinished = true
            }
        }

        fun recover(dis: Int) {
            if (dis <= 0) {
                return
            }
            removeCallbacks(this)
            mLastY = 0
            mIsFinished = false
            mScroller!!.startScroll(0, 0, 0, dis, 300)
            post(this)
        }

        fun isFinished(): Boolean {
            return mIsFinished
        }
    }

}