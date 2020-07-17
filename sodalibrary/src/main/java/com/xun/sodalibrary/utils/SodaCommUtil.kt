package com.xun.sodalibrary.utils

import android.content.res.Resources
import android.util.TypedValue
import android.view.View

/**
 * @Description:
 * @Author:         xwang
 * @CreateDate:     2020/7/16
 */

//显示
fun View.show() {
    this.visibility = View.VISIBLE
}

//隐藏
fun View.gone() {
    this.visibility = View.GONE
}

//不可见
fun View.invisible() {
    this.visibility = View.INVISIBLE
}

//获得屏幕高度
fun getScreenHeight(): Int {
    return Resources.getSystem().displayMetrics.heightPixels
}

//获得屏幕宽度
fun getScreenWidth(): Int {
    return Resources.getSystem().displayMetrics.widthPixels
}

//dp单位转换
val Number.dp2px
    get() = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        this.toFloat(),
        Resources.getSystem().displayMetrics
    ).toInt()
