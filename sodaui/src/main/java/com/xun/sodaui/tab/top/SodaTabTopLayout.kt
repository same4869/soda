package com.xun.sodaui.tab.top

import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import android.widget.HorizontalScrollView
import android.widget.LinearLayout
import androidx.viewpager.widget.ViewPager
import com.xun.sodalibrary.utils.getScreenWidth
import com.xun.sodaui.tab.comm.ISodaTabLayout

/**
 * @Description:
 * @Author:         xwang
 * @CreateDate:     2020/7/20
 */

class SodaTabTopLayout @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : HorizontalScrollView(context, attrs, defStyleAttr), ISodaTabLayout<SodaTabTop, SodaTabTopInfo>,
    ViewPager.OnPageChangeListener {
    private val tabSelectedChangeListeners: MutableList<ISodaTabLayout.OnTabSelectedListener<SodaTabTopInfo>> =
        mutableListOf()
    private var selectedInfo: SodaTabTopInfo? = null
    private var infoList: List<SodaTabTopInfo> = listOf()
    private var tabWith: Int = 0

    override fun findTab(data: SodaTabTopInfo): SodaTabTop? {
        val ll = getRootLayout()
        for (i in 0 until ll.childCount) {
            val child = ll.getChildAt(i)
            if (child is SodaTabTop && child.getSodaTabInfo() == data) {
                return child
            }
        }
        return null
    }

    private fun getRootLayout(clear: Boolean = false): LinearLayout {
        val theRootView: LinearLayout?
        if (childCount == 0) {
            theRootView = LinearLayout(context).apply {
                orientation = LinearLayout.HORIZONTAL
            }
            theRootView.let {
                val layoutParams = LayoutParams(
                    LayoutParams.MATCH_PARENT,
                    LayoutParams.WRAP_CONTENT
                )
                addView(theRootView, layoutParams)
            }
        } else {
            theRootView = getChildAt(0) as LinearLayout
            if (clear) {
                theRootView.removeAllViews()
            }
        }
        return theRootView
    }

    override fun addTabSelectedChangeListener(listener: ISodaTabLayout.OnTabSelectedListener<SodaTabTopInfo>) {
        tabSelectedChangeListeners.add(listener)
    }

    override fun defaultSelected(defaultInfo: SodaTabTopInfo) {
        onSelected(defaultInfo)
    }

    private fun onSelected(nextInfo: SodaTabTopInfo) {
        for (i in 0 until tabSelectedChangeListeners.size) {
            tabSelectedChangeListeners[i].onTabSelectedChange(
                infoList.indexOf(nextInfo),
                selectedInfo,
                nextInfo
            )
        }
        this.selectedInfo = nextInfo
        autoScroll(nextInfo)
    }

    override fun inflateInfo(infoList: List<SodaTabTopInfo>) {
        if (infoList.isEmpty()) {
            return
        }
        this.infoList = infoList
        val linearLayout = getRootLayout(true)
        selectedInfo = null
        tabSelectedChangeListeners.clear()
        for (i in infoList.indices) {
            val info = infoList.get(i)
            val tab = SodaTabTop(context)
            tabSelectedChangeListeners.add(tab)
            tab.setSodaTabInfo(info)
            linearLayout.addView(tab)
            tab.setOnClickListener { onSelected(info) }
        }

    }

    /**
     * 自动滚动，实现点击的位置能够自动滚动以展示前后2个
     *
     * @param nextInfo 点击tab的info
     */
    private fun autoScroll(nextInfo: SodaTabTopInfo) {
        val tabTop = findTab(nextInfo) ?: return
        val index = infoList!!.indexOf(nextInfo)
        val loc = IntArray(2)
        //获取点击的控件在屏幕的位置
        tabTop.getLocationInWindow(loc)
        val scrollWidth: Int
        if (tabWith == 0) {
            tabWith = tabTop.width
        }
        //判断点击了屏幕左侧还是右侧
        scrollWidth = if (loc[0] + tabWith / 2 > getScreenWidth() / 2) {
            rangeScrollWidth(index, 2)
        } else {
            rangeScrollWidth(index, -2)
        }
        scrollTo(scrollX + scrollWidth, 0)
    }

    /**
     * 获取可滚动的范围
     *
     * @param index 从第几个开始
     * @param range 向前向后的范围
     * @return 可滚动的范围
     */
    private fun rangeScrollWidth(index: Int, range: Int): Int {
        var scrollWidth = 0
        for (i in 0..Math.abs(range)) {
            val next: Int = if (range < 0) {
                range + i + index
            } else {
                range - i + index
            }
            if (next >= 0 && next < infoList.size) {
                if (range < 0) {
                    scrollWidth -= scrollWidth(next, false)
                } else {
                    scrollWidth += scrollWidth(next, true)
                }
            }
        }
        return scrollWidth
    }

    /**
     * 指定位置的控件可滚动的距离
     *
     * @param index   指定位置的控件
     * @param toRight 是否是点击了屏幕右侧
     * @return 可滚动的距离
     */
    private fun scrollWidth(index: Int, toRight: Boolean): Int {
        val target = findTab(infoList!![index]) ?: return 0
        val rect = Rect()
        target.getLocalVisibleRect(rect)
        return if (toRight) { //点击屏幕右侧
            if (rect.right > tabWith) { //right坐标大于控件的宽度时，说明完全没有显示
                tabWith
            } else { //显示部分，减去已显示的宽度
                tabWith - rect.right
            }
        } else {
            if (rect.left <= -tabWith) { //left坐标小于等于-控件的宽度，说明完全没有显示
                return tabWith
            } else if (rect.left > 0) { //显示部分
                return rect.left
            }
            0
        }
    }

    fun setViewPager(vp: ViewPager, currentIndex: Int = 0){
        if (vp.adapter == null) return
        if(vp.adapter!!.count!= infoList.size) return
        vp.addOnPageChangeListener(this)
        onSelected(infoList[currentIndex])
    }

    override fun onPageScrollStateChanged(state: Int) {
    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
    }

    override fun onPageSelected(position: Int) {
        onSelected(infoList[position])
    }

}