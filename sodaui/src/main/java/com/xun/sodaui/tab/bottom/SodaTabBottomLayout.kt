package com.xun.sodaui.tab.bottom

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.xun.sodalibrary.utils.dp2px
import com.xun.sodalibrary.utils.getScreenWidth
import com.xun.sodaui.R
import com.xun.sodaui.tab.comm.ISodaTabLayout

/**
 * @Description:
 * @Author:         xwang
 * @CreateDate:     2020/7/20
 */

class SodaTabBottomLayout @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr), ISodaTabLayout<SodaTabBottom, SodaTabBottomInfo> {
    companion object {
        private const val TAG_TAB_BOTTOM = "TAG_TAB_BOTTOM"
    }

    private val tabSelectedChangeListeners: MutableList<ISodaTabLayout.OnTabSelectedListener<SodaTabBottomInfo>> =
        mutableListOf()
    private var infoList: List<SodaTabBottomInfo> = listOf()
    private var selectedInfo: SodaTabBottomInfo? = null

    //TabBottom高度
    private var tabBottomHeight = 50

    //TabBottom的头部线条高度
    private val bottomLineHeight = 0.5f

    //TabBottom的头部线条颜色
    private val bottomLineColor = "#dfe0e1"
    private var bottomAlpha = 1f

    fun setTabAlpha(alpha: Float) {
        bottomAlpha = alpha
    }

    override fun findTab(data: SodaTabBottomInfo): SodaTabBottom? {
        val ll = findViewWithTag<ViewGroup>(TAG_TAB_BOTTOM)
        for (i in 0 until ll.childCount) {
            val child = ll.getChildAt(i)
            if (child is SodaTabBottom && child.getSodaTabInfo() == data) {
                return child
            }
        }
        return null
    }

    override fun addTabSelectedChangeListener(listener: ISodaTabLayout.OnTabSelectedListener<SodaTabBottomInfo>) {
        tabSelectedChangeListeners.add(listener)
    }

    override fun defaultSelected(defaultInfo: SodaTabBottomInfo) {
        onSelected(defaultInfo)
    }

    override fun inflateInfo(infoList: List<SodaTabBottomInfo>) {
        if (infoList.isEmpty()) {
            return
        }
        this.infoList = infoList
        removeAllViews()

        addBackground()

        tabSelectedChangeListeners.clear()
        val fl = FrameLayout(context)
        val height = tabBottomHeight.dp2px
        val width = getScreenWidth() / infoList.size
        fl.tag = TAG_TAB_BOTTOM
        for (i in infoList.indices) {
            val info = infoList[i]
            val params = LayoutParams(width, height)
            params.gravity = Gravity.BOTTOM
            params.leftMargin = i * width

            val tabBottom = SodaTabBottom(context)
            tabSelectedChangeListeners.add(tabBottom)
            tabBottom.setSodaTabInfo(info)
            fl.addView(tabBottom, params)
            tabBottom.setOnClickListener {
                onSelected(info)
            }
        }

        val flPrams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
        flPrams.gravity = Gravity.BOTTOM
        addBottomLine()
        addView(fl, flPrams)

//        fixContentView()
    }

    private fun onSelected(nextInfo: SodaTabBottomInfo) {
        tabSelectedChangeListeners.forEach {
            it.onTabSelectedChange(infoList.indexOf(nextInfo), selectedInfo, nextInfo)
        }
        this.selectedInfo = nextInfo
    }

    private fun addBottomLine() {
        val bottomLine = View(context)
        bottomLine.setBackgroundColor(Color.parseColor(bottomLineColor))
        val bottomLineParams = LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            bottomLineHeight.dp2px
        )
        bottomLineParams.gravity = Gravity.BOTTOM
        bottomLineParams.bottomMargin = (tabBottomHeight - bottomLineHeight).dp2px
        addView(bottomLine, bottomLineParams)
        bottomLine.alpha = bottomAlpha
    }

    private fun addBackground() {
        val view: View = View(context).apply {
            background = context.getDrawable(R.color.white)
        }
        val params = LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            tabBottomHeight.dp2px
        )
        params.gravity = Gravity.BOTTOM
        addView(view, params)
        view.alpha = bottomAlpha
    }

}