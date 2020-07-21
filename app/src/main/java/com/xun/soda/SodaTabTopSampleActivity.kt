package com.xun.soda

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.PagerAdapter
import com.xun.sodaui.tab.comm.ISodaTabLayout
import com.xun.sodaui.tab.top.SodaTabTopInfo
import kotlinx.android.synthetic.main.activity_soda_tab_top_demo.*

class SodaTabTopSampleActivity : AppCompatActivity() {
    private var tabsStr = arrayOf(
        "热门",
        "服装",
        "数码",
        "鞋子",
        "零食",
        "家电",
        "汽车",
        "百货",
        "家居",
        "装修"
    )

    private val viewList by lazy {
        listOf<View>(TextView(applicationContext).apply {
            text = "第1页"
        }, TextView(applicationContext).apply {
            text = "第2页"
        }, TextView(applicationContext).apply {
            text = "第3页"
        }, TextView(applicationContext).apply {
            text = "第4页"
        }, TextView(applicationContext).apply {
            text = "第5页"
        }, TextView(applicationContext).apply {
            text = "第6页"
        }, TextView(applicationContext).apply {
            text = "第7页"
        }, TextView(applicationContext).apply {
            text = "第8页"
        }, TextView(applicationContext).apply {
            text = "第9页"
        }, TextView(applicationContext).apply {
            text = "第10页"
        })
    }

    private val pagerAdapter = object : PagerAdapter() {
        override fun isViewFromObject(view: View, `object`: Any): Boolean = `object` == view

        override fun getCount(): Int = viewList.size

        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            val lp =
                ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
            container.addView(viewList[position], lp)
            return viewList[position]
        }

        override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
            container.removeView(viewList[position])
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_soda_tab_top_demo)
        initTabTop()

        mViewPager.adapter = pagerAdapter
        mTabTopLayout.setViewPager(mViewPager)
    }

    private fun initTabTop() {
        val infoList: MutableList<SodaTabTopInfo> = mutableListOf()
        val defaultColor = "#ff656667"
        val tintColor = "#ffd44949"
        tabsStr.forEach {
            val info = SodaTabTopInfo(it, defaultColor, tintColor)
            infoList.add(info)
        }
        mTabTopLayout.inflateInfo(infoList)
        mTabTopLayout.addTabSelectedChangeListener(object :
            ISodaTabLayout.OnTabSelectedListener<SodaTabTopInfo> {
            override fun onTabSelectedChange(
                index: Int,
                prevInfo: SodaTabTopInfo?,
                nextInfo: SodaTabTopInfo
            ) {
                mViewPager.setCurrentItem(index, true)
            }
        })
        mTabTopLayout.defaultSelected(infoList[0])
    }
}