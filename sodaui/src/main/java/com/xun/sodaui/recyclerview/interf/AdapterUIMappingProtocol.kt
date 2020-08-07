package com.xun.sodaui.recyclerview.interf

interface AdapterUIMappingProtocol<T> {
    companion object {
        const val ERROR_ITEM_TYPE = -1
    }

    //数据 ——> Type
    fun getItemType(data: T): Int

    // Type -> View
    fun createItem(type: Int): AdapterItemView<*>?
}