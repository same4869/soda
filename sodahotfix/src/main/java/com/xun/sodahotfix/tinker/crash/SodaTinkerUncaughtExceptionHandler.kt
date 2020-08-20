package com.xun.sodahotfix.tinker.crash

import android.content.Context
import android.os.SystemClock
import com.tencent.tinker.lib.tinker.TinkerApplicationHelper
import com.tencent.tinker.loader.shareutil.ShareConstants
import com.tencent.tinker.loader.shareutil.ShareTinkerInternals
import com.xun.sodahotfix.tinker.manager.SodaTinkerManager
import com.xun.sodahotfix.tinker.manager.TinkerManager
import com.xun.sodahotfix.tinker.reporter.SodaTinkerReport
import com.xun.sodahotfix.tinker.utils.LogUtils
import com.xun.sodahotfix.tinker.utils.Utils

/**
 * @Description:  is SampleUncaughtExceptionHandler
 * @Author:         xwang
 * @CreateDate:     2019-11-28
 */

class SodaTinkerUncaughtExceptionHandler : Thread.UncaughtExceptionHandler {

    private val ueh: Thread.UncaughtExceptionHandler = Thread.getDefaultUncaughtExceptionHandler()

    override fun uncaughtException(thread: Thread, ex: Throwable) {
        LogUtils.d("sodaTinker uncaughtException ${ex.message}")
        tinkerFastCrashProtect()
        tinkerPreVerifiedCrashHandler(ex)
        ueh.uncaughtException(thread, ex)
    }

    /**
     * Such as Xposed, if it try to load some class before we load from patch files.
     * With dalvik, it will crash with "Class ref in pre-verified class resolved to unexpected implementation".
     * With art, it may crash at some times. But we can't know the actual crash type.
     * If it use Xposed, we can just clean patch or mention user to uninstall it.
     */
    private fun tinkerPreVerifiedCrashHandler(ex: Throwable) {
        val applicationLike = TinkerManager.getTinkerApplicationLike()
        if (applicationLike?.application == null) {
            LogUtils.d("sodaTinker applicationlike is null")
            return
        }

        if (!TinkerApplicationHelper.isTinkerLoadSuccess(applicationLike)) {
            LogUtils.d("sodaTinker tinker is not loaded")
            return
        }

        var throwable: Throwable? = ex
        var isXposed = false
        while (throwable != null) {
            if (!isXposed) {
                isXposed = Utils.isXposedExists(throwable)
            }

            // xposed?
            if (isXposed) {
                var isCausedByXposed = false
                //for art, we can't know the actually crash type
                //just ignore art
                if (throwable is IllegalAccessError && throwable.message?.contains(
                        DALVIK_XPOSED_CRASH
                    ) == true
                ) {
                    //for dalvik, we know the actual crash type
                    isCausedByXposed = true
                }

                if (isCausedByXposed) {
                    SodaTinkerReport.onXposedCrash()
                    LogUtils.d("sodaTinker have xposed: just clean tinker")

                    //kill all other process to ensure that all process's code is the same.
                    ShareTinkerInternals.killAllOtherProcess(applicationLike.application)

                    TinkerApplicationHelper.cleanPatch(applicationLike)
                    ShareTinkerInternals.setTinkerDisableWithSharedPreferences(applicationLike.application)
                    return
                }
            }
            throwable = throwable.cause
        }
    }

    /**
     * if tinker is load, and it crash more than MAX_CRASH_COUNT, then we just clean patch.
     */
    private fun tinkerFastCrashProtect(): Boolean {
        val applicationLike = TinkerManager.getTinkerApplicationLike()

        if (applicationLike == null || applicationLike.application == null) {
            return false
        }
        if (!TinkerApplicationHelper.isTinkerLoadSuccess(applicationLike)) {
            return false
        }

        val elapsedTime =
            SystemClock.elapsedRealtime() - applicationLike.applicationStartElapsedTime
        //this process may not install tinker, so we use TinkerApplicationHelper api
        if (elapsedTime < QUICK_CRASH_ELAPSE) {
            val currentVersion = TinkerApplicationHelper.getCurrentVersion(applicationLike!!)
            if (ShareTinkerInternals.isNullOrNil(currentVersion)) {
                return false
            }

            val sp = applicationLike.application.getSharedPreferences(
                ShareConstants.TINKER_SHARE_PREFERENCE_CONFIG,
                Context.MODE_MULTI_PROCESS
            )
            val fastCrashCount = sp.getInt(currentVersion, 0) + 1
            if (fastCrashCount >= MAX_CRASH_COUNT) {
                SodaTinkerReport.onFastCrashProtect()
                TinkerApplicationHelper.cleanPatch(applicationLike)
                LogUtils.d("tinker has fast crash more than $fastCrashCount, we just clean patch!")
                SodaTinkerManager.enterSafeMode(applicationLike.application)
                return true
            } else {
                sp.edit().putInt(currentVersion, fastCrashCount).commit()
                LogUtils.d("tinker has fast crash $fastCrashCount times")
            }
        }

        return false
    }

    companion object {
        private const val QUICK_CRASH_ELAPSE = (10 * 1000).toLong()
        const val MAX_CRASH_COUNT = 3
        private const val DALVIK_XPOSED_CRASH =
            "Class ref in pre-verified class resolved to unexpected implementation"
    }
}
