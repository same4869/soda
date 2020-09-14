package com.xun.sodaability.mock.annotation

/**
 * Created by javalong on 2018/7/8.
 */
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class MOCK(val value:String,val enable:Boolean = true)