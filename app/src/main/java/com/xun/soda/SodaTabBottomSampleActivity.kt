package com.xun.soda

import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.xun.sodalibrary.utils.dp2px
import com.xun.sodaui.tab.bottom.SodaTabBottomInfo
import com.xun.sodaui.tab.comm.ISodaTabLayout
import kotlinx.android.synthetic.main.activity_soda_tab_bottom_sample.*
import java.util.ArrayList

class SodaTabBottomSampleActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_soda_tab_bottom_sample)
        initTabBottom()
    }

    private fun initTabBottom() {
        val bottomInfoList: MutableList<SodaTabBottomInfo> = ArrayList()
        mTabBottomLayout.setTabAlpha(0.5f)
        val homeInfo = SodaTabBottomInfo(
            "首页",
            "fonts/iconfont.ttf",
            getString(R.string.if_home),
            "首页哈",
            "#ff656667",
            "#ffd44949"
        )
        val infoRecommend = SodaTabBottomInfo(
            "收藏",
            "fonts/iconfont.ttf",
            "收藏",
            "收藏哈",
            "#ff656667",
            "#ffd44949"
        )
        val bitmap = BitmapFactory.decodeResource(resources, R.drawable.fire, null)
        val infoCategory = SodaTabBottomInfo(
            "分类",
            bitmap,
            bitmap
        )
        bottomInfoList.add(homeInfo)
        bottomInfoList.add(infoRecommend)
        bottomInfoList.add(infoCategory)
        mTabBottomLayout.inflateInfo(bottomInfoList)
        mTabBottomLayout.addTabSelectedChangeListener(object :
            ISodaTabLayout.OnTabSelectedListener<SodaTabBottomInfo> {
            override fun onTabSelectedChange(
                index: Int,
                prevInfo: SodaTabBottomInfo?,
                nextInfo: SodaTabBottomInfo
            ) {
                Toast.makeText(applicationContext, nextInfo.name, Toast.LENGTH_SHORT).show()
            }
        })
        mTabBottomLayout.defaultSelected(homeInfo)
        val tabBottom = mTabBottomLayout.findTab(bottomInfoList[2])
        tabBottom?.apply {
            resetHeight(66f.dp2px)
        }
    }
}