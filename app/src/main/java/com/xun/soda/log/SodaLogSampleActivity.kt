package com.xun.soda.log

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.xun.soda.R
import com.xun.sodalibrary.log.SodaLog
import com.xun.sodalibrary.log.SodaLogConfig
import com.xun.sodalibrary.log.SodaLogManager
import com.xun.sodalibrary.log.printer.SodaViewPrinter
import kotlinx.android.synthetic.main.activity_soda_log_sample.*

class SodaLogSampleActivity : AppCompatActivity() {
    private var viewPrinter: SodaViewPrinter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_soda_log_sample)

        viewPrinter = SodaViewPrinter(this)
        SodaLogManager.addPrinter(viewPrinter!!)

        mLog1.setOnClickListener {
            SodaLog.d("------onCreate------")
        }

        val arrayList1 = arrayListOf<String>()
        arrayList1.add("你好")
        arrayList1.add("吃饭了吗")
        arrayList1.add("今天天气不是")
        arrayList1.add("是的呢")
        arrayList1.add("再见")
        mLog2.setOnClickListener {
            SodaLog.d("SodaLogSampleActivity", arrayList1)
        }

        val logBean = TestLogBean()
        logBean.age = 18
        logBean.name = "张三"
        logBean.isGood = true
        logBean.sex = 1
        mLog3.setOnClickListener {
            SodaLog.d("kkkkkkkkk", logBean)
        }

        mLog4.setOnClickListener {
            test1()
        }

        mLog5.setOnClickListener {
            test1()
        }

        mLog6.setOnClickListener {
            viewPrinter?.showLogView()
        }
    }

    private fun test1() {
        SodaLog.log(object : SodaLogConfig() {
            override fun stackTraceDepth(): Int {
                return 5
            }
        }, contents = "test1~~~~~~~")
    }
}