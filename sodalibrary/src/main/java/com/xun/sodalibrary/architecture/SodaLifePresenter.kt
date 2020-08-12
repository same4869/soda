package com.xun.sodalibrary.architecture

import androidx.appcompat.app.AppCompatActivity
import java.lang.ref.WeakReference
import kotlin.reflect.KClass

/**
 * @Description:
 * @Author:         xwang
 * @CreateDate:     2020/8/10
 */

abstract class SodaLifePresenter : SodaPresenter {

    private var lifeOwnerReference = WeakReference<AppCompatActivity>(null)

    fun getLifeOwner() = lifeOwnerReference.get()

    fun getContext() =  lifeOwnerReference.get()

    fun injectLifeOwner(lifecycleOwner: AppCompatActivity) {
        lifeOwnerReference = WeakReference(lifecycleOwner)
    }

    override fun <T : State> getStatus(statusClass: KClass<T>): T? {
        return null
    }

}