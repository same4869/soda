package com.xun.sodaui.recyclerview.views

import android.content.Context
import android.view.View
import com.xun.sodaui.recyclerview.interf.AdapterItemView

/**
 * @Description:
 * @Author:         xwang
 * @CreateDate:     2020/8/7
 */

class SodaRvErrorView(context: Context) : View(context),
    AdapterItemView<Any> {

    override fun bindData(t: Any, position: Int) {
    }
}