package com.xun.sodaui.tab.bottom

import android.graphics.Bitmap

/**
 * @Description:
 * @Author:         xwang
 * @CreateDate:     2020/7/20
 */

class SodaTabBottomInfo {
    enum class TabType {
        BITMAP, //图片文字模式
        ICON    //图标字体模式
    }

    var name: String = ""
    var defaultBitmap: Bitmap? = null
    var selectedBitmap: Bitmap? = null
    var iconFont: String = ""           //图标字体模式时，字体路径名称
    var defaultIconName: String = ""    //图标字体模式时，未选中时的字体
    var selectedIconName: String = ""   //图标字体模式时，选中时的字体
    var defaultColor: String = ""     //两种模式默认字体颜色，图标字体模式下也会影响到图标
    var tintColor: String = ""        //两种模式选中字体颜色，图标字体模式下也会影响到图标
    var tabType: TabType? = null

    constructor(
        name: String,
        iconFont: String,
        defaultIconName: String,
        selectedIconName: String,
        defaultColor: String,
        tintColor: String
    ) {
        this.name = name
        this.iconFont = iconFont
        this.defaultIconName = defaultIconName
        this.selectedIconName = selectedIconName
        this.defaultColor = defaultColor
        this.tintColor = tintColor
        this.tabType = TabType.ICON
    }

    constructor(name: String, defaultBitmap: Bitmap?, selectedBitmap: Bitmap?) {
        this.name = name
        this.defaultBitmap = defaultBitmap
        this.selectedBitmap = selectedBitmap
        this.tabType = TabType.BITMAP
    }


}