package com.xun.soda.net

/**
 * @Description:
 * @Author:         xwang
 * @CreateDate:     2020/9/1
 */

object ApiType {
    const val KEY_HEADER = "urlname"

    const val HEADER_API_TAKUMI = "urlname:API_TAKUMI"
    const val HEADER_API_LOGIN = "urlname:API_LOGIN"

    fun getType(s: String): Type {
        return Type.valueOf(s)
    }

    enum class Type {
        API_TAKUMI,
        API_LOGIN
    }
}