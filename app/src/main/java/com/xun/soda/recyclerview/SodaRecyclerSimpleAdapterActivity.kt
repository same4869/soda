package com.xun.soda.recyclerview

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.xun.soda.R
import com.xun.soda.views.PersonView
import com.xun.soda.views.PersonView2
import com.xun.sodaui.recyclerview.adapter.SodaSimpleAdapter
import kotlinx.android.synthetic.main.activity_soda_recycler_base_adapter.*

class SodaRecyclerSimpleAdapterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_soda_recycler_base_adapter)

        val adapter = SodaSimpleAdapter<Any>(applicationContext).apply {
            registerMapping(PersonBean::class.java, PersonView::class.java)
            registerMapping(PersonBean2::class.java, PersonView2::class.java)
        }

        mRecyclerView.layoutManager = LinearLayoutManager(applicationContext)
        mRecyclerView.adapter = adapter
        adapter.data.addAll(initData())

        adapter.addHeaderView(ImageView(applicationContext).apply {
            setImageResource(R.mipmap.ic_launcher)
        })

        adapter.addFooterView(TextView(applicationContext).apply {
            text = "我是一个footer"
        })
    }

    private fun initData(): List<Any> {
        val list = mutableListOf<Any>()
        val personBean1 =
            PersonBean("zhangsan", 1, 18, "你好啊")
        val personBean2 =
            PersonBean("李四", 1, 17, "你好啊1")
        val personBean3 =
            PersonBean("王五", 0, 16, "你好啊2")
        val personBean4 =
            PersonBean2("赵柳", 0, 15, "你好啊3")
        val personBean5 =
            PersonBean2("孙⑦", 1, 14, "你好啊4")
        list.add(personBean1)
        list.add(personBean2)
        list.add(personBean3)
        list.add(personBean4)
        list.add(personBean5)
        return list
    }
}