package com.dangjang.android.data.interceptor

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

        headerAccessToken?.let {
            inAppStorageHelper.setAccessToken(ACCESS_TOKEN_KEY, it)
            //val refreshRequest = chain.request().newBuilder().addHeader("AccessToken", it).build()
            //chain.proceed(refreshRequest)
        }

        return response.newBuilder().body(response.body).build()
    }

}