package com.xun.soda.sodaclean

import com.xun.soda.comm.disposeOnDestroy
import com.xun.soda.sodaclean.api.GithubService
import com.xun.sodaability.comm.applySchedulers
import com.xun.sodalibrary.architecture.Action
import com.xun.sodalibrary.architecture.SodaLifePresenter
import com.xun.sodalibrary.architecture.SodaPageStatus
import com.xun.sodalibrary.architecture.SodaRvPageProtocol

/**
 * @Description:
 * @Author:         xwang
 * @CreateDate:     2020/8/11
 */

class GithubPresenter(val view: SodaRvPageProtocol) : SodaLifePresenter() {
    private val apiService = GithubService.create()
    private val IN_QUALIFIER = "in:name,description"
    private var page = 0
    private var PAGE_SIZE = 20

    override fun dispatch(action: Action) {
        when (action) {
            is LoadData -> {
                loadSearchResult(action.searchWord, action.isLoadMore)
            }
        }
    }

    private fun loadSearchResult(searchWord: String, loadMore: Boolean) {
        if (!loadMore) {
            page = 0
        }
        val requestPage = if (loadMore) page + 1 else page
        apiService.searchRepos(searchWord + IN_QUALIFIER, requestPage, PAGE_SIZE).applySchedulers()
            .doOnSubscribe {
                view.refreshPageStatus(if (loadMore) SodaPageStatus.START_LOAD_MORE else SodaPageStatus.START_LOAD_PAGE_DATA)
            }.doOnTerminate {
                view.refreshPageStatus(if (loadMore) SodaPageStatus.END_LOAD_MORE else SodaPageStatus.END_LOAD_PAGE_DATA)
            }.subscribe({
                if (loadMore) {
                    page++
                }
                val list = arrayListOf<Any>("github 搜索 Android 的结果 : ")
                list.addAll(it.items)
                view.refreshDatas(list, loadMore)
            }, {
                view.refreshPageStatus(SodaPageStatus.NET_ERROR)
            }).disposeOnDestroy(getLifeOwner())
    }

}