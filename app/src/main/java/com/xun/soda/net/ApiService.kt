package com.xun.soda.net

import com.xun.soda.net.converter.bean.BaseBean
import com.xun.sodaability.mock.annotation.MOCK
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Headers

interface ApiService {
    @Headers(ApiType.HEADER_API_TAKUMI)
    @MOCK("mock_getMultiTokenByLoginTicket.json")
    @GET("misc/api/createVerification")
    fun requestCreateVerification(): Observable<BaseBean<Any>>
}