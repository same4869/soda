package com.xun.sodalibrary.utils

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import androidx.collection.ArrayMap

/**
 * @Description:
 * @Author:         xwang
 * @CreateDate:     2020/8/19
 */

object SodaSPUtils {
    enum class SpName(val innerName: String) {
        SP_TABLE_DEFAULT("global"),
        SP_TABLE_TINKER("soda_hotfix_tinker"),
        SP_TABLE_DOWNLOAD("soda_download")
    }

    private val sSPMap = ArrayMap<SpName, SharedPreferences>()

    private val context: Context = APPLICATION

    /**
     * 获取SP实例
     * @param spName sp名
     * *
     * @return [SPUtils]
     */
    fun getInstance(name: SpName = SpName.SP_TABLE_DEFAULT): SharedPreferences {
        var sp = sSPMap[name]
        if (sp == null) {
            sp = getSharedPreferences(name.innerName)
            sSPMap[name] = sp
        }
        return sp
    }

    private fun getSharedPreferences(name: String): SharedPreferences {
        return context.getSharedPreferences(name, Context.MODE_PRIVATE)
    }
}

/**
 * SP中写入String

 * @param key   键
 * *
 * @param value 值
 */
fun SharedPreferences.put(key: String, value: String) {
    this.edit().putString(key, value).apply()
}

/**
 * SP中写入String

 * @param key   键
 * *
 * @param value 值
 */
@SuppressLint("ApplySharedPref")
fun SharedPreferences.putSyn(key: String, value: String) {
    this.edit().putString(key, value).commit()
}

/**
 * SP中写入Int

 * @param key   键
 * *
 * @param value 值
 */
@SuppressLint("ApplySharedPref")
fun SharedPreferences.putSyn(key: String, value: Int) {
    this.edit().putInt(key, value).commit()
}

/**
 * SP中读取String

 * @param key          键
 * *
 * @param defaultValue 默认值
 * *
 * @return 存在返回对应值，不存在返回默认值`defaultValue`
 */
@JvmOverloads
fun SharedPreferences.getString(key: String, defaultValue: String = ""): String {
    return this.getString(key, defaultValue) ?: ""
}

/**
 * SP中写入int

 * @param key   键
 * *
 * @param value 值
 */
fun SharedPreferences.put(key: String, value: Int) {
    this.edit().putInt(key, value).apply()
}

/**
 * SP中读取int

 * @param key          键
 * *
 * @param defaultValue 默认值
 * *
 * @return 存在返回对应值，不存在返回默认值`defaultValue`
 */
@JvmOverloads
fun SharedPreferences.getInt(key: String, defaultValue: Int = -1): Int {
    return this.getInt(key, defaultValue)
}

/**
 * SP中写入long

 * @param key   键
 * *
 * @param value 值
 */
fun SharedPreferences.put(key: String, value: Long) {
    this.edit().putLong(key, value).apply()
}

/**
 * SP中读取long

 * @param key          键
 * *
 * @param defaultValue 默认值
 * *
 * @return 存在返回对应值，不存在返回默认值`defaultValue`
 */
@JvmOverloads
fun SharedPreferences.getLong(key: String, defaultValue: Long = -1L): Long {
    return this.getLong(key, defaultValue)
}

/**
 * SP中写入float

 * @param key   键
 * *
 * @param value 值
 */
fun SharedPreferences.put(key: String, value: Float) {
    this.edit().putFloat(key, value).apply()
}

/**
 * SP中读取float

 * @param key          键
 * *
 * @param defaultValue 默认值
 * *
 * @return 存在返回对应值，不存在返回默认值`defaultValue`
 */
@JvmOverloads
fun SharedPreferences.getFloat(key: String, defaultValue: Float = -1f): Float {
    return this.getFloat(key, defaultValue)
}

/**
 * SP中写入boolean

 * @param key   键
 * *
 * @param value 值
 */
fun SharedPreferences.put(key: String, value: Boolean) {
    this.edit().putBoolean(key, value).apply()
}

/**
 * SP中读取boolean

 * @param key          键
 * *
 * @param defaultValue 默认值
 * *
 * @return 存在返回对应值，不存在返回默认值`defaultValue`
 */
@JvmOverloads
fun SharedPreferences.getBoolean(key: String, defaultValue: Boolean = false): Boolean {
    return this.getBoolean(key, defaultValue)
}

/**
 * SP中写入String集合

 * @param key    键
 * *
 * @param values 值
 */
fun SharedPreferences.put(key: String, values: Set<String>) {
    this.edit().putStringSet(key, values).apply()
}

/**
 * SP中移除该key

 * @param key 键
 */
fun SharedPreferences.remove(key: String) {
    this.edit().remove(key).apply()
}

/**
 * SP中清除所有数据
 */
fun SharedPreferences.clear() {
    this.edit().clear().apply()
}
