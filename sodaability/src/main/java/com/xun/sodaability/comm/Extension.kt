package com.xun.sodaability.comm

import android.annotation.SuppressLint
import android.view.View
import com.jakewharton.rxbinding3.view.clicks
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

//通用网络请求rx线程调度
fun <T> Observable<T>.applySchedulers(): Observable<T> {
    return subscribeOn(Schedulers.io())
        .unsubscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
}

//通用网络请求rx线程调度，单线程
fun <T> Observable<T>.applySingleSchedulers(): Observable<T> {
    return subscribeOn(Schedulers.single()).unsubscribeOn(Schedulers.single())
        .observeOn(AndroidSchedulers.mainThread())
}

typealias SimpleOnClickListener = () -> Unit

//点击事件，防抖500ms
fun View.onClick(onClick: SimpleOnClickListener) {
    throttleFirstClick(Consumer { onClick() })
}

//点击事件，防抖500ms
@SuppressLint("CheckResult")
internal fun View.throttleFirstClick(action: Consumer<Any?>) {
    this.clicks().throttleFirst(500, TimeUnit.MILLISECONDS)
        .subscribe(action, Consumer { it.printStackTrace() })
}