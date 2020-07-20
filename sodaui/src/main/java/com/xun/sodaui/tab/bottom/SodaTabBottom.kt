package com.xun.sodaui.tab.bottom

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.RelativeLayout
import com.xun.sodalibrary.utils.getColorByString
import com.xun.sodalibrary.utils.gone
import com.xun.sodalibrary.utils.show
import com.xun.sodaui.R
import com.xun.sodaui.tab.comm.ISodaTab
import kotlinx.android.synthetic.main.soda_tab_bottom.view.*

/**
 * @Description:
 * @Author:         xwang
 * @CreateDate:     2020/7/20
 */

class SodaTabBottom @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : RelativeLayout(context, attrs, defStyleAttr), ISodaTab<SodaTabBottomInfo> {
    private var tabInfo: SodaTabBottomInfo? = null

    init {
        LayoutInflater.from(context).inflate(R.layout.soda_tab_bottom, this)
    }

    fun getSodaTabInfo(): SodaTabBottomInfo? {
        return tabInfo
    }

    override fun setSodaTabInfo(data: SodaTabBottomInfo) {
        this.tabInfo = data
        inflateInfo(selected = false, init = true)
    }

    private fun inflateInfo(selected: Boolean, init: Boolean) {
        if (tabInfo == null) {
            return
        }
        when (tabInfo!!.tabType) {
            SodaTabBottomInfo.TabType.ICON -> {
                if (init) {
                    mTabImageView.gone()
                    mTabIconView.show()
                    val typeface = Typeface.createFromAsset(context.assets, tabInfo!!.iconFont)
                    mTabIconView.typeface = typeface
                    if (tabInfo!!.name.isNotBlank()) {
                        mTabNameView.text = tabInfo!!.name
                    }
                }

                if (selected) {
                    mTabIconView.text = if (tabInfo!!.selectedIconName.isNotBlank()) {
                        tabInfo!!.selectedIconName
                    } else {
                        tabInfo!!.defaultIconName
                    }
                    mTabIconView.setTextColor(getColorByString(tabInfo!!.tintColor))
                    mTabNameView.setTextColor(getColorByString(tabInfo!!.tintColor))
                } else {
                    mTabIconView.text = tabInfo!!.defaultIconName
                    mTabIconView.setTextColor(getColorByString(tabInfo!!.defaultColor))
                    mTabNameView.setTextColor(getColorByString(tabInfo!!.defaultColor))
                }
            }
            SodaTabBottomInfo.TabType.BITMAP -> {
                if (init) {
                    mTabImageView.show()
                    mTabIconView.gone()
                    if (tabInfo!!.name.isNotBlank()) {
                        mTabNameView.text = tabInfo!!.name
                    }
                }
                if (selected) {
                    mTabImageView.setImageBitmap(tabInfo!!.selectedBitmap)
                } else {
                    mTabImageView.setImageBitmap(tabInfo!!.defaultBitmap)
                }
            }
        }
    }

    override fun resetHeight(height: Int) {
        val layoutParams = layoutParams
        layoutParams.height = height
        setLayoutParams(layoutParams)
    }

    override fun onTabSelectedChange(
        index: Int,
        prevInfo: SodaTabBottomInfo?,
        nextInfo: SodaTabBottomInfo
    ) {
        if (prevInfo != tabInfo && nextInfo != tabInfo || prevInfo == nextInfo) {
            return
        }
        if (prevInfo == tabInfo) {
            inflateInfo(selected = false, init = false)
        } else {
            inflateInfo(selected = true, init = false)
        }
    }

}