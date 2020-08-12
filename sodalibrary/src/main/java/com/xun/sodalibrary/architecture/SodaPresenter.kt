package com.xun.sodalibrary.architecture

import kotlin.reflect.KClass

/**
 * @Description:
 * @Author:         xwang
 * @CreateDate:     2020/8/10
 */
interface SodaPresenter {
    /**
     * UI发送Action, Presenter可以选择处理这个事件
     * */
    fun dispatch(action: Action)

    /**
     * UI希望获得的数据状态
     * UI层应不包含任何数据, 数据由Presenter持有
     * */
    fun <T : State> getStatus(statusClass: KClass<T>): T? {
        return null
    }
}

interface Action // [SodaPage] 向 [SodaPresenter] 发出的事件

interface State //[SodaPage] 需要的数据状态