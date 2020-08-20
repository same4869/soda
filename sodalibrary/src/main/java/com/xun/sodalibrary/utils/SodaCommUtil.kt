package com.xun.sodalibrary.utils

import android.app.Application
import android.content.Context
import android.content.res.Resources
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Environment
import android.util.TypedValue
import android.view.View
import androidx.core.content.ContextCompat
import java.text.ParseException

/**
 * @Description:
 * @Author:         xwang
 * @CreateDate:     2020/7/16
 */

lateinit var APPLICATION: Application

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

//通过字符串获得颜色
fun getColorByString(colorStr: String): Int {
    return try {
        Color.parseColor(colorStr)
    } catch (e: Exception) {
        Color.parseColor("#777777")
    }
}

fun Context.getDrawable(resId: Int): Drawable? {
    return if (this.resources == null || resId <= -1) {
        ColorDrawable()
    } else ContextCompat.getDrawable(this, resId)
}

fun getCacheDirPath(): String {
    val externalStorageAvailable =
        Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED
    return if (externalStorageAvailable) {
        APPLICATION.externalCacheDir?.path ?: APPLICATION.cacheDir.path
    } else {
        APPLICATION.cacheDir?.path ?: ""
    }
}

fun getFileDirPath(): String {
    val externalStorageAvailable =
        Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED
    return if (externalStorageAvailable) {
        APPLICATION.getExternalFilesDir(null)?.path ?: APPLICATION.filesDir.path
    } else {
        APPLICATION.filesDir?.path ?: ""
    }
}
