package com.xun.sodaui.recyclerview.interf

interface AdapterItemView<T> {
    fun bindData(data: T, position: Int)
}