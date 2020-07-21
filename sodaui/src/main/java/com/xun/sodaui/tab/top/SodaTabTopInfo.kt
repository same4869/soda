package com.xun.sodaui.tab.top

import android.graphics.Bitmap

/**
 * @Description:
 * @Author:         xwang
 * @CreateDate:     2020/7/20
 */

class SodaTabTopInfo {
    enum class TabType {
        BITMAP, TXT
    }

    var name: String = ""
    var defaultBitmap: Bitmap? = null
    var selectedBitmap: Bitmap? = null
    var defaultColor: String = ""     //两种模式默认字体颜色，图标字体模式下也会影响到图标
    var tintColor: String = ""        //两种模式选中字体颜色，图标字体模式下也会影响到图标
    var tabType: TabType? = null

    constructor(name: String, defaultBitmap: Bitmap?, selectedBitmap: Bitmap?) {
        this.name = name
        this.defaultBitmap = defaultBitmap
        this.selectedBitmap = selectedBitmap
        this.tabType = TabType.BITMAP
    }

    constructor(name: String, defaultColor: String, tintColor: String) {
        this.name = name
        this.defaultColor = defaultColor
        this.tintColor = tintColor
        this.tabType = TabType.TXT
    }


}