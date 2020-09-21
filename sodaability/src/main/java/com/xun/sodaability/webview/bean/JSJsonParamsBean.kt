package com.xun.sodaability.webview.bean

data class JSJsonParamsBean(
    var method: String = "",
    var callback: String = "",
    var payload: Payload = Payload()
)

data class Payload(
    var title: String = "",
    var page: String = "",
    var enable: Boolean = false,
    var type: String = "",
    var content: JSShareInfoBean = JSShareInfoBean(),
    var toast: String = "",
    var login_ticket: String = "",
    var action_type: String = "",
    var delta: Any = Any(),
    var html: String = "",
    var count: CountBean = CountBean(),
    var pageInfo: PageInfo = PageInfo(),
    var eventInfo: EventInfo = EventInfo(),
    var open_url: String = "",
    var name: String = "",
    var message: String = "",
    var isEmpty: Boolean = true,
    var image_list: List<ImageListItemBean> = arrayListOf(),
    var index: Int = 0,
    var forceRefresh: Boolean = false,
    var style: String? = null,
    var navigationBar: NavigationBar? = null,
    val statusBar: StatusBar? = null,
    var availableChannels: Int = 0x00,
    var game_biz: String = "",
    var region: String = "",
    var game_uid: Int = 0,
    var auth_appid: String = ""
)

data class ImageListItemBean(
    var url: String = "",
    var height: Int = 0,
    var width: Int = 0,
    var format: String = "",
    var size: String = ""
)

data class CountBean(
    var text: Int = 0,
    var img: Int = 0,
    var vote: Int = 0,
    var video: Int = 0,
    var emoticon: Int = 0
)

data class JSShareInfoBean(
    var title: String = "",
    var description: String = "",
    var link: String = "",
    var image_url: String = "",
    var image_base64: String = "",
    val preview: Boolean = false,
    val save_img: Boolean = true
)

data class PageInfo(
    var page_id: String = "",
    var page_name: String = "",
    var page_path: String = "",
    var page_type: String = "",
    var source_id: String = "",
    var source_name: String = "",
    var source_path: String = "",
    var sub_page_name: String = "",
    var sub_page_path: String = ""
)

data class EventInfo(
    var action_id: Int = 0,
    var btn_name: String = "",
    var btn_id: String = "",
    var index: String = "",
    var module_id: String = "",
    var module_name: String = "",
    var time: String = ""
)

data class NavigationBar(
    /**
     * 背景色
     */
    var backgroundColor: String? = null,
    /**
     * 文字和图标颜色
     */
    var tintColor: String? = null,
    /**
     * 是否显示导航栏下方的分割线(阴影)
     */
    var showBorder: Boolean? = null
)

data class StatusBar(
    var style: String? = null
) {
    fun isDark(): Boolean {
        return style == "dark"
    }

    fun isLight(): Boolean {
        return style == "light"
    }
}
