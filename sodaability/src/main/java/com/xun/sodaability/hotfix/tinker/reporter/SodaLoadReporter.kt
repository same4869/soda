package com.xun.sodaability.hotfix.tinker.reporter

import android.content.Context
import com.tencent.tinker.lib.reporter.DefaultLoadReporter
import com.tencent.tinker.loader.shareutil.ShareConstants
import com.xun.sodaability.hotfix.tinker.manager.SodaTinkerManager
import com.xun.sodaability.hotfix.tinker.utils.LogUtils
import java.io.File

/**
 * @Description:
 * @Author:         xwang
 * @CreateDate:     2019-10-16
 */

class SodaLoadReporter(context: Context?) : DefaultLoadReporter(context) {
    override fun onLoadPatchListenerReceiveFail(patchFile: File, errorCode: Int) {
        super.onLoadPatchListenerReceiveFail(patchFile, errorCode)
        LogUtils.d(SodaTinkerManager.SODA_TINKER_LOG_TAG, "---onLoadPatchListenerReceiveFail")
    }

    override fun onLoadResult(patchDirectory: File, loadCode: Int, cost: Long) {
        super.onLoadResult(patchDirectory, loadCode, cost)
        LogUtils.d(SodaTinkerManager.SODA_TINKER_LOG_TAG, "---onLoadResult loadCode:$loadCode")
        when (loadCode) {
            ShareConstants.ERROR_LOAD_OK -> {
                if (SodaTinkerManager.getPatchApply(context)) {
                    SodaTinkerManager.savePatchLoadSuc( true)
                }
            }
        }
    }

    override fun onLoadException(e: Throwable, errorCode: Int) {
        super.onLoadException(e, errorCode)
        LogUtils.d(SodaTinkerManager.SODA_TINKER_LOG_TAG, "---onLoadException $errorCode ${e.message}")
    }

    override fun onLoadFileMd5Mismatch(file: File, fileType: Int) {
        super.onLoadFileMd5Mismatch(file, fileType)
        LogUtils.d(SodaTinkerManager.SODA_TINKER_LOG_TAG, "---onLoadFileMd5Mismatch")
    }

    /**
     * try to recover patch oat file
     *
     * @param file
     * @param fileType
     * @param isDirectory
     */
    override fun onLoadFileNotFound(file: File, fileType: Int, isDirectory: Boolean) {
        super.onLoadFileNotFound(file, fileType, isDirectory)
        LogUtils.d(SodaTinkerManager.SODA_TINKER_LOG_TAG, "---onLoadFileNotFound")
    }

    override fun onLoadPackageCheckFail(patchFile: File, errorCode: Int) {
        super.onLoadPackageCheckFail(patchFile, errorCode)
        LogUtils.d(SodaTinkerManager.SODA_TINKER_LOG_TAG, "---onLoadPackageCheckFail")
    }

    override fun onLoadPatchInfoCorrupted(
        oldVersion: String,
        newVersion: String,
        patchInfoFile: File
    ) {
        super.onLoadPatchInfoCorrupted(oldVersion, newVersion, patchInfoFile)
        LogUtils.d(SodaTinkerManager.SODA_TINKER_LOG_TAG, "---onLoadPatchInfoCorrupted")
    }

    override fun onLoadInterpret(type: Int, e: Throwable) {
        super.onLoadInterpret(type, e)
        LogUtils.d(SodaTinkerManager.SODA_TINKER_LOG_TAG, "---onLoadInterpret")
    }

}