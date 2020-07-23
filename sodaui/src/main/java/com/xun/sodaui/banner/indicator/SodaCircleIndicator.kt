package com.xun.sodaui.banner.indicator

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.annotation.DrawableRes
import com.xun.sodalibrary.utils.dp2px
import com.xun.sodaui.R

/**
 * @Description:
 * @Author:         xwang
 * @CreateDate:     2020/7/22
 */

class SodaCircleIndicator @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr), ISodaIndicator<FrameLayout> {
    /**
     * 正常状态下的指示点
     */
    @DrawableRes
    private val mPointNormal = R.drawable.shape_point_normal

    /**
     * 选中状态下的指示点
     */
    @DrawableRes
    private val mPointSelected = R.drawable.shape_point_select

    /**
     * 指示点左右内间距
     */
    private var mPointLeftRightPadding = 0

    /**
     * 指示点上下内间距
     */
    private var mPointTopBottomPadding = 0

    init {
        mPointLeftRightPadding = 5.dp2px
        mPointTopBottomPadding = 15.dp2px
    }

    override fun get(): FrameLayout {
        return this
    }

    override fun onInflate(count: Int) {
        removeAllViews()
        if (count <= 0) {
            return
        }
        val groupView = LinearLayout(context)
        groupView.orientation = LinearLayout.HORIZONTAL
        var imageView: ImageView
        val imageViewParams =
            LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
        imageViewParams.gravity = Gravity.CENTER_VERTICAL
        imageViewParams.setMargins(
            mPointLeftRightPadding,
            mPointTopBottomPadding,
            mPointLeftRightPadding,
            mPointTopBottomPadding
        )
        for (i in 0 until count) {
            imageView = ImageView(context)
            imageView.layoutParams = imageViewParams
            if (i == 0) {
                imageView.setImageResource(mPointSelected)
            } else {
                imageView.setImageResource(mPointNormal)
            }
            groupView.addView(imageView)
        }
        val groupViewParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
        groupViewParams.gravity = Gravity.CENTER or Gravity.BOTTOM
        addView(groupView, groupViewParams)

    }

    override fun onPointChange(current: Int, count: Int) {
        val viewGroup = getChildAt(0) as ViewGroup
        for (i in 0 until viewGroup.childCount) {
            val imageView = viewGroup.getChildAt(i) as ImageView
            if (i == current) {
                imageView.setImageResource(mPointSelected)
            } else {
                imageView.setImageResource(mPointNormal)
            }
            imageView.requestLayout()
        }
    }

}