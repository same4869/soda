package com.xun.soda.adapter

import android.content.Context
import com.xun.soda.bean.PersonBean
import com.xun.soda.bean.PersonBean2
import com.xun.soda.views.PersonView
import com.xun.soda.views.PersonView2
import com.xun.sodaui.recyclerview.adapter.SodaBaseAdapter
import com.xun.sodaui.recyclerview.interf.AdapterItemView
import com.xun.sodaui.recyclerview.interf.AdapterUIMappingProtocol.Companion.ERROR_ITEM_TYPE
import com.xun.sodaui.recyclerview.views.SodaRvErrorView

/**
 * @Description:
 * @Author:         xwang
 * @CreateDate:     2020/8/7
 */

class PersonAdapter(
    data: MutableList<Any>,
    private val mContext: Context
) : SodaBaseAdapter<Any>(data) {
    private val TYPE_PERSON_1 = 1
    private val TYPE_PERSON_2 = 2

    override fun getItemType(data: Any): Int {
        return when (data) {
            is PersonBean -> TYPE_PERSON_1
            is PersonBean2 -> TYPE_PERSON_2
            else -> ERROR_ITEM_TYPE
        }
    }

    override fun createItem(type: Int): AdapterItemView<*>? {
        return when (type) {
            TYPE_PERSON_1 -> PersonView(mContext)
            TYPE_PERSON_2 -> PersonView2(mContext)
            else -> SodaRvErrorView(mContext)
        }
    }
}