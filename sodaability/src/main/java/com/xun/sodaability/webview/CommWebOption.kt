package com.xun.sodaability.webview

import java.io.Serializable

class CommWebOption : Serializable {
    var isShowBack = false
    var isShowClose = true
    var isShowBackPage = false
    var isShowForwordPage = false
    var isShowRefresh = false
    var isShowShare = false

    fun setShowBack(isShowBack: Boolean): CommWebOption {
        this.isShowBack = isShowBack
        return this
    }

    fun setShowClose(isShowClose: Boolean): CommWebOption {
        this.isShowClose = isShowClose
        return this
    }

    fun setShowBackPage(isShowBackPage: Boolean): CommWebOption {
        this.isShowBackPage = isShowBackPage
        return this
    }

    fun setShowForwordPage(isShowForwordPage: Boolean): CommWebOption {
        this.isShowForwordPage = isShowForwordPage
        return this
    }

    fun setShowRefresh(isShowRefresh: Boolean): CommWebOption {
        this.isShowRefresh = isShowRefresh
        return this
    }

    fun setShowShare(isShowShare: Boolean): CommWebOption {
        this.isShowShare = isShowShare
        return this
    }


}