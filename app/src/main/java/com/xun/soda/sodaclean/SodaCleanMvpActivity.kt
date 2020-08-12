package com.xun.soda.sodaclean

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.xun.soda.R
import com.xun.sodalibrary.architecture.*
import com.xun.sodalibrary.log.SodaLog
import kotlinx.android.synthetic.main.activity_soda_clean_mvp.*

/**
 * 该页面发出的事件
 * */
class LoadData(val searchWord: String, val isLoadMore: Boolean) : Action

/**
 * 该页面需要的状态
 * */
class SodaMvpStatus(val currentPageSize: Int) : State

class SodaCleanMvpActivity : AppCompatActivity(), SodaRvPageProtocol {

    private val presenter: SodaLifePresenter by lazy {
        SodaClean.createPresenter<GithubPresenter, SodaRvPageProtocol>(this, this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_soda_clean_mvp)

        presenter.dispatch(LoadData("Android",false))
    }

    override fun refreshDatas(datas: List<Any>, isLoadMore: Boolean, extra: Any) {
        mTitle.text = "111111111"
        SodaLog.d("123 -> $datas")
    }

    override fun refreshPageStatus(status: String, extra: Any) {
    }
}