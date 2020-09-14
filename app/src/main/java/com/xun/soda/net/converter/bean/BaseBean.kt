package com.xun.soda.net.converter.bean

/**
 * @Description:
 * @Author:         xwang
 * @CreateDate:     2020/9/14
 */

data class BaseBean<T>(
    val retcode: Int,
    val message: String,
    val data: T
)
