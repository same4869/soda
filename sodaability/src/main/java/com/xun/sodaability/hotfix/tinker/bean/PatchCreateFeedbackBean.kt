package com.xun.sodaability.hotfix.tinker.bean

/**
 * @Description:
 * @Author:         xwang
 * @CreateDate:     2020/8/25
 */

class PatchCreateFeedbackBean {
    var app_version: String = ""
    var channel: String = ""
    var device_id: String = ""
    var extra: String = ""
    var extra_list: ArrayList<String> = arrayListOf()
    var patch_version: String = ""

    override fun toString(): String {
        return "app_version:$app_version,channel:$channel,device_id:$device_id,patch_version:$patch_version,extra:$extra,extra_list:$extra_list"
    }
}