package com.xun.sodaability.hotfix.tinker.manager

import com.tencent.tinker.entry.ApplicationLike
import com.tencent.tinker.lib.listener.DefaultPatchListener
import com.tencent.tinker.lib.patch.UpgradePatch
import com.tencent.tinker.lib.tinker.TinkerInstaller
import com.tencent.tinker.lib.util.UpgradePatchRetry
import com.xun.sodaability.hotfix.tinker.crash.SodaTinkerUncaughtExceptionHandler
import com.xun.sodaability.hotfix.tinker.reporter.SodaLoadReporter
import com.xun.sodaability.hotfix.tinker.reporter.SodaPatchReporter
import com.xun.sodaability.hotfix.tinker.service.SampleResultService
import com.xun.sodaability.hotfix.tinker.utils.LogUtils

object TinkerManager {
    private var applicationLike: ApplicationLike? = null
    private var uncaughtExceptionHandler: SodaTinkerUncaughtExceptionHandler? = null
    private var isInstalled = false

    fun installTinker(appLike: ApplicationLike) {
        if (isInstalled) {
            LogUtils.d("sodaTinker", "sodaTinker install tinker, but has installed, ignore")
            return
        }
        //you can set your own upgrade patch if you need
        val upgradePatchProcessor = UpgradePatch()

        TinkerInstaller.install(
            appLike,
            SodaLoadReporter(appLike.application),
            SodaPatchReporter(appLike.application),
            DefaultPatchListener(appLike.application),
            SampleResultService::class.java,
            upgradePatchProcessor
        )

        isInstalled = true

    }


    fun setTinkerApplicationLike(appLike: ApplicationLike) {
        applicationLike = appLike
    }

    fun getTinkerApplicationLike(): ApplicationLike? {
        return applicationLike
    }

    fun initFastCrashProtect() {
        if (uncaughtExceptionHandler == null) {
            uncaughtExceptionHandler = SodaTinkerUncaughtExceptionHandler()
            Thread.setDefaultUncaughtExceptionHandler(uncaughtExceptionHandler)
        }
    }

    fun setUpgradeRetryEnable(enable: Boolean) {
        UpgradePatchRetry.getInstance(applicationLike?.application).setRetryEnable(enable)
    }
}