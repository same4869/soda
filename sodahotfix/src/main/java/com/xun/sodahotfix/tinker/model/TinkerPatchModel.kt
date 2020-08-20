//package com.xun.sodahotfix.tinker.model
//
//import com.xun.sodahotfix.tinker.bean.TinkerSeverInfoBean
//import io.reactivex.Observable
//
///**
// * @Description:
// * @Author:         xwang
// * @CreateDate:     2019-11-04
// */
//
//class TinkerPatchModel {
//    fun requestGetPatch(): Observable<TinkerSeverInfoBean>? {
//        return RetrofitClient.getApiServiceWithBaseUrl().requestGetPatch().applySchedulers()
//    }
//
//    fun requestCreateFeedback(patchCreateFeedbackBean: PatchCreateFeedbackBean): Observable<BaseBean>? {
//        return RetrofitClient.getApiServiceWithBaseUrl()
//            .requestCreateFeedback(patchCreateFeedbackBean).applySchedulers()
//    }
//}