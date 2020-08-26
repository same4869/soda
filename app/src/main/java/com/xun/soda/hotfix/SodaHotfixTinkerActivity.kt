package com.xun.soda.hotfix

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.xun.soda.R
import com.xun.sodaability.comm.onClick
import com.xun.sodaability.hotfix.tinker.SodaTinker
import com.xun.sodaability.hotfix.tinker.bean.PatchCreateFeedbackBean
import com.xun.sodaability.hotfix.tinker.bean.PatchDataBean
import com.xun.sodaability.hotfix.tinker.manager.SodaTinkerManager
import com.xun.sodalibrary.log.SodaLog
import kotlinx.android.synthetic.main.activity_soda_hotfix_tinker.*

class SodaHotfixTinkerActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_soda_hotfix_tinker)

        SodaTinker.init(isDebug = true)

        mTextBtn.onClick {
            SodaLog.d("假装这是一个bug,但已经被修复")
            Toast.makeText(applicationContext, "假装这是一个bug,但已经被修复", Toast.LENGTH_SHORT).show()
        }

        mDownloadBtn.onClick {
            SodaTinker.syncPatchFromServer(PatchDataBean())
        }

        mMergeBtn.onClick {
            SodaTinker.syncUpgradePatch()
        }

        SodaTinker.setSodaTinkerListener(object : SodaTinkerManager.SodaTinkerListener {
            override fun onShouldFeedbackInfo(patchCreateFeedbackBean: PatchCreateFeedbackBean) {
                SodaLog.d(patchCreateFeedbackBean)
            }
        })
    }
}