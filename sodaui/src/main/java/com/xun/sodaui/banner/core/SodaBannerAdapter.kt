package com.xun.sodaui.banner.core

import android.content.Context
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.viewpager.widget.PagerAdapter

class SodaBannerAdapter(context: Context) : PagerAdapter() {
    private var mContext: Context? = null
    private var models: List<SodaBannerBean>? = null
    private val mCachedViews: SparseArray<View> = SparseArray()
    private var mBannerClickListener: ISodaBanner.OnBannerClickListener? = null
    private var mBindAdapter: IBindAdapter? = null

    /**
     * 是否开启自动轮,该模式下一定可以循环切换
     */
    private var mAutoPlay = true

    /**
     * 非自动轮播状态下是否可以循环切换
     */
    private var mLoop = false
    private var mLayoutResId = -1

    init {
        mContext = context
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean = view == `object`

    override fun getCount(): Int {
        return if (mAutoPlay || mLoop) {
            Int.MAX_VALUE
        } else {
            getRealCount()
        }
    }

    /**
     * 获取初次展示的item位置
     *
     * @return
     */
    fun getFirstItem(): Int {
        return Int.MAX_VALUE / 2 - Int.MAX_VALUE / 2 % getRealCount()
    }

    fun getRealCount(): Int {
        return models?.size ?: 0
    }

    fun setAutoPlay(autoPlay: Boolean) {
        mAutoPlay = autoPlay
    }

    fun setLoop(loop: Boolean) {
        mLoop = loop
    }

    fun setOnBannerClickListener(OnBannerClickListener: ISodaBanner.OnBannerClickListener?) {
        mBannerClickListener = OnBannerClickListener
    }

    fun setBindAdapter(bindAdapter: IBindAdapter) {
        mBindAdapter = bindAdapter
    }

    fun setLayoutResId(@LayoutRes layoutResId: Int) {
        mLayoutResId = layoutResId
    }

    fun setBannerData(models: List<SodaBannerBean>) {
        this.models = models
        initCachedView()
        notifyDataSetChanged()
    }

    private fun initCachedView() {
        models?.forEachIndexed { index, _ ->
            mCachedViews.put(index, createView(LayoutInflater.from(mContext)))
        }
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        var realPosition = position
        if (getRealCount() > 0) {
            realPosition = position % getRealCount()
        }
        val viewHolder = mCachedViews.get(realPosition)
        if (viewHolder.parent != null) {
            (viewHolder.parent as ViewGroup).removeView(viewHolder)
        }
        onBind(viewHolder, models?.get(realPosition)!!, realPosition)
        container.addView(viewHolder)
        return viewHolder
    }

    protected fun onBind(viewHolder: View, bannerBean: SodaBannerBean, position: Int) {
        viewHolder.setOnClickListener {
            mBannerClickListener?.onBannerClick(viewHolder, bannerBean, position)
        }
        mBindAdapter?.onBind(viewHolder, bannerBean, position)
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
    }

    private fun createView(layoutInflater: LayoutInflater): View {
        require(mLayoutResId != -1) { "you must be set setLayoutResId first" }
        return layoutInflater.inflate(mLayoutResId, null, false)
    }

}