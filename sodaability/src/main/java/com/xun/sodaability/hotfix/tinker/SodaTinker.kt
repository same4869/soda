package com.xun.sodaability.hotfix.tinker

import com.xun.sodaability.hotfix.tinker.bean.PatchDataBean
import com.xun.sodaability.hotfix.tinker.manager.SodaTinkerManager

/**
 * @Description:    带业务逻辑的tinker热修复方案封装，对外接口类
 * @Author:         xwang
 * @CreateDate:     2020/8/25
 */

object SodaTinker {

    /**
     * 初始化一些配置变量，因为不是必须，所以不强制
     */
    fun init(appChannel: String = "", isDebug: Boolean = false){
        SodaTinkerManager.setAppChannel(appChannel)
        SodaTinkerManager.setIsDebug(isDebug)
    }

    /**
     * 收到服务器下发的补丁信息后交给此类进行业务处理
     */
    fun syncPatchFromServer(patchData: PatchDataBean) {
        SodaTinkerManager.syncPatchFromServer(patchData)
    }

    /**
     * 尝试合成与应用本地补丁
     */
    fun syncUpgradePatch() {
        SodaTinkerManager.syncUpgradePatch()
    }

    /**
     * 有上报日志时回调，业务方自己处理
     */
    fun setSodaTinkerListener(sodaTinkerListener: SodaTinkerManager.SodaTinkerListener) {
        SodaTinkerManager.setSodaTinkerListener(sodaTinkerListener)
    }

}