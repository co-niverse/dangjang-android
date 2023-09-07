package com.dangjang.android.data.datasource

import android.content.Context
import com.dangjang.android.data.storage.InAppStorageHelperImpl
import com.dangjang.android.domain.constants.ACCESS_TOKEN_KEY
import okhttp3.Interceptor
import okhttp3.Response

class TokenInterceptor(
    context: Context
) : Interceptor {

    private val inAppStorageHelper = InAppStorageHelperImpl(context)

    override fun intercept(chain: Interceptor.Chain): Response {
        val response = chain.proceed(chain.request())
        val headerAccessToken = response.headers.get("AccessToken")
        // TODO : 서버 토큰 로직 변경 시 수정
        val headerRefreshToken = response.headers.get("RefreshToken")

        inAppStorageHelper.setAccessToken(ACCESS_TOKEN_KEY, headerAccessToken)

        return response.newBuilder().body(response.body).build()
    }

}