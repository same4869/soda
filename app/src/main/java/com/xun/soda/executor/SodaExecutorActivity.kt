package com.xun.soda.executor

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.xun.soda.R
import com.xun.sodaability.comm.onClick
import com.xun.sodalibrary.executor.SodaExecutor
import com.xun.sodalibrary.log.SodaLog
import kotlinx.android.synthetic.main.activity_soda_executor.*

class SodaExecutorActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_soda_executor)

        mSingleTask1.onClick {
            SodaExecutor.execute(runnable = Runnable {
                Thread.sleep(2000)
                SodaLog.d("mSingleTask1 done")
            })
        }

        mSingleTask2.onClick {
            SodaExecutor.execute(1, object : SodaExecutor.Callable<Int>() {
                override fun onBackground(): Int {
                    Thread.sleep(3000)
                    return 1234
                }

                override fun onCompleted(t: Int) {
                    SodaLog.d("mSingleTask2 done onCompleted : $t")
                }
            })
        }

        mSingleTask3.onClick {
            for (i in 50 downTo 0) {
                SodaExecutor.execute(i, runnable = Runnable {
                    Thread.sleep(5000)
                    SodaLog.d("mSingleTask3 done priority is $i")
                })
            }
        }

        mPause.onClick {
            SodaExecutor.pause()
            SodaLog.d("SodaExecutor.pause()")
        }

        mResume.onClick {
            SodaExecutor.resume()
            SodaLog.d("SodaExecutor.resume()")
        }
    }
}