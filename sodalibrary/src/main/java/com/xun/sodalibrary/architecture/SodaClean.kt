package com.xun.sodalibrary.architecture

import androidx.appcompat.app.AppCompatActivity

/**
 * @Description:
 * @Author:         xwang
 * @CreateDate:     2020/8/10
 */

object SodaClean {
    /**
     *实例化[SodaLifePresenter], 必须传入AppCompatActivity 。如果不需要生命周期的话可以直接继承自 [SodaPresenter],使用普通的构造方法
     * */
    inline fun <reified T : SodaLifePresenter> createPresenter(activity: AppCompatActivity): T {
        return PresenterFactory(activity).create(T::class.java)
    }

    /**
     * 实例化带有一个任意类型参数的[SodaLifePresenter]
     * */
    inline fun <reified T : SodaLifePresenter, reified P : Any> createPresenter(
        activity: AppCompatActivity,
        params1: P
    ): T {
        return PresenterFactory(activity).create(T::class.java, params1)
    }

    /**
     * 实例化带有两个任意类型参数的[SodaLifePresenter]
     * */
    inline fun <reified T : SodaLifePresenter, reified P1 : Any, reified P2 : Any> createPresenter(
        activity: AppCompatActivity,
        params1: P1,
        params2: P2
    ): T {
        return PresenterFactory(activity).create(T::class.java, params1, params2)
    }

    /**
     * 实例化带有三个任意类型参数的[SodaLifePresenter]
     * */
    inline fun <reified T : SodaLifePresenter, reified P1 : Any, reified P2 : Any, reified P3 : Any> createPresenter(
        activity: AppCompatActivity,
        params1: P1,
        params2: P2,
        params3: P3
    ): T {
        return PresenterFactory(activity).create(T::class.java, params1, params2, params3)
    }

    /**
     * 构造SodaLifePresenter
     * */
    class PresenterFactory(val activity: AppCompatActivity) {
        inline fun <reified T : SodaLifePresenter> create(presenterClass: Class<T>): T {
            if (SodaLifePresenter::class.java.isAssignableFrom(presenterClass)) {
                val obj = presenterClass.getConstructor().newInstance()
                (obj as SodaLifePresenter).injectLifeOwner(activity)
                return obj
            }
            throw IllegalArgumentException("Page Must Is Child of SodaLifePage")
        }

        inline fun <reified T : SodaLifePresenter, reified P : Any> create(
            presenterClass: Class<T>,
            param: P
        ): T {
            if (SodaLifePresenter::class.java.isAssignableFrom(presenterClass)) {
                val obj = presenterClass.getConstructor(P::class.java).newInstance(param)
                (obj as SodaLifePresenter).injectLifeOwner(activity)
                return obj
            }
            throw IllegalArgumentException("Page Must Is Child of SodaLifePage")
        }

        inline fun <reified T : SodaLifePresenter, reified P1 : Any, reified P2 : Any> create(
            presenterClass: Class<T>,
            param1: P1,
            param2: P2
        ): T {
            if (SodaLifePresenter::class.java.isAssignableFrom(presenterClass)) {
                val obj = presenterClass.getConstructor(P1::class.java, P2::class.java)
                    .newInstance(param1, param2)
                (obj as SodaLifePresenter).injectLifeOwner(activity)
                return obj
            }
            throw IllegalArgumentException("Page Must Is Child of LifePage")
        }

        inline fun <reified T : SodaLifePresenter, reified P1 : Any, reified P2 : Any, reified P3 : Any> create(
            presenterClass: Class<T>,
            param1: P1,
            param2: P2,
            param3: P3
        ): T {
            if (SodaLifePresenter::class.java.isAssignableFrom(presenterClass)) {
                val obj =
                    presenterClass.getConstructor(P1::class.java, P2::class.java, P3::class.java)
                        .newInstance(param1, param2, param3)
                (obj as SodaLifePresenter).injectLifeOwner(activity)
                return obj
            }
            throw IllegalArgumentException("Page Must Is Child of LifePage")
        }
    }
}
