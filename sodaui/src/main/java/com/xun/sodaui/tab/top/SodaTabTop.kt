package com.xun.sodaui.tab.top

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.RelativeLayout
import android.widget.TextView
import com.xun.sodalibrary.utils.getColorByString
import com.xun.sodalibrary.utils.gone
import com.xun.sodalibrary.utils.show
import com.xun.sodaui.R
import com.xun.sodaui.tab.comm.ISodaTab
import kotlinx.android.synthetic.main.soda_tab_top.view.*

/**
 * @Description:
 * @Author:         xwang
 * @CreateDate:     2020/7/20
 */

class SodaTabTop @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : RelativeLayout(context, attrs, defStyleAttr), ISodaTab<SodaTabTopInfo> {
    private var tabInfo: SodaTabTopInfo? = null

    init {
        LayoutInflater.from(context).inflate(R.layout.soda_tab_top, this)
    }

    fun getTabNameView(): TextView {
        return mTabNameView
    }

    fun getSodaTabInfo(): SodaTabTopInfo? {
        return tabInfo
    }

    override fun setSodaTabInfo(data: SodaTabTopInfo) {
        this.tabInfo = data
        inflateInfo(selected = false, init = true)
    }

    private fun inflateInfo(selected: Boolean, init: Boolean) {
        if (tabInfo == null) {
            return
        }
        when (tabInfo!!.tabType) {
            SodaTabTopInfo.TabType.TXT -> {
                if (init) {
                    mTabNameView.show()
                    mTabImageView.gone()
                    if (tabInfo!!.name.isNotBlank()) {
                        mTabNameView.text = tabInfo!!.name
                    }
                }
                if (selected) {
                    mIndicator.show()
                    mTabNameView.setTextColor(getColorByString(tabInfo!!.tintColor))
                } else {
                    mIndicator.gone()
                    mTabNameView.setTextColor(getColorByString(tabInfo!!.defaultColor))
                }
            }
            SodaTabTopInfo.TabType.BITMAP -> {
                if (init) {
                    mTabImageView.show()
                    mTabNameView.gone()
                }
                if (selected) {
                    mIndicator.show()
                    mTabImageView.setImageBitmap(tabInfo!!.selectedBitmap)
                } else {
                    mIndicator.gone()
                    mTabImageView.setImageBitmap(tabInfo!!.defaultBitmap)
                }
            }
        }
    }

    override fun resetHeight(height: Int) {
        val layoutParams = layoutParams
        layoutParams.height = height
        setLayoutParams(layoutParams)
        getTabNameView().visibility = View.GONE
    }

    override fun onTabSelectedChange(
        index: Int,
        prevInfo: SodaTabTopInfo?,
        nextInfo: SodaTabTopInfo
    ) {
        if (prevInfo !== tabInfo && nextInfo !== tabInfo || prevInfo === nextInfo) {
            return
        }
        if (prevInfo === tabInfo) {
            inflateInfo(selected = false, init = false)
        } else {
            inflateInfo(selected = true, init = false)
        }
    }

}