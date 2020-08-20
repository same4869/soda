package com.xun.sodahotfix.tinker.app

import android.annotation.TargetApi
import android.app.Application
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.multidex.MultiDex
import com.tencent.tinker.entry.DefaultApplicationLike
import com.tencent.tinker.lib.tinker.Tinker
import com.xun.sodahotfix.tinker.manager.TinkerManager
import com.xun.sodalibrary.utils.APPLICATION

/**
 * @Description:
 * @Author:         xwang
 * @CreateDate:     2020/8/19
 */

abstract class SodaTinkerApplicationLike(
    application: Application?,
    tinkerFlags: Int,
    tinkerLoadVerifyFlag: Boolean,
    applicationStartElapsedTime: Long,
    applicationStartMillisTime: Long,
    tinkerResultIntent: Intent?
) : DefaultApplicationLike(
    application,
    tinkerFlags,
    tinkerLoadVerifyFlag,
    applicationStartElapsedTime,
    applicationStartMillisTime,
    tinkerResultIntent
) {

    abstract fun initInApplication(application: Application)

    override fun onCreate() {
        super.onCreate()
        initInApplication(application)
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    override fun onBaseContextAttached(base: Context) {
        super.onBaseContextAttached(base)
        //you must install multiDex whatever tinker is installed!
        MultiDex.install(base)
        APPLICATION = application

        TinkerManager.setTinkerApplicationLike(this)

        TinkerManager.initFastCrashProtect()
        //should set before tinker is installed
        TinkerManager.setUpgradeRetryEnable(true)

        //optional set logIml, or you can use default debug log
//        TinkerInstaller.setLogIml(MyLogImp())

        //installTinker after load multiDex
        //or you can put com.tencent.tinker.** to main dex
        TinkerManager.installTinker(this)

        Tinker.with(application)
    }

}