package com.xun.sodaability.hotfix.tinker.manager

import android.content.Context
import com.google.gson.Gson
import com.tencent.tinker.lib.tinker.TinkerInstaller
import com.xun.sodaability.download.SodaDownLoadManager
import com.xun.sodaability.download.core.DownloadListener
import com.xun.sodaability.hotfix.tinker.bean.PatchCreateFeedbackBean
import com.xun.sodaability.hotfix.tinker.bean.PatchDataBean
import com.xun.sodaability.hotfix.tinker.utils.LogUtils
import com.xun.sodalibrary.utils.*
import java.io.File

/**
 * @Description:    tinker业务管理类
 * @Author:         xwang
 * @CreateDate:     2019-10-14
 */

object SodaTinkerManager {
    const val TINKER_ENTER_SAFE_MODE_COUNT = 3
    const val SODA_TINKER_LOG_TAG = "sodaTinker"
    private lateinit var mContext: Context

    private const val TINKER_PATCH_FOLDER = "/soda_tinker_patch/"
    private const val TINKER_PATCH_NAME = "soda_patch_signed_7zip"

    private const val TINKER_LAST_SERVER_PATCH_BEAN_1 =
        "last_server_patch_bean_1" //保存服务器下发的数据，getServerBeanInStr
    private const val TINKER_LAST_SERVER_PATCH_BEAN_2 = "last_server_patch_bean_2"

    //如果这个值为true，则不再下载服务器补丁，除非focus字段为true，进入安全模式有两种情况，1首页自检crash大于3自动清除补丁，2补丁应用连续失败3次
    private const val TINKER_SAFE_MODE_KEY = "safe_mode_key"
    private const val TINKER_SAFE_MODE_COUNT_KEY = "safe_mode_count_key" //补丁加载失败时加1，如果连续大于3次，进入安全模式

    //补丁加载成功因为太靠前，先记录一个变量，延迟上报
    private const val PATCH_LOAD_SUCCESS_KEY = "patch_load_suc_key"

    //成功上报，防止每次都上报同样的日志
    private const val PATCH_LOADED_SUCCESS_KEY = "patch_loaded_suc_key"

    //补丁清除上报，防止每次都上报同样的日志
    private const val PATCH_CLEAR_KEY = "patch_clear_key"

    //补丁应用了之后，回调load 0才算成功
    private const val PATCH_IS_APPLY = "patch_is_apply"

    private var TINKER_LOCAL_DEBUG = true //如果是debug模式，那么加载mock的补丁，且跳过一系列限制

    private var appChannel: String = ""
    private var mSodaTinkerListener: SodaTinkerListener? = null

    interface SodaTinkerListener {
        fun onShouldFeedbackInfo(patchCreateFeedbackBean: PatchCreateFeedbackBean)
    }

    fun setSodaTinkerListener(sodaTinkerListener: SodaTinkerListener) {
        mSodaTinkerListener = sodaTinkerListener
    }

    fun setIsDebug(isDebug: Boolean) {
        TINKER_LOCAL_DEBUG = isDebug
    }

    fun setAppChannel(channel: String) {
        this.appChannel = channel
    }

    fun setApplicationContext(context : Context){
        mContext = context
    }

    fun getApplicationContext():Context{
        return mContext
    }

    private fun mockData(): PatchDataBean {
        val patchData = PatchDataBean()
        patchData.patch_url =
            "https://same4869-test.oss-cn-shanghai.aliyuncs.com/soda_tinker_patch_1.apk"
        return patchData
    }

    //应该是网络请求后把数据丢进这个方法
    //TODO 这个方法的扩展性还可以再提升下
    fun syncPatchFromServer(patchData: PatchDataBean) {
        uploadPatchSucIfNeed()

        LogUtils.d(SODA_TINKER_LOG_TAG, " syncPatchFromServer")
        val tinkerSeverInfoBean = if (TINKER_LOCAL_DEBUG) {
            mockData()
        } else {
            patchData
        }
        if (!TINKER_LOCAL_DEBUG) {
            //服务器要求关闭，停止后续操作
            if (!patchData.is_enable) {
                LogUtils.d(SODA_TINKER_LOG_TAG, "sodaTinker 接口要求关闭")
                return
            }

            //如果channel数组为空，证明是全渠道，直接放行
            if (patchData.channels.isNotEmpty() && appChannel.isBlank()) {
                //数组中找不到该渠道
                if (patchData.channels.indexOf(appChannel) == -1
                ) {
                    LogUtils.d(SODA_TINKER_LOG_TAG, "sodaTinker 没有匹配的渠道")
                    return
                }
            }

            if (patchData.clear_patch) {
                LogUtils.d(SODA_TINKER_LOG_TAG, "sodaTinker 服务器要求清除补丁")
                TinkerInstaller.cleanPatch(mContext)
                if (!SodaSPUtils.getInstance(SodaSPUtils.SpName.SP_TABLE_TINKER)
                        .getBoolean(PATCH_CLEAR_KEY, false)
                ) {
                    setupPatchFeedbackInfo("clear patch", 1)
                    savePatchClear(true)
                }
                clearServerBean()
                return
            }

            //如果是安全模式且后台focusInSafeMode为关闭，则不再继续执行
            if (SodaSPUtils.getInstance(SodaSPUtils.SpName.SP_TABLE_TINKER).getBoolean(
                    TINKER_SAFE_MODE_KEY,
                    false
                ) && !patchData.focus_in_safe_mode
            ) {
                LogUtils.d(SODA_TINKER_LOG_TAG, "sodaTinker 进入安全模式且后台没有强制 直接忽略")
                return
            }
        }

        if (getServerBeanInStr(mContext) == Gson().toJson(tinkerSeverInfoBean) && !patchData.focus) {
            LogUtils.d(SODA_TINKER_LOG_TAG, "sodaTinker 和上次保存的数据一致，忽略")
            return
        }

        if (checkShouldDownPatch(tinkerSeverInfoBean) || TINKER_LOCAL_DEBUG) {
            LogUtils.d(SODA_TINKER_LOG_TAG, "sodaTinker 开始下载补丁")
            savePatchLoadSuc(false)
            savePatchLoadedSuc(false)
            savePatchClear(false)
            savePatchApply(false)
            downPatchAndVerify(
                mContext,
                mContext.filesDir.path + TINKER_PATCH_FOLDER + TINKER_PATCH_NAME,
                tinkerSeverInfoBean
            )
        }
    }

    fun syncUpgradePatch() {
        LogUtils.d(SODA_TINKER_LOG_TAG, "sodaTinker syncUpgradePatch")
        val lastServerPatchStr = getServerBeanInStr(mContext)
        if (lastServerPatchStr.isNullOrBlank()) {
            LogUtils.d(
                SODA_TINKER_LOG_TAG,
                "sodaTinker syncUpgradePatch lastServerPatchStr is blank"
            )
            return
        }

        val patchPath = mContext.filesDir.path + TINKER_PATCH_FOLDER + TINKER_PATCH_NAME

        if (SodaFileUtil.fileIsExists(patchPath)) {
            val md5 =
                SodaFileUtil.getFileMD5(File(patchPath))
            val tinkerSeverInfoBean =
                Gson().fromJson(lastServerPatchStr, PatchDataBean::class.java)
            LogUtils.d(
                SODA_TINKER_LOG_TAG,
                "sodaTinker md5${md5} lastServerPatchStr:$lastServerPatchStr"
            )
            if (md5 == tinkerSeverInfoBean.patch_md5 || TINKER_LOCAL_DEBUG) {
                LogUtils.d(SODA_TINKER_LOG_TAG, "sodaTinker 尝试应用补丁")
                setupPatchFeedbackInfo("patch apply", 1)
                TinkerInstaller.onReceiveUpgradePatch(
                    mContext, patchPath
                )
                savePatchApply(true)
            } else {
                LogUtils.d(SODA_TINKER_LOG_TAG, "sodaTinker 应用补丁时md5校验失败，删除文件")
                SodaFileUtil.deleteFile(File(patchPath))
            }
        } else {
            LogUtils.d(SODA_TINKER_LOG_TAG, "sodaTinker syncUpgradePatch file not exits")
        }
    }

    private fun uploadPatchSucIfNeed() {
        if (SodaSPUtils.getInstance(SodaSPUtils.SpName.SP_TABLE_TINKER)
                .getBoolean(
                    PATCH_LOAD_SUCCESS_KEY,
                    false
                ) && !SodaSPUtils.getInstance(SodaSPUtils.SpName.SP_TABLE_TINKER)
                .getBoolean(PATCH_LOADED_SUCCESS_KEY, false)
        ) {
            setupPatchFeedbackInfo("patch load suc", 1)
            savePatchLoadedSuc(true)
        }
    }

    fun setupPatchFeedbackInfo(
        extra: String,
        type: Int = 0,
        patch_version: String = ""
    ) {
        LogUtils.d(SODA_TINKER_LOG_TAG, "sodaTinker uploadFeedbackAboutPatch extra:$extra")
        val patchCreateFeedbackBean = PatchCreateFeedbackBean()
        patchCreateFeedbackBean.app_version = getAppVersionCode()
        patchCreateFeedbackBean.channel = appChannel
        patchCreateFeedbackBean.device_id = getDeviceId()

        val localData = getServerBeanInStr(mContext, type)
        if (!localData.isNullOrBlank()) {
            val patchDataBean = Gson().fromJson(
                localData,
                PatchDataBean::class.java
            )
            patchCreateFeedbackBean.patch_version = patchDataBean.patch_version
        } else {
            if (type == 1) {
                val patchDataBean2 = Gson().fromJson(
                    getServerBeanInStr(mContext, 0),
                    PatchDataBean::class.java
                )
                if (patchDataBean2 != null) {
                    patchCreateFeedbackBean.patch_version = patchDataBean2.patch_version
                }
            }
        }
        patchCreateFeedbackBean.extra_list.add(extra)
        LogUtils.d(
            SODA_TINKER_LOG_TAG,
            "sodaTinker uploadFeedbackAboutPatch patchCreateFeedbackBean:$patchCreateFeedbackBean"
        )
        if (patch_version.isNotBlank()) {
            patchCreateFeedbackBean.patch_version = patch_version
        }
        if (patchCreateFeedbackBean.patch_version.isBlank()) {
            //如果还为空证明可能是第一次或者从未下载过补丁，不上报
            return
        }
        mSodaTinkerListener?.onShouldFeedbackInfo(patchCreateFeedbackBean)
    }

    fun savePatchLoadSuc(isLoadSuc: Boolean) {
        SodaSPUtils.getInstance(SodaSPUtils.SpName.SP_TABLE_TINKER)
            .put(PATCH_LOAD_SUCCESS_KEY, isLoadSuc)
    }

    //这个只是防止重复一直上报的
    private fun savePatchLoadedSuc(isLoadedSuc: Boolean) {
        SodaSPUtils.getInstance(SodaSPUtils.SpName.SP_TABLE_TINKER)
            .put(PATCH_LOADED_SUCCESS_KEY, isLoadedSuc)
    }

    private fun savePatchClear(isclear: Boolean) {
        SodaSPUtils.getInstance(SodaSPUtils.SpName.SP_TABLE_TINKER)
            .put(PATCH_CLEAR_KEY, isclear)
    }

    private fun savePatchApply(isApply: Boolean) {
        SodaSPUtils.getInstance(SodaSPUtils.SpName.SP_TABLE_TINKER)
            .put(PATCH_IS_APPLY, isApply)
    }

    fun getPatchApply(context: Context): Boolean {
        return SodaSPUtils.getInstance(SodaSPUtils.SpName.SP_TABLE_TINKER)
            .getBoolean(PATCH_IS_APPLY, false)
    }

    fun addSafeModeCount(): Int {
        val count =
            SodaSPUtils.getInstance(SodaSPUtils.SpName.SP_TABLE_TINKER)
                .getInt(TINKER_SAFE_MODE_COUNT_KEY, 0)
        SodaSPUtils.getInstance(SodaSPUtils.SpName.SP_TABLE_TINKER)
            .put(TINKER_SAFE_MODE_COUNT_KEY, count + 1)
        return count + 1
    }

    fun resetSafeModeCount() {
        SodaSPUtils.getInstance(SodaSPUtils.SpName.SP_TABLE_TINKER)
            .put(TINKER_SAFE_MODE_COUNT_KEY, 0)
    }

    fun enterSafeMode() {
        setupPatchFeedbackInfo("enter safe mode", 1)
        SodaSPUtils.getInstance(SodaSPUtils.SpName.SP_TABLE_TINKER).put(TINKER_SAFE_MODE_KEY, true)
    }

    fun exitSafeMode() {
        SodaSPUtils.getInstance(SodaSPUtils.SpName.SP_TABLE_TINKER).put(TINKER_SAFE_MODE_KEY, false)
    }

    private fun clearServerBean() {
        SodaSPUtils.getInstance(SodaSPUtils.SpName.SP_TABLE_TINKER)
            .put(TINKER_LAST_SERVER_PATCH_BEAN_1, "")
        SodaSPUtils.getInstance(SodaSPUtils.SpName.SP_TABLE_TINKER)
            .put(TINKER_LAST_SERVER_PATCH_BEAN_2, "")
    }

    //这里使用了两个变量存服务器的下发配置，逻辑如下
    //在应用补丁之前会读取上一次保存的服务器下发配置，这个时候type=0,要取last_server_patch_bean_2这个最新的，并同步给last_server_patch_bean_1
    //在补丁应用的过程中（可能成功或失败回调之前），可能又会下载新的补丁配置，这时更新的是last_server_patch_bean_2
    //补丁成功或失败上报是应该type=1，取原来的那个last_server_patch_bean_1
    //0是服务器最新的，1是原来本地的（如果有）
    private fun getServerBeanInStr(context: Context, type: Int = 0): String? {
        val lastServerPatchStr1 =
            SodaSPUtils.getInstance(SodaSPUtils.SpName.SP_TABLE_TINKER)
                .getString(TINKER_LAST_SERVER_PATCH_BEAN_1)
        if (type == 1) {
            return lastServerPatchStr1
        }
        val lastServerPatchStr2 =
            SodaSPUtils.getInstance(SodaSPUtils.SpName.SP_TABLE_TINKER)
                .getString(TINKER_LAST_SERVER_PATCH_BEAN_2)
        return if (lastServerPatchStr1 == lastServerPatchStr2) {
            lastServerPatchStr1
        } else {
            SodaSPUtils.getInstance(SodaSPUtils.SpName.SP_TABLE_TINKER)
                .put(TINKER_LAST_SERVER_PATCH_BEAN_1, lastServerPatchStr2)
            lastServerPatchStr2
        }
    }

    private fun saveServerBeanInStr(str: String) {
        SodaSPUtils.getInstance(SodaSPUtils.SpName.SP_TABLE_TINKER)
            .putSyn(TINKER_LAST_SERVER_PATCH_BEAN_2, str)
    }

    //根据app version判断是否下载补丁包
    private fun checkShouldDownPatch(tinkerSeverInfoBean: PatchDataBean): Boolean {
        val appVersionName = getAppVersionCode()
        LogUtils.d(SODA_TINKER_LOG_TAG, "sodaTinker appVersionName : $appVersionName")
        return tinkerSeverInfoBean.target_host_version == appVersionName
    }

    //下载及校验补丁包md5
    private fun downPatchAndVerify(
        context: Context,
        filePath: String,
        patchDataBean: PatchDataBean
    ) {
        LogUtils.d(SODA_TINKER_LOG_TAG, "sodaTinker filePath : $filePath")
        SodaDownLoadManager().downloadWithAbsolutePath(patchDataBean.patch_url,
            filePath,
            false,
            object : DownloadListener {
                override fun onStartDownload() {
                    LogUtils.d(SODA_TINKER_LOG_TAG, "sodaTinker onStartDownload")
                }

                override fun onProgress(progress: Int) {
                    LogUtils.d(SODA_TINKER_LOG_TAG, "sodaTinker progress : $progress")
                }

                override fun onFinishDownload(url: String, path: String) {
                    LogUtils.d(SODA_TINKER_LOG_TAG, "sodaTinker onFinishDownload : $path")
                    val md5 = SodaFileUtil.getFileMD5(File(path))
                    LogUtils.d(SODA_TINKER_LOG_TAG, "sodaTinker onFinishDownload md5 : $md5")
                    if (md5 == patchDataBean.patch_md5 || TINKER_LOCAL_DEBUG) {
                        LogUtils.d(SODA_TINKER_LOG_TAG, "sodaTinker 补丁下载并校验完成")
                        saveServerBeanInStr(Gson().toJson(patchDataBean))
                        setupPatchFeedbackInfo("patch downloaded")
                    } else {
                        SodaFileUtil.deleteFile(File(path))
                        LogUtils.d("sodaTinker 文件校验失败 删除")
                    }
                }

                override fun onFail(url: String, errorInfo: String) {
                    LogUtils.d(SODA_TINKER_LOG_TAG, "sodaTinker onFail : $errorInfo")
                }
            })
    }

}