package com.xun.sodaui.banner.indicator

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.Gravity
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import com.xun.sodalibrary.utils.dp2px

/**
 * @Description:
 * @Author:         xwang
 * @CreateDate:     2020/7/22
 */

class SodaNumIndicator @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr), ISodaIndicator<FrameLayout> {
    /**
     * 指示点左右内间距
     */
    private val mPointLeftRightPadding = 10.dp2px

    /**
     * 指示点上下内间距
     */
    private val mPointTopBottomPadding = 10.dp2px

    override fun get(): FrameLayout = this

    override fun onInflate(count: Int) {
        removeAllViews()
        if (count <= 0) {
            return
        }

        val groupView = LinearLayout(context)
        groupView.orientation = LinearLayout.HORIZONTAL
        groupView.setPadding(0, 0, mPointLeftRightPadding, mPointTopBottomPadding)

        val indexTv = TextView(context)
        indexTv.text = "1"
        indexTv.setTextColor(Color.WHITE)
        groupView.addView(indexTv)

        val symbolTv = TextView(context)
        symbolTv.text = " / "
        symbolTv.setTextColor(Color.WHITE)
        groupView.addView(symbolTv)

        val countTv = TextView(context)
        countTv.text = count.toString()
        countTv.setTextColor(Color.WHITE)
        groupView.addView(countTv)

        val groupViewParams = LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        groupViewParams.gravity = Gravity.END or Gravity.BOTTOM
        addView(groupView, groupViewParams)
    }

    override fun onPointChange(current: Int, count: Int) {
        val viewGroup = getChildAt(0) as ViewGroup
        val indexTv = viewGroup.getChildAt(0) as TextView
        val countTv = viewGroup.getChildAt(2) as TextView
        indexTv.text = (current + 1).toString()
        countTv.text = count.toString()
    }

}