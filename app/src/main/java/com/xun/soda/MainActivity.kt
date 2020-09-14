package com.xun.soda

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.xun.soda.banner.SodaBannerSampleActivity
import com.xun.soda.download.SodaDownloadActivity
import com.xun.soda.executor.SodaExecutorActivity
import com.xun.soda.hotfix.SodaHotfixTinkerActivity
import com.xun.soda.log.SodaLogSampleActivity
import com.xun.soda.net.RetrofitClient
import com.xun.soda.net.converter.exception.BaseErrorConsumer
import com.xun.soda.recyclerview.SodaRecyclerViewActivity
import com.xun.soda.refresh.SodaRefreshSampleActivity
import com.xun.soda.sodaclean.SodaCleanActivity
import com.xun.soda.tabview.SodaTabBottomSampleActivity
import com.xun.soda.tabview.SodaTabTopSampleActivity
import com.xun.sodaability.comm.applySchedulers
import com.xun.sodaability.comm.onClick
import com.xun.sodalibrary.log.SodaLog
import io.reactivex.functions.Consumer
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

        mRecyclerViewBtn.setOnClickListener {
            startTargetActivity(SodaRecyclerViewActivity::class.java)
        }

        mSodaCleanBtn.setOnClickListener {
            startTargetActivity(SodaCleanActivity::class.java)
        }

        mSodaExecutor.setOnClickListener {
            startTargetActivity(SodaExecutorActivity::class.java)
        }

        mSodaDownload.onClick {
            startTargetActivity(SodaDownloadActivity::class.java)
        }

        mSodaHotFixTinker.onClick {
            startTargetActivity(SodaHotfixTinkerActivity::class.java)
        }

        mSodaMock.onClick {
            RetrofitClient.getApiServiceWithBaseUrl().requestCreateVerification().applySchedulers()
                .subscribe(
                    Consumer {
                        SodaLog.d("mock data : " + it.data)
                    }, BaseErrorConsumer()
                )
        }
    }

    private fun startTargetActivity(clazz: Class<*>) {
        startActivity(Intent(this, clazz))
    }
}