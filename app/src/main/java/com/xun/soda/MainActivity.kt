package com.xun.soda

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mLogBtn.setOnClickListener {
            startTargetActivity(SodaLogSampleActivity::class.java)
        }

        mTabBottomBtn.setOnClickListener {
            startTargetActivity(SodaTabBottomSampleActivity::class.java)
        }

        mTabTopBtn.setOnClickListener {
            startTargetActivity(SodaTabTopSampleActivity::class.java)
        }

        mTabRefreshBtn.setOnClickListener {
            startTargetActivity(SodaRefreshSampleActivity::class.java)
        }

        mBannerBtn.setOnClickListener {
            startTargetActivity(SodaBannerSampleActivity::class.java)
        }
    }

    private fun startTargetActivity(clazz: Class<*>) {
        startActivity(Intent(this, clazz))
    }
}