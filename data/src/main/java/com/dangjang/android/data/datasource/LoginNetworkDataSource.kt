package com.dangjang.android.data.datasource

import com.dangjang.android.domain.HttpResponseException
import com.dangjang.android.domain.HttpResponseStatus
import retrofit2.Response

abstract class LoginNetworkDataSource {

    protected fun <T> checkResponse(response: Response<T>): T {
        if (response.isSuccessful) {
            val headerAccessToken = response.headers().get("AccessToken")
            val headerRefreshToken = response.headers().get("RefreshToken")
            //TODO : save token

            return response.body()!!
        } else {
            val errorBody = response.errorBody()?.string()
            throw HttpResponseException(
                status = HttpResponseStatus.create(response.code()),
                httpCode = response.code(),
                errorRequestUrl = response.raw().request.url.toString(),
                message = "Http Request Failed (${response.code()}) ${response.message()}, $errorBody",
                cause = Throwable(errorBody),
            )
        }
    }
}