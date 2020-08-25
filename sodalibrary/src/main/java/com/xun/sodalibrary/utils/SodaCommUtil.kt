package com.xun.sodalibrary.utils

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.content.pm.PackageManager
import android.content.res.Resources
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Environment
import android.provider.Settings
import android.telephony.TelephonyManager
import android.text.TextUtils
import android.util.TypedValue
import android.view.View
import androidx.core.content.ContextCompat
import java.io.UnsupportedEncodingException
import java.text.ParseException
import java.util.*

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

fun getAppVersionCode(): String {
    val manager = APPLICATION.packageManager
    var name = ""
    try {
        val info =
            manager.getPackageInfo(APPLICATION.packageName, 0)
        name = info.versionName
    } catch (e: PackageManager.NameNotFoundException) {
        e.printStackTrace()
    }
    return name
}

@SuppressLint("HardwareIds")
fun getAndroidId(): String {
    return Settings.Secure.getString(
        APPLICATION.contentResolver,
        Settings.Secure.ANDROID_ID
    )
}

@SuppressLint("MissingPermission", "HardwareIds")
fun getIMEIId(): String {
    var deviceId = ""
    try {
        if (ContextCompat.checkSelfPermission(
                APPLICATION,
                "android.permission.READ_PHONE_STATE"
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            val telephony =
                APPLICATION.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
            if (telephony != null) {
                deviceId = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    telephony.imei
                } else {
                    telephony.deviceId
                }
            }
        }
    } catch (var3: Exception) {
        var3.printStackTrace()
    }
    return deviceId
}

private var uuid: UUID? = null
private const val PREFS_DEVICE_ID = "device_id"
private const val PREFS_FILE = "pre_device.xml"
fun getDeviceId(): String {
    if (uuid == null) {
        synchronized(Objects::class.java) {
            if (uuid == null) {
                val prefs = APPLICATION.getSharedPreferences(PREFS_FILE, 0)
                val id = prefs.getString(PREFS_DEVICE_ID, null)
                if (id != null) { // Use the ids previously computed and stored in the prefs file
                    uuid = UUID.fromString(id)
                } else {
                    val androidId = getAndroidId()
                    try {
                        uuid =
                            if (!TextUtils.isEmpty(androidId) && "9774d56d682e549c" != androidId) {
                                UUID.nameUUIDFromBytes(
                                    androidId.toByteArray(charset("utf8"))
                                )
                            } else {
                                val deviceId = getIMEIId()
                                if (!TextUtils.equals(deviceId, "unknow")
                                ) UUID.nameUUIDFromBytes(
                                    deviceId.toByteArray(charset("utf8"))
                                ) else UUID.randomUUID()
                            }
                    } catch (e: UnsupportedEncodingException) {
                        e.printStackTrace()
                    }
                    prefs.edit().putString(PREFS_DEVICE_ID, uuid.toString()).apply()
                }
            }
        }
    }
    return uuid.toString()
}
