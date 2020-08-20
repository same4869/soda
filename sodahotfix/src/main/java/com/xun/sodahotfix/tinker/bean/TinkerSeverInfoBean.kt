package com.xun.sodahotfix.tinker.bean

/**
 * @Description:
 * @Author:         xwang
 * @CreateDate:     2019-10-14
 */

class TinkerSeverInfoBean {
    var `data`: PatchData = PatchData()
    var message: String = ""
    var retcode: Int = 0

    override fun toString(): String {
        return "channels:${data.channels}, clear_patch:${data.clear_patch}, is_enable:${data.is_enable}, patch_md5:${data.patch_md5}, patch_url:${data.patch_url}, patch_version:${data.patch_version}, target_host_version:${data.target_host_version}},focus:${data.focus},focus_in_safe_mode:${data.focus_in_safe_mode}"
    }
}

class PatchData {
    var channels: List<String> = listOf() //返回空字符串匹配所有渠道
    var clear_patch: Boolean = false //是否清除补丁
    var is_enable: Boolean = false //服务器控制开关
    var patch_md5: String = ""//插件md5
    var patch_url: String = ""//插件下载地址
    var patch_version: String = "" //插件版本
    var target_host_version: String = "" //目标宿主版本
    var focus : Boolean = false //如果为false，上一次下载了补丁，下一次如果配置一样则不操作，如果是true，则删掉删一次的文件重新下载
    var focus_in_safe_mode : Boolean = false //如果app进入了安全模式，仍然强制重新下载应用补丁
}