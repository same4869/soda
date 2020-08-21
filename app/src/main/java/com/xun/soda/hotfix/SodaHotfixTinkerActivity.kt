package com.xun.soda.hotfix

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.xun.soda.R
import com.xun.sodaability.comm.onClick
import com.xun.sodaability.hotfix.tinker.manager.SodaTinkerManager
import com.xun.sodalibrary.log.SodaLog
import kotlinx.android.synthetic.main.activity_soda_hotfix_tinker.*

class SodaHotfixTinkerActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_soda_hotfix_tinker)

        mTextBtn.onClick {
            SodaLog.d("假装这是一个bug，但已经被修复")
        }

        mDownloadBtn.onClick {
            SodaTinkerManager.syncPatchFromServer(applicationContext)
        }

        mMergeBtn.onClick {
            SodaTinkerManager.syncUpgradePatch(applicationContext)
        }
    }
}