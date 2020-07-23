package com.xun.sodaui.banner.core

import android.view.View

interface IBindAdapter {
    fun onBind(
        viewHolder: View, bean: SodaBannerBean, position: Int
    )
}