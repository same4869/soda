package com.xun.soda.views

import android.content.Context
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.xun.soda.R
import com.xun.soda.recyclerview.PersonBean
import com.xun.sodaui.recyclerview.interf.AdapterItemView
import kotlinx.android.synthetic.main.view_recycler_person_item.view.*

/**
 * @Description:
 * @Author:         xwang
 * @CreateDate:     2020/8/7
 */

class PersonView(context: Context) : LinearLayout(context), AdapterItemView<PersonBean> {
    init {
        LayoutInflater.from(context)
            .inflate(R.layout.view_recycler_person_item, this)
        layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
        orientation = VERTICAL
    }

    override fun bindData(data: PersonBean, position: Int) {
        mNameTv.text = data.name
        mSexTv.text = data.sex.toString()
        mContentTv.text = data.content + "position:$position"
    }
}