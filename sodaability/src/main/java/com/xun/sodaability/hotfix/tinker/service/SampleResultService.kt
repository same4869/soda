package com.xun.sodaability.hotfix.tinker.service

import android.os.Handler
import android.os.Looper
import com.tencent.tinker.lib.service.DefaultTinkerResultService
import com.tencent.tinker.lib.service.PatchResult
import com.tencent.tinker.lib.util.TinkerLog
import com.tencent.tinker.lib.util.TinkerServiceInternals
import com.xun.sodaability.hotfix.tinker.manager.SodaTinkerManager
import com.xun.sodaability.hotfix.tinker.utils.Utils
import java.io.File

class SampleResultService : DefaultTinkerResultService() {
    private val TAG = "sodaTinker"


    override fun onPatchResult(result: PatchResult?) {
        if (result == null) {
            TinkerLog.e(TAG, "SampleResultService received null result!!!!")
            return
        }
        TinkerLog.i(TAG, "SampleResultService receive result: %s", result.toString())

        //first, we want to kill the recover process
        TinkerServiceInternals.killTinkerPatchServiceProcess(applicationContext)

        val handler = Handler(Looper.getMainLooper())
        handler.post {
            if (result.isSuccess) {
//                SodaTinkerManager.uploadFeedbackAboutPatch(applicationContext,"patch merge success",1)
////                Toast.makeText(applicationContext, "patch success, please restart process", Toast.LENGTH_LONG).show()
//                SodaTinkerManager.resetSafeModeCount(applicationContext)
//                SodaTinkerManager.exitSafeMode(applicationContext)
//                sodaTinkerManager.enterSafeMode(applicationContext)
            } else {
//                SodaTinkerManager.uploadFeedbackAboutPatch(applicationContext,"patch merge failed",1)
//                if(SodaTinkerManager.addSafeModeCount(applicationContext) >= 3){
//                    SodaTinkerManager.enterSafeMode(applicationContext)
//                }

//                Toast.makeText(applicationContext, "patch fail, please check reason", Toast.LENGTH_LONG).show()
            }

        }
        // is success and newPatch, it is nice to delete the raw file, and restart at once
        // for old patch, you can't delete the patch file
        if (result.isSuccess) {
            deleteRawPatchFile(File(result.rawPatchFilePath))

            //not like TinkerResultService, I want to restart just when I am at background!
            //if you have not install tinker this moment, you can use TinkerApplicationHelper api
            if (checkIfNeedKill(result)) {
                if (Utils.isBackground()) {
                    TinkerLog.i(TAG, "it is in background, just restart process")
                    restartProcess()
                } else {
                    //we can wait process at background, such as onAppBackground
                    //or we can restart when the screen off
                    TinkerLog.i(TAG, "tinker wait screen to restart process")
                    Utils.ScreenState(applicationContext,
                        Utils.ScreenState.IOnScreenOff { restartProcess() })
                }
            } else {
                TinkerLog.i(TAG, "I have already install the newly patch version!")
            }
        }
    }

    /**
     * you can restart your process through service or broadcast
     */
    private fun restartProcess() {
        TinkerLog.i(TAG, "app is background now, i can kill quietly")
        //you can send service or broadcast intent to restart your process
        //android.os.Process.killProcess(android.os.Process.myPid())
    }
}