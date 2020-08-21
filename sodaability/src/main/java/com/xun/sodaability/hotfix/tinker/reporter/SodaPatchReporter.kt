package com.xun.sodaability.hotfix.tinker.reporter

import android.content.Context
import android.content.Intent
import com.tencent.tinker.lib.reporter.DefaultPatchReporter
import com.tencent.tinker.loader.shareutil.SharePatchInfo
import com.xun.sodaability.hotfix.tinker.utils.LogUtils
import java.io.File

/**
 * @Description:
 * @Author:         xwang
 * @CreateDate:     2019-10-16
 */

class SodaPatchReporter(context: Context?) : DefaultPatchReporter(context) {
    override fun onPatchServiceStart(intent: Intent) {
        super.onPatchServiceStart(intent)
        LogUtils.d("sodaTinker", "---onPatchServiceStart")
    }

    override fun onPatchDexOptFail(patchFile: File, dexFiles: List<File>, t: Throwable) {
        super.onPatchDexOptFail(patchFile, dexFiles, t)
        LogUtils.d("sodaTinker", "---onPatchDexOptFail")
    }

    override fun onPatchException(patchFile: File, e: Throwable) {
        super.onPatchException(patchFile, e)
        LogUtils.d("sodaTinker", "---onPatchException")
    }

    override fun onPatchInfoCorrupted(patchFile: File?, oldVersion: String, newVersion: String) {
        super.onPatchInfoCorrupted(patchFile, oldVersion, newVersion)
        LogUtils.d("sodaTinker", "---onPatchInfoCorrupted")
    }

    override fun onPatchPackageCheckFail(patchFile: File, errorCode: Int) {
        super.onPatchPackageCheckFail(patchFile, errorCode)
        LogUtils.d("sodaTinker", "---onPatchPackageCheckFail")
    }

    override fun onPatchResult(patchFile: File, success: Boolean, cost: Long) {
        super.onPatchResult(patchFile, success, cost)
        LogUtils.d("sodaTinker", "---onPatchResult success:$success")
    }

    override fun onPatchTypeExtractFail(
        patchFile: File,
        extractTo: File,
        filename: String,
        fileType: Int
    ) {
        super.onPatchTypeExtractFail(patchFile, extractTo, filename, fileType)
        LogUtils.d("sodaTinker", "---onPatchTypeExtractFail")
    }

    override fun onPatchVersionCheckFail(
        patchFile: File,
        oldPatchInfo: SharePatchInfo?,
        patchFileVersion: String
    ) {
        super.onPatchVersionCheckFail(patchFile, oldPatchInfo, patchFileVersion)
        LogUtils.d("sodaTinker", "---onPatchVersionCheckFail")
    }
}