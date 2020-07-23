package com.xun.soda

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.bumptech.glide.Glide
import com.xun.soda.bean.SodaBannerBeanImp
import com.xun.sodalibrary.log.SodaLog
import com.xun.sodaui.banner.core.IBindAdapter
import com.xun.sodaui.banner.core.SodaBannerBean
import com.xun.sodaui.banner.indicator.ISodaIndicator
import com.xun.sodaui.banner.indicator.SodaCircleIndicator
import com.xun.sodaui.banner.indicator.SodaNumIndicator
import kotlinx.android.synthetic.main.activity_soda_banner_sample.*
import kotlinx.android.synthetic.main.banner_item_layout.*
import kotlinx.android.synthetic.main.banner_item_layout.view.*

class SodaBannerSampleActivity : AppCompatActivity() {
    private var urls = arrayOf(
        "https://www.devio.org/img/beauty_camera/beauty_camera1.jpg",
        "https://www.devio.org/img/beauty_camera/beauty_camera3.jpg",
        "https://www.devio.org/img/beauty_camera/beauty_camera4.jpg",
        "https://www.devio.org/img/beauty_camera/beauty_camera5.jpg",
        "https://www.devio.org/img/beauty_camera/beauty_camera2.jpg",
        "https://www.devio.org/img/beauty_camera/beauty_camera6.jpg",
        "https://www.devio.org/img/beauty_camera/beauty_camera7.jpg",
        "https://www.devio.org/img/beauty_camera/beauty_camera8.jpeg"
    )

    private var sodaIndicator: ISodaIndicator<*>? = null
    private var autoPlay: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_soda_banner_sample)

        initView(SodaCircleIndicator(applicationContext) as ISodaIndicator<View>, false)

        mAutoPlay.setOnCheckedChangeListener { _, b ->
            autoPlay = b
            initView(sodaIndicator!!, b)
        }

        mTvSwitch.setOnClickListener {
            if (sodaIndicator is SodaCircleIndicator) {
                initView(SodaNumIndicator(this), autoPlay)
            } else {
                initView(SodaCircleIndicator(this), autoPlay)
            }
        }
    }

    private fun initView(sodaIndicator: ISodaIndicator<*>, autoPlay: Boolean) {
        this.sodaIndicator = sodaIndicator
        val dataList = mutableListOf<SodaBannerBean>()
        urls.forEach {
            val sodaBannerBean = SodaBannerBeanImp()
            sodaBannerBean.url = it
            dataList.add(sodaBannerBean)
        }
        mBanner.setIntervalTime(3000)
        mBanner.setScrollDuration(2000)
        mBanner.setAutoPlay(autoPlay)
        mBanner.setSodaIndicator(sodaIndicator)
        mBanner.setBannerData(R.layout.banner_item_layout, dataList)
        mBanner.setBindAdapter(object : IBindAdapter {
            override fun onBind(viewHolder: View, bean: SodaBannerBean, position: Int) {
                SodaLog.d("position:$position url:${bean.url}")
                Glide.with(this@SodaBannerSampleActivity).load(bean.url).into(viewHolder.iv_image)
                viewHolder.tv_title.text = (bean as SodaBannerBeanImp).title
            }
        })
    }
}