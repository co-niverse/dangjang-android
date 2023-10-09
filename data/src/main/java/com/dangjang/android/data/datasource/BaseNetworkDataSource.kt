package com.dangjang.android.data.datasource

import retrofit2.Response
import java.io.IOException

abstract class BaseNetworkDataSource {
    protected fun <T> checkResponse(response: Response<T>): T {
//        if (response.isSuccessful) {
//            return response.body()!!
//        } else {
//            val errorBody = response.errorBody()?.string()
//            throw HttpResponseException(
//                status = HttpResponseStatus.create(response.code()),
//                httpCode = response.code(),
//                errorRequestUrl = response.raw().request.url.toString(),
//                message = "Http Request Failed (${response.code()}) ${response.message()}, $errorBody",
//                cause = Throwable(errorBody),
//            )
//        }
        try {
            return response.body()!!
        } catch (e: IOException) {
            throw e
        }
    }
}