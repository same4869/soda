//package com.xun.sodahotfix.tinker.manager
//
//import android.content.Context
//import com.tencent.tinker.lib.tinker.TinkerInstaller
//import com.xun.sodahotfix.tinker.bean.PatchData
//import com.xun.sodahotfix.tinker.bean.TinkerSeverInfoBean
//import com.xun.sodahotfix.tinker.model.TinkerPatchModel
//import com.xun.sodahotfix.tinker.utils.LogUtils
//import java.io.File
//
///**
// * @Description:    tinker业务管理类
// * @Author:         xwang
// * @CreateDate:     2019-10-14
// */
//
//object SodaTinkerManager {
//    private const val TINKER_PATCH_FOLDER = "/soda_tinker_patch/"
//    private const val TINKER_PATCH_NAME = "soda_patch_signed_7zip"
//
//    private const val TINKER_LAST_SERVER_PATCH_BEAN_1 =
//        "last_server_patch_bean_1" //保存服务器下发的数据，getServerBeanInStr
//    private const val TINKER_LAST_SERVER_PATCH_BEAN_2 = "last_server_patch_bean_2"
//
//    //如果这个值为true，则不再下载服务器补丁，除非focus字段为true，进入安全模式有两种情况，1首页自检crash大于3自动清除补丁，2补丁应用连续失败3次
//    private const val TINKER_SAFE_MODE_KEY = "safe_mode_key"
//    private const val TINKER_SAFE_MODE_COUNT_KEY = "safe_mode_count_key" //补丁加载失败时加1，如果连续大于3次，进入安全模式
//
//    private const val TINKER_LOCAL_DEBUG = false //如果是debug模式，那么加载mock的补丁，且跳过一系列限制
//
//    private fun mockData(): TinkerSeverInfoBean {
//        val tinkerSeverInfoBean = TinkerSeverInfoBean()
//        val patchData = PatchData()
//        patchData.patch_url =
//            "https://same4869-test.oss-cn-shanghai.aliyuncs.com/200303_patch_1.apk"
//        tinkerSeverInfoBean.data = patchData
//        return tinkerSeverInfoBean
//    }
//
//    fun syncPatchFromServer(context: Context) {
//        LogUtils.d("sodaTinker syncPatchFromServer")
//        TinkerPatchModel()
//            .requestGetPatch()?.subscribe(Consumer { tinkerSeverInfoBean1 ->
//
//                LogUtils.d("sodaTinker syncPatchFromServer TinkerSeverInfoBean $tinkerSeverInfoBean1")
//
//                val tinkerSeverInfoBean = if (TINKER_LOCAL_DEBUG) {
//                    mockData()
//                } else {
//                    tinkerSeverInfoBean1
//                }
//                if (!TINKER_LOCAL_DEBUG) {
//
//                    //服务器要求关闭，停止后续操作
//                    if (!tinkerSeverInfoBean.data.is_enable) {
//                        LogUtils.d("sodaTinker 接口要求关闭")
//                        return@Consumer
//                    }
//
//                    //如果channel数组为空，证明是全渠道，直接放行
//                    if (tinkerSeverInfoBean.data.channels.isNotEmpty()) {
//                        //数组中找不到该渠道
//                        if (tinkerSeverInfoBean.data.channels.indexOf(
//                                CommUtils.getAppChannel(
//                                    context
//                                )
//                            ) == -1
//                        ) {
//                            LogUtils.d("sodaTinker 没有匹配的渠道")
//                            return@Consumer
//                        }
//                    }
//
//                    if (tinkerSeverInfoBean.data.clear_patch) {
//                        LogUtils.d("sodaTinker 服务器要求清除补丁")
//                        TinkerInstaller.cleanPatch(context)
//                        if (!SPUtils.getInstance(SPUtils.SpName.SP_TABLE_TINKER)
//                                .getBoolean(Constants.PATCH_CLEAR_KEY, false)
//                        ) {
//                            uploadFeedbackAboutPatch(context, "clear patch", 1)
//                            savePatchClear(context, true)
//                        }
//                        clearServerBean(context)
//                        return@Consumer
//                    }
//
//                    //如果是安全模式且后台focusInSafeMode为关闭，则不再继续执行
//                    if (SPUtils.getInstance(SPUtils.SpName.SP_TABLE_TINKER).getBoolean(
//                            TINKER_SAFE_MODE_KEY,
//                            false
//                        ) && !tinkerSeverInfoBean.data.focus_in_safe_mode
//                    ) {
//                        LogUtils.d("sodaTinker 进入安全模式且后台没有强制")
//                        return@Consumer
//                    }
//
//                }
//
//                if (getServerBeanInStr(context) == GSON.toJson(tinkerSeverInfoBean) && !tinkerSeverInfoBean.data.focus) {
//                    LogUtils.d("sodaTinker 和上次保存的数据一致，忽略")
//                    return@Consumer
//                }
//
//                if (checkShouldDownPatch(tinkerSeverInfoBean) || TINKER_LOCAL_DEBUG) {
//                    LogUtils.d("sodaTinker 开始下载补丁")
//                    savePatchLoadSuc(context, false)
//                    savePatchLoadedSuc(context, false)
//                    savePatchClear(context, false)
//                    savePatchApply(context, false)
//                    downPatchAndVerify(
//                        context,
//                        context.filesDir.path + TINKER_PATCH_FOLDER + TINKER_PATCH_NAME,
//                        tinkerSeverInfoBean
//                    )
//                }
//
//            }, BaseErrorConsumer())
//
//    }
//
//    fun syncUpgradePatch(context: Context) {
//        LogUtils.d("sodaTinker syncUpgradePatch")
//        val lastServerPatchStr = getServerBeanInStr(context)
//        if (lastServerPatchStr.isBlank()) {
//            LogUtils.d("sodaTinker syncUpgradePatch lastServerPatchStr is blank")
//            return
//        }
//
//        val patchPath = context.filesDir.path + TINKER_PATCH_FOLDER + TINKER_PATCH_NAME
//
//        if (CommUtils.fileIsExists(patchPath)) {
//            val md5 =
//                MD5Utils.getFileMD5(File(patchPath))
//            val tinkerSeverInfoBean =
//                GSON.fromJson(lastServerPatchStr, TinkerSeverInfoBean::class.java)
//            LogUtils.d("sodaTinker md5${md5} lastServerPatchStr:$lastServerPatchStr")
//            if (md5 == tinkerSeverInfoBean.data.patch_md5 || TINKER_LOCAL_DEBUG) {
//                LogUtils.d("sodaTinker 尝试应用补丁")
//                uploadFeedbackAboutPatch(context, "patch apply", 1)
//                TinkerInstaller.onReceiveUpgradePatch(
//                    context, patchPath
//                )
//                savePatchApply(context, true)
//            } else {
//                LogUtils.d("sodaTinker 应用补丁时md5校验失败，删除文件")
//                CommUtils.deleteFile(File(patchPath))
//            }
//        } else {
//            LogUtils.d("sodaTinker syncUpgradePatch file not exits")
//        }
//    }
//
//    fun uploadFeedbackAboutPatch(
//        context: Context,
//        extra: String,
//        type: Int = 0,
//        patch_version: String = ""
//    ) {
//        LogUtils.d("sodaTinker uploadFeedbackAboutPatch extra:$extra")
//        val patchCreateFeedbackBean = PatchCreateFeedbackBean()
//        patchCreateFeedbackBean.app_version = DeviceUtils.getAppVersionCode(HYPERION_APPLICATION)
//        patchCreateFeedbackBean.channel = CommUtils.getAppChannel(HYPERION_APPLICATION)
//        patchCreateFeedbackBean.device_id = DeviceUtils.getDeviceId(HYPERION_APPLICATION)
//        val tinkerSeverInfoBean = GSON.fromJson(
//            getServerBeanInStr(context, type),
//            TinkerSeverInfoBean::class.java
//        )
//        if (tinkerSeverInfoBean?.data != null) {
//            patchCreateFeedbackBean.patch_version = tinkerSeverInfoBean.data.patch_version
//        } else {
//            if (type == 1) {
//                val tinkerSeverInfoBean2 = GSON.fromJson(
//                    getServerBeanInStr(context, 0),
//                    TinkerSeverInfoBean::class.java
//                )
//                if (tinkerSeverInfoBean2?.data != null) {
//                    patchCreateFeedbackBean.patch_version = tinkerSeverInfoBean2.data.patch_version
//                }
//            }
//        }
//        patchCreateFeedbackBean.extra_list.add(extra)
//        LogUtils.d("sodaTinker uploadFeedbackAboutPatch patchCreateFeedbackBean:$patchCreateFeedbackBean")
//        if (patch_version.isNotBlank()) {
//            patchCreateFeedbackBean.patch_version = patch_version
//        }
//        if (patchCreateFeedbackBean.patch_version.isBlank()) {
//            //如果还为空证明可能是第一次或者从未下载过补丁，不上报
//            return
//        }
//        TinkerPatchModel().requestCreateFeedback(patchCreateFeedbackBean)?.subscribe(Consumer {
//            LogUtils.d("sodaTinker uploadFeedbackAboutPatch suc patchCreateFeedbackBean:$patchCreateFeedbackBean")
//        }, BaseErrorConsumer())
//    }
//
//    fun initTinkerCall(context: Context) {
//        LogUtils.d(
//            "sodaTinker",
//            "sodaTinker initTinkerCall devicesId:" + DeviceUtils.getDeviceId(context)
//        )
//        sodaTinkerReport().setReporter(object : sodaTinkerReport.Reporter {
//            override fun onReport(message: String) {
//                LogUtils.d("sodaTinker", "onReport2 message : $message")
//                uploadFeedbackAboutPatch(context, "tinker report2 : $message")
//            }
//
//            override fun onReport(key: Int) {
//                LogUtils.d("sodaTinker", "onReport1 key : $key")
//                uploadFeedbackAboutPatch(context, "tinker report1 : $key")
//            }
//        })
//    }
//
//    fun savePatchLoadSuc(context: Context, isLoadSuc: Boolean) {
//        SPUtils.getInstance(SPUtils.SpName.SP_TABLE_TINKER)
//            .put(Constants.PATCH_LOAD_SUCCESS_KEY, isLoadSuc)
//        if (isLoadSuc) { //如果补丁应用成功 就把本地（如果还有）补丁文件清理掉
//            //CommUtils.deleteFile(File(context.filesDir.path + TINKER_PATCH_FOLDER + TINKER_PATCH_NAME))
//        }
//    }
//
//    //这个只是防止重复一直上报的
//    fun savePatchLoadedSuc(context: Context, isLoadedSuc: Boolean) {
//        SPUtils.getInstance(SPUtils.SpName.SP_TABLE_TINKER)
//            .put(Constants.PATCH_LOADED_SUCCESS_KEY, isLoadedSuc)
//    }
//
//    fun savePatchClear(context: Context, isclear: Boolean) {
//        SPUtils.getInstance(SPUtils.SpName.SP_TABLE_TINKER)
//            .put(Constants.PATCH_CLEAR_KEY, isclear)
//    }
//
//    fun savePatchApply(context: Context, isApply: Boolean) {
//        SPUtils.getInstance(SPUtils.SpName.SP_TABLE_TINKER)
//            .put(Constants.PATCH_IS_APPLY, isApply)
//    }
//
//    fun getPatchApply(context: Context): Boolean {
//        return SPUtils.getInstance(SPUtils.SpName.SP_TABLE_TINKER)
//            .getBoolean(Constants.PATCH_IS_APPLY, false)
//    }
//
//    fun uploadPatchSucIfNeed(context: Context) {
//        if (SPUtils.getInstance(SPUtils.SpName.SP_TABLE_TINKER)
//                .getBoolean(
//                    Constants.PATCH_LOAD_SUCCESS_KEY,
//                    false
//                ) && !SPUtils.getInstance(SPUtils.SpName.SP_TABLE_TINKER)
//                .getBoolean(Constants.PATCH_LOADED_SUCCESS_KEY, false)
//        ) {
//            uploadFeedbackAboutPatch(context, "patch load suc", 1)
//            savePatchLoadedSuc(context, true)
//        }
//    }
//
//    fun addSafeModeCount(context: Context): Int {
//        val count =
//            SPUtils.getInstance(SPUtils.SpName.SP_TABLE_TINKER).getInt(TINKER_SAFE_MODE_COUNT_KEY, 0)
//        SPUtils.getInstance(SPUtils.SpName.SP_TABLE_TINKER).put(TINKER_SAFE_MODE_COUNT_KEY, count + 1)
//        return count + 1
//    }
//
//    fun resetSafeModeCount(context: Context) {
//        SPUtils.getInstance(SPUtils.SpName.SP_TABLE_TINKER).put(TINKER_SAFE_MODE_COUNT_KEY, 0)
//    }
//
//    fun enterSafeMode(context: Context) {
//        uploadFeedbackAboutPatch(context, "enter safe mode", 1)
//        SPUtils.getInstance(SPUtils.SpName.SP_TABLE_TINKER).put(TINKER_SAFE_MODE_KEY, true)
//    }
//
//    fun exitSafeMode(context: Context) {
//        SPUtils.getInstance(SPUtils.SpName.SP_TABLE_TINKER).put(TINKER_SAFE_MODE_KEY, false)
//    }
//
//    private fun clearServerBean(context: Context) {
//        SPUtils.getInstance(SPUtils.SpName.SP_TABLE_TINKER).put(TINKER_LAST_SERVER_PATCH_BEAN_1, "")
//        SPUtils.getInstance(SPUtils.SpName.SP_TABLE_TINKER).put(TINKER_LAST_SERVER_PATCH_BEAN_2, "")
//    }
//
//    //这里使用了两个变量存服务器的下发配置，逻辑如下
//    //在应用补丁之前会读取上一次保存的服务器下发配置，这个时候type=0,要取last_server_patch_bean_2这个最新的，并同步给last_server_patch_bean_1
//    //在补丁应用的过程中（可能成功或失败回调之前），可能又会下载新的补丁配置，这时更新的是last_server_patch_bean_2
//    //补丁成功或失败上报是应该type=1，取原来的那个last_server_patch_bean_1
//    //0是服务器最新的，1是原来本地的（如果有）
//    private fun getServerBeanInStr(context: Context, type: Int = 0): String {
//        val lastServerPatchStr1 =
//            SPUtils.getInstance(SPUtils.SpName.SP_TABLE_TINKER).getString(TINKER_LAST_SERVER_PATCH_BEAN_1)
//        if (type == 1) {
//            return lastServerPatchStr1
//        }
//        val lastServerPatchStr2 =
//            SPUtils.getInstance(SPUtils.SpName.SP_TABLE_TINKER).getString(TINKER_LAST_SERVER_PATCH_BEAN_2)
//        return if (lastServerPatchStr1 == lastServerPatchStr2) {
//            lastServerPatchStr1
//        } else {
//            SPUtils.getInstance(SPUtils.SpName.SP_TABLE_TINKER)
//                .put(TINKER_LAST_SERVER_PATCH_BEAN_1, lastServerPatchStr2)
//            lastServerPatchStr2
//        }
//    }
//
//    private fun saveServerBeanInStr(context: Context, str: String) {
//        SPUtils.getInstance(SPUtils.SpName.SP_TABLE_TINKER).putSyn(TINKER_LAST_SERVER_PATCH_BEAN_2, str)
//    }
//
//    //根据app version判断是否下载补丁包
//    private fun checkShouldDownPatch(tinkerSeverInfoBean: TinkerSeverInfoBean): Boolean {
//        val appVersionName = DeviceUtils.getAppVersionCode(HYPERION_APPLICATION)
//        LogUtils.d("sodaTinker appVersionName : $appVersionName")
//        return tinkerSeverInfoBean.data.target_host_version == appVersionName
//    }
//
//    //下载及校验补丁包md5
//    private fun downPatchAndVerify(
//        context: Context,
//        filePath: String,
//        tinkerSeverInfoBean: TinkerSeverInfoBean
//    ) {
//        LogUtils.d("sodaTinker filePath : $filePath")
//        uploadFeedbackAboutPatch(
//            context,
//            "notify patch download",
//            patch_version = tinkerSeverInfoBean.data.patch_version
//        )
//        //这里应该是下载和校验流程，简化直接读本地文件
//        val md5 = MD5Utils.getFileMD5(File(path))
//        LogUtils.d("sodaTinker onFinishDownload md5 : $md5")
//        if (md5 == tinkerSeverInfoBean.data.patch_md5 || TINKER_LOCAL_DEBUG) {
//            LogUtils.d("sodaTinker 补丁下载并校验完成")
//            saveServerBeanInStr(context, GSON.toJson(tinkerSeverInfoBean))
//            uploadFeedbackAboutPatch(context, "patch downloaded")
//        } else {
//            CommUtils.deleteFile(File(path))
//            LogUtils.d("sodaTinker 文件校验失败 删除")
//        }
////        DownloadUtils.init(object : DownloadListener {
////            override fun onStartDownload() {
////                LogUtils.d("sodaTinker onStartDownload")
////            }
////
////            override fun onProgress(progress: Int) {
////                LogUtils.d("sodaTinker progress : $progress")
////            }
////
////            override fun onFinishDownload(url: String, path: String) {
////                LogUtils.d("sodaTinker onFinishDownload : $path")
////                val md5 = MD5Utils.getFileMD5(File(path))
////                LogUtils.d("sodaTinker onFinishDownload md5 : $md5")
////                if (md5 == tinkerSeverInfoBean.data.patch_md5 || TINKER_LOCAL_DEBUG) {
////                    LogUtils.d("sodaTinker 补丁下载并校验完成")
////                    saveServerBeanInStr(context, GSON.toJson(tinkerSeverInfoBean))
////                    uploadFeedbackAboutPatch(context, "patch downloaded")
////                } else {
////                    CommUtils.deleteFile(File(path))
////                    LogUtils.d("sodaTinker 文件校验失败 删除")
////                }
////            }
////
////            override fun onFail(url: String, errorInfo: String) {
////                LogUtils.d("sodaTinker onFail : $errorInfo")
////            }
////
////        }).download(tinkerSeverInfoBean.data.patch_url, filePath)
//    }
//
//}