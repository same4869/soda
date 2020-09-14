package com.xun.soda.net.converter.exception

/**
 * @Description:
 * @Author:         xwang
 * @CreateDate:     2020/8/31
 */

class ApiException(val errorCode: Int,val errorMessage: String = "",val passErrorCode :Int= 0) : RuntimeException(errorMessage)