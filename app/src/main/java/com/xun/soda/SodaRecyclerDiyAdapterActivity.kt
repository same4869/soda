package com.xun.soda

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.xun.soda.adapter.PersonAdapter
import com.xun.soda.bean.PersonBean
import com.xun.soda.bean.PersonBean2
import kotlinx.android.synthetic.main.activity_soda_recycler_base_adapter.*

class SodaRecyclerDiyAdapterActivity : AppCompatActivity() {
    var adapter: PersonAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_soda_recycler_base_adapter)

        adapter = PersonAdapter(arrayListOf(),applicationContext)

        mRecyclerView.layoutManager = LinearLayoutManager(applicationContext)
        mRecyclerView.adapter = adapter
        adapter?.data?.addAll(initData())

        adapter?.addHeaderView(ImageView(applicationContext).apply {
            setImageResource(R.mipmap.ic_launcher)
        })

        adapter?.addFooterView(TextView(applicationContext).apply {
            text = "我是一个footer"
        })
    }

    private fun initData(): List<Any> {
        val list = mutableListOf<Any>()
        val personBean1 = PersonBean("zhangsan", 1, 18, "你好啊")
        val personBean2 = PersonBean("李四", 1, 17, "你好啊1")
        val personBean3 = PersonBean("王五", 0, 16, "你好啊2")
        val personBean4 = PersonBean2("赵柳", 0, 15, "你好啊3")
        val personBean5 = PersonBean2("孙⑦8888888", 1, 14, "你好啊999999999")
        list.add(personBean1)
        list.add(personBean2)
        list.add(personBean3)
        list.add(personBean4)
        list.add(personBean5)
        return list
    }
}